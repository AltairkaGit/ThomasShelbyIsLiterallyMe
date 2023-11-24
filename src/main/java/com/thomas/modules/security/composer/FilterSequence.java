package com.thomas.modules.security.composer;

import com.thomas.modules.security.composer.context.ComposerContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import javax.naming.AuthenticationException;
import java.io.IOException;

public interface FilterSequence {
    void doFilter(ServletRequest request, ServletResponse response, ComposerContext context) throws ServletException, IOException, AuthenticationException;
}
