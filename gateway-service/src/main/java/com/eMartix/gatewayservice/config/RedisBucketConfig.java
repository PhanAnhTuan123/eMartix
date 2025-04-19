package com.eMartix.gatewayservice.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisBucketConfig {

    @Value("${spring.data.redis.password:#{null}}")  // Mặc định null nếu không có
    private String redisPassword;

    @Value("${spring.data.redis.host:localhost}")  // Mặc định null nếu không có
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")  // Mặc định null nếu không có
    private int redisPort;

    /**
     * Bean RedisClient tạo kết nối Redis.
     */
    @Bean(destroyMethod = "shutdown")
    public RedisClient redisClient() {
        RedisURI redisURI = RedisURI.builder()
                .withHost(redisHost)
                .withPort(redisPort)
                .withPassword(redisPassword != null ? redisPassword.toCharArray() : null)
                .build();
        return RedisClient.create(redisURI);
    }

    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, String> redisConnection(RedisClient redisClient) {
        return redisClient.connect();
    }

}
