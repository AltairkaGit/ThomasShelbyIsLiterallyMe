package com.thomas.config;

import com.thomas.lib.ws.impl.HandShakeHandlerImpl;
import com.thomas.modules.security.websocket.ConnectionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(3)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final ConnectionInterceptor connectionInterceptor;
    private final HandShakeHandlerImpl handShakeHandler;

    @Autowired
    public WebSocketConfig(ConnectionInterceptor connectionInterceptor, HandShakeHandlerImpl handShakeHandler) {
        this.connectionInterceptor = connectionInterceptor;
        this.handShakeHandler = handShakeHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(handShakeHandler)
                .addInterceptors(connectionInterceptor);
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(handShakeHandler)
                .addInterceptors(connectionInterceptor)
                .withSockJS();
    }
}
