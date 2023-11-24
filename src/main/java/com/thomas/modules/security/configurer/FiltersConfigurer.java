package com.thomas.modules.security.configurer;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

public class FiltersConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final GenericFilterBean filter;
    public FiltersConfigurer(GenericFilterBean filter) {
        this.filter = filter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

}
