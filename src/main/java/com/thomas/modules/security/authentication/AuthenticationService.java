package com.thomas.modules.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface AuthenticationService {
    Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities);
}
