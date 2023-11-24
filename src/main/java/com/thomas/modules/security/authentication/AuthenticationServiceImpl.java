package com.thomas.modules.security.authentication;

import com.thomas.modules.security.users.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationServiceImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities) {
        UserDetails userDetails = userDetailsService.loadByUserId(userId);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (authorities != null) authorityList.addAll(authorities);
        Collection<? extends GrantedAuthority> a = userDetails.getAuthorities();
        if (a != null) authorityList.addAll(a);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorityList);
    }

}
