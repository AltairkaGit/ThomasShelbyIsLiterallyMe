package com.thomas.modules.security.tokenExtractor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
class ExtractTokenCookieStrategy implements ExtractTokenStrategy {
    @Override
    public String extractToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (var cookie : cookies) {
            if (tokenHeader.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
