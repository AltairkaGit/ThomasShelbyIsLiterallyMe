package com.thomas.modules.security.tokenExtractor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;

public interface TokenExtractor {
    String extract(HttpServletRequest req);
    String extract(ServerHttpRequest req);
}
