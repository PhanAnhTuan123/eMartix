package com.eMartix.auth_service.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDto {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
