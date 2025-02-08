package com.eMartix.auth_service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class TokenResponseDto implements Serializable {
    private String accessToken;
    private String refreshToken;
}
