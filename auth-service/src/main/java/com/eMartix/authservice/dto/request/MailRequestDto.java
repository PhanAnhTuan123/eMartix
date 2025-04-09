package com.eMartix.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailRequestDto {
    private String to;
    private String subject;
    private String body;
}
