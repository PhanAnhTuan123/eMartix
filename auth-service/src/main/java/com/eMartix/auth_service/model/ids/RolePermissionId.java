package com.eMartix.auth_service.model.ids;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class RolePermissionId implements Serializable {

    private String roleId;

    private String permissionId;

    // Equals and HashCode cần thiết cho lớp khóa composite
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermissionId that = (RolePermissionId) o;
        return roleId.equals(that.roleId) && permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }
}
