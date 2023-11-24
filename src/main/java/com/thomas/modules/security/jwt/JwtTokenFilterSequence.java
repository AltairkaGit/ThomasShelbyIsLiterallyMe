package com.thomas.modules.security.jwt;

import com.thomas.modules.security.composer.FilterSequence;
import com.thomas.modules.security.composer.context.ComposerContext;
import com.thomas.modules.security.composer.context.ComposerContextEnum;
import com.thomas.modules.security.tokenExtractor.TokenExtractor;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
@Order(1)
public class JwtTokenFilterSequence implements FilterSequence {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenExtractor tokenExtractor;

    @Autowired
    public JwtTokenFilterSequence(
            JwtTokenProvider jwtTokenProvider,
            TokenExtractor tokenExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, ComposerContext context)
            throws IOException, ServletException, AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String access = tokenExtractor.extract(httpServletRequest);
        if (access == null ) throw new AuthenticationException("no authorization header");
        if (access.isEmpty()) throw new AuthenticationException("empty access token");
        if (!jwtTokenProvider.validateAccess(access)) throw new AuthenticationException("access token is not ok");
        context.put(ComposerContextEnum.UserId, jwtTokenProvider.getUserId(access));
        Long userId = jwtTokenProvider.getUserId(access);
        request.setAttribute("reqUserId", userId);
        request.setAttribute("accessToken", access);
    }


}
