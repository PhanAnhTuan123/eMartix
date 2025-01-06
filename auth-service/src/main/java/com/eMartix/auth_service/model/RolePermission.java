package com.eMartix.auth_service.model;

import com.eMartix.auth_service.model.ids.RolePermissionId;
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
public class RolePermission {
    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @MapsId("roleId")
    private Role role;


    @ManyToOne
    @MapsId("permissionId")
    private Permission permission;
}
