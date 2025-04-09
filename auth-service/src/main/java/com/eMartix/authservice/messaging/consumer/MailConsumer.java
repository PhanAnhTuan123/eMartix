package com.eMartix.authservice.messaging.consumer;

import com.eMartix.authservice.configuration.RabbitMQConfig;
import com.eMartix.authservice.dto.request.MailRequestDto;
import com.eMartix.authservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleMail(MailRequestDto mailRequestDto) {
        emailService.sendEmailProviderToken(
                mailRequestDto.getTo(),
                mailRequestDto.getSubject(),
                mailRequestDto.getBody()
        );
    }
}
