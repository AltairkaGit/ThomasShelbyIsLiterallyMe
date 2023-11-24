package com.thomas.modules.security.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            Long id,
            String username,
            String password,
            String email,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getEmail() {
        return email;
    }
}
