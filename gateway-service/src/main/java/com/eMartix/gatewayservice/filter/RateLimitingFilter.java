package com.eMartix.gatewayservice.filter;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RateLimitingFilter implements WebFilter {

    private static final int MAX_REQUESTS = 10; // Maximum requests per IP per minute
    private static final long TIME_FRAME = 60; // Time frame in seconds (1 minute)

    @Autowired
    private StatefulRedisConnection<String, String> redisConnection;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Lấy địa chỉ IP của client
        String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        log.info("RateLimitingFilter: clientIp = {}", clientIp);

        // Tạo key Redis để lưu số lượng yêu cầu của mỗi IP
        String key = "rate_limit:" + clientIp;

        // Sử dụng Redis để kiểm tra và cập nhật số lượng yêu cầu của IP
        return Mono.fromCallable(() -> {
                    long currentRequests = redisConnection.sync().get(key) != null ?
                            Long.parseLong(redisConnection.sync().get(key)) : 0L;

                    // Nếu không có dữ liệu (lần đầu yêu cầu), tạo mới và thiết lập thời gian sống
                    if (currentRequests == 0) {
                        redisConnection.sync().setex(key, TIME_FRAME, "1");
                        return null;
                    }

                    // Nếu số lượng yêu cầu vượt quá giới hạn
                    if (currentRequests >= MAX_REQUESTS) {
                        throw new RateLimitExceededException("Rate limit exceeded for IP: " + clientIp);
                    }

                    // Tăng số lượng yêu cầu
                    redisConnection.sync().incr(key);
                    return null;
                })
                .onErrorResume(e -> {
                    // Nếu đã vượt quá giới hạn, trả về lỗi
                    if (e instanceof RateLimitExceededException) {
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    }
                    return Mono.error(e);
                })
                .then(chain.filter(exchange));
    }

    // Định nghĩa lỗi khi rate limit vượt quá
    public static class RateLimitExceededException extends RuntimeException {
        public RateLimitExceededException(String message) {
            super(message);
        }
    }
}
