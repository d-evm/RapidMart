package com.rapidmart.security;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Intercepts WebSocket messages to extract and validate JWT during the handshake phase.
 * Ensures only authenticated users can establish a WebSocket connection.
 */
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // Wrap the message for STOMP header access
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Only intercept CONNECT commands (initial WebSocket handshake)
        if ("CONNECT".equals(accessor.getCommand().name())) {
            // Extract Authorization header
            List<String> authHeader = accessor.getNativeHeader("Authorization");
            if (authHeader != null && !authHeader.isEmpty()) {
                // Remove "Bearer " prefix to isolate token
                String token = authHeader.get(0).replace("Bearer ", "");

                try {
                    // Extract email from token and validate
                    String email = jwtUtil.extractUsername(token);
                    if (jwtUtil.validateToken(token)) {
                        // Load user details and authenticate the WebSocket connection
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        // Attach authenticated user to security context and accessor
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        accessor.setUser(auth);
                    }
                } catch (JwtException e) {
                    throw new IllegalArgumentException("Invalid JWT token");
                }
            }
        }

        return message;
    }
}
