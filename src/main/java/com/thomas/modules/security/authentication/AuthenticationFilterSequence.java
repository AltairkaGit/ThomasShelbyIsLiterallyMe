package com.thomas.modules.security.authentication;

import com.thomas.modules.security.composer.FilterSequence;
import com.thomas.modules.security.composer.context.ComposerContext;
import com.thomas.modules.security.composer.context.ComposerContextEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Order(3)
public class AuthenticationFilterSequence implements FilterSequence {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationFilterSequence(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, ComposerContext context)
            throws ServletException, IOException, AuthenticationException, NoSuchElementException {
        Long userId = context.get(ComposerContextEnum.UserId);
        List<GrantedAuthority> chatAuthorities = context.get(ComposerContextEnum.ChatAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationService.getAuthentication(userId, chatAuthorities));
    }
}
