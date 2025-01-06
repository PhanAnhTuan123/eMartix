package com.eMartix.auth_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private String fullName;
    private boolean status;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user")
    private Set<Token> tokens;
}
