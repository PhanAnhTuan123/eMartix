package com.eMartix.authservice.dto.response;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {
    private String accessToken;
}
