package com.thomas.modules.security.websocket;

import com.thomas.modules.security.jwt.JwtTokenProvider;
import com.thomas.modules.security.tokenExtractor.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class ConnectionInterceptor implements HandshakeInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenExtractor tokenExtractor;
    @Autowired
    public ConnectionInterceptor(
            JwtTokenProvider jwtTokenProvider,
            TokenExtractor tokenExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String access = tokenExtractor.extract(request);
        try {
            if (access == null || access.isEmpty() || !jwtTokenProvider.validateAccess(access))
                return false;
        } catch (Exception ex) {
            return false;
        }
        Long userId = jwtTokenProvider.getUserId(access);
        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
