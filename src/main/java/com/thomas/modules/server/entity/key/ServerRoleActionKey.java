package com.thomas.modules.server.entity.key;

import com.thomas.roles.ServerRoleAction;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.io.Serializable;

@Embeddable
public class ServerRoleActionKey implements Serializable {
    public static ServerRoleActionKey valueOf(ServerRoleKey roleKey, ServerRoleAction roleAction) {
        ServerRoleActionKey res = new ServerRoleActionKey();
        res.setRoleKey(roleKey);
        res.setRoleAction(roleAction.toString());
        return res;
    }

    @Embedded
    private ServerRoleKey roleId;

    @Column(name = "role_action")
    private String roleAction;

    public ServerRoleKey getRoleId() {
        return roleId;
    }

    public void setRoleKey(ServerRoleKey roleId) {
        this.roleId = roleId;
    }

    public String getRoleAction() {
        return roleAction;
    }

    public void setRoleAction(String roleAction) {
        this.roleAction = roleAction;
    }

    public void setRoleId(ServerRoleKey roleId) {
        this.roleId = roleId;
    }
}
