package com.thomas.modules.security.users;

import com.thomas.modules.appRole.entity.AppRoleEntity;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class UserDetailsFactory {
    public UserDetailsFactory() {
    }

    public static UserDetailsImpl create(UserEntity user) {
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                mapAppRolesToGrantedAuthorities(new ArrayList<>(user.getRoles()))
        );
    }
    public static UserDetailsImpl create(UserEntity user, Collection<GrantedAuthority> authorities) {
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities
        );
    }

    private static List<GrantedAuthority> mapAppRolesToGrantedAuthorities (List<AppRoleEntity> roles) {
        return roles.stream().map(role ->
                    new SimpleGrantedAuthority(role.getRole().toUpperCase())
                ).collect(Collectors.toList());
    }
}

