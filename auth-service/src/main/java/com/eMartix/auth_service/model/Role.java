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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity{

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDescription;

    // Mối quan hệ với RolePermission (One-to-Many)
    @OneToMany(mappedBy = "role")
    private Set<RolePermission> rolePermissions;

    // Mối quan hệ với UserRole (One-to-Many)
    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles;
}
