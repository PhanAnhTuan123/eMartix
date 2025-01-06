package com.eMartix.auth_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token extends BaseEntity {

    private String accessToken;

    private String refreshToken;

    // th/gian het han
    private LocalDateTime expiryDate;

    // trang thai token(co bi thu hoi hay khong)
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
