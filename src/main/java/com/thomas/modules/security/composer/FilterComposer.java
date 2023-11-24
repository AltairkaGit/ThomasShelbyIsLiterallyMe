package com.thomas.modules.security.composer;

import com.thomas.modules.security.composer.context.ComposerContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class FilterComposer extends OncePerRequestFilter {
    private final List<FilterSequence> filters;
    private final List<String> permitted;
    public FilterComposer(List<FilterSequence> filters, List<String> permitted) {
        this.filters = filters;
        this.permitted = permitted;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return permitted.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
    }

    @Override
    public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ComposerContext context = new ComposerContext();
        try {
            for (FilterSequence filter : filters)
                filter.doFilter(servletRequest, servletResponse, context);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (
                MalformedJwtException | SignatureException | NoSuchElementException | AuthenticationException ex
        ) {
            ResponseEntity<String> res = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
            sendErrorResponse(servletResponse, res);
        }
    }

    private void sendErrorResponse(ServletResponse servletResponse, ResponseEntity<String> errorResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setStatus(errorResponse.getStatusCodeValue());
        response.setContentType("application/json");
        response.getWriter().write(errorResponse.getBody());
    }
}
