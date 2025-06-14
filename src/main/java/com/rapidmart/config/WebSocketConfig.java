package com.rapidmart.config;

import com.rapidmart.security.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.*;
import org.springframework.web.socket.config.annotation.*;

/**
 * Configures Spring WebSocket with STOMP protocol,
 * defines endpoint and registers JWT interceptor for authentication.
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    /**
     * Register STOMP endpoint for WebSocket connections.
     * Enables SockJS fallback for browsers that don’t support WebSocket.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // allow all origins
                .withSockJS(); // support fallback options
    }

    /**
     * Configure message broker to handle message routing.
     * "/topic" is used for broadcasting messages.
     * "/app" is used for application-level message mapping.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // server-to-client
        config.setApplicationDestinationPrefixes("/app"); // client-to-server
    }

    /**
     * Attach custom JWT interceptor to incoming WebSocket messages.
     * This secures the WebSocket handshake using JWT tokens.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }
}
