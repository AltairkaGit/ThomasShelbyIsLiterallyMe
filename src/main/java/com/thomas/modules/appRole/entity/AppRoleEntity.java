package com.thomas.modules.appRole.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.thomas.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "app_role")
public class AppRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_role_id", nullable = false)
    private Long appRoleId;
    @Column(name = "role_name", nullable = false)
    private String role;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<UserEntity> users;

    public Long getAppRoleId() {
        return appRoleId;
    }

    public void setAppRoleId(Long appRoleId) {
        this.appRoleId = appRoleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppRoleEntity that = (AppRoleEntity) o;

        if (!appRoleId.equals(that.appRoleId)) return false;
        return role.equals(that.role);
    }

    @Override
    public int hashCode() {
        int result = appRoleId.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }
}
