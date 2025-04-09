package com.eMartix.authservice.messaging.producer;

import com.eMartix.authservice.configuration.RabbitMQConfig;
import com.eMartix.authservice.dto.request.MailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailProducer {

    private final AmqpTemplate amqpTemplate;

    public void sendOtpMail(MailRequestDto mailRequestDto) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                mailRequestDto
        );
    }
}
