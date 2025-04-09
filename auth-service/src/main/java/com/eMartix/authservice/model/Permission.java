package com.eMartix.authservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_permissions")
public class Permission extends BaseEntity{

    @Column(name = "name", unique = true)
    private String permissionName;

    @Column(name = "description")
    private String permissionDescription;

    @JsonManagedReference
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RolePermission> rolePermissions = new HashSet<>();
}
