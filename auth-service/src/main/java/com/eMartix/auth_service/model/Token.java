package com.eMartix.auth_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_token")
@Builder
//public class Token extends BaseEntity<String> {
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hoặc GenerationType.AUTO
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;
}
