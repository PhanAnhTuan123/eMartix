package com.eMartix.auth_service.model;

import com.eMartix.auth_service.model.ids.UserRoleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    private Role role;
}
