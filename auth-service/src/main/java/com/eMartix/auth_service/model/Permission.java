package com.eMartix.auth_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Permission extends BaseEntity{

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "permission_desc")
    private String permissionDescription;

    @OneToMany(mappedBy = "permission")
    private Set<RolePermission> rolePermissions;
}
