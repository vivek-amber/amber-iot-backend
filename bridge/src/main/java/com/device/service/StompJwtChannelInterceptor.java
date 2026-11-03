/*
 * @file    StompJwtChannelInterceptor.java
 * @brief   Validates JWT authentication for inbound STOMP connections.
 * @author  Vivek
 * @date    2026-10-29
 */

package com.device.service;

import java.util.List;
import java.util.Locale;

import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.sucurity.exception.InvalidTokenException;
import com.sucurity.jwt.JwtUtil;
import com.sucurity.model.AuthUser;
import com.sucurity.model.SecurityPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor 
public final class StompJwtChannelInterceptor
    implements ChannelInterceptor
{
    private static final String AUTHORIZATION_HEADER ="Authorization";
    private static final String BEARER_PREFIX ="Bearer ";
    private final JwtUtil jwtUtil;

    @Override
    public @Nullable Message<?> preSend(Message<?> message,MessageChannel channel)
    {
        StompHeaderAccessor accessor =MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);

        if (accessor == null)
        {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand()))
        {
            authenticateConnection(accessor);
        }

        return message;
    }

    private void authenticateConnection(StompHeaderAccessor accessor)
    {
        String sessionId = accessor.getSessionId();

        String authorizationHeader = getFirstNativeHeader(accessor,AUTHORIZATION_HEADER);

        if ((authorizationHeader == null) ||
            authorizationHeader.isBlank())
        {
            throw reject(sessionId,"Authorization header is missing");
        }

        /*
         * Bearer scheme names are case-insensitive.
         */
        if (!authorizationHeader.regionMatches(
                true,
                0,
                BEARER_PREFIX,
                0,
                BEARER_PREFIX.length()))
        {
            throw reject(
                sessionId,
                "Authorization header must use Bearer authentication");
        }

        String accessToken =
            authorizationHeader
                .substring(BEARER_PREFIX.length())
                .trim();

        if (accessToken.isEmpty())
        {
            throw reject(
                sessionId,
                "Access token is empty");
        }

        try
        {
            /*
             * This verifies the JWT signature, structure and
             * expiration using the configured signing key.
             */
            jwtUtil.validateToken(accessToken);

            AuthUser authUser = jwtUtil.getAuthUser(accessToken);

            validateAuthUser(authUser,sessionId);

            SecurityPrincipal securityPrincipal = new SecurityPrincipal(authUser);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    securityPrincipal,
                    null,
                    securityPrincipal.getAuthorities());

            /*
             * Associate the authenticated principal with the complete
             * STOMP session. Spring carries this principal into later
             * SEND, SUBSCRIBE, UNSUBSCRIBE and DISCONNECT frames.
             */
            accessor.setUser(authentication);

            log.info(
                "STOMP authentication successful: userId={}, sessionId={}",
                securityPrincipal.getUsername(),
                sessionId);
        }
        catch (InvalidTokenException exception)
        {
            /*
             * Preserve the original expired/invalid-token reason
             * produced by JwtUtil.
             */
            log.warn(
                "STOMP authentication rejected: sessionId={}, reason={}",
                sessionId,
                exception.getMessage());

            throw exception;
        }
        catch (Exception exception)
        {
            log.warn(
                "STOMP authentication failed: sessionId={}, reason={}",
                sessionId,
                exception.getClass().getSimpleName());

            throw new InvalidTokenException(
                "Access token validation failed");
        }
    }

    private void validateAuthUser(
        AuthUser authUser,
        String stompSessionId)
    {
        if (authUser == null)
        {
            throw reject(stompSessionId,"Authenticated user is unavailable");
        }

        if ((authUser.userId() == null) ||
            authUser.userId().isBlank())
        {
            throw reject(
                stompSessionId,
                "JWT user ID is unavailable");
        }

        if ((authUser.companyId() == null) ||
            authUser.companyId().isBlank())
        {
            throw reject(
                stompSessionId,
                "JWT company ID is unavailable");
        }

        if (authUser.tokenVersion() == null)
        {
            throw reject(
                stompSessionId,
                "JWT token version is unavailable");
        }

        /*
         * Access tokens generated without sessionId may be valid in
         * other workflows. Remove this check if sessionId is optional
         * for WebSocket authentication.
         */
        if ((authUser.sessionId() == null) ||
            authUser.sessionId().isBlank())
        {
            throw reject(
                stompSessionId,
                "JWT session ID is unavailable");
        }
    }

    private String getFirstNativeHeader(
        StompHeaderAccessor accessor,
        String headerName)
    {
        List<String> values =
            accessor.getNativeHeader(headerName);

        if ((values == null) || values.isEmpty())
        {
            /*
             * Some clients or gateways normalize native STOMP header
             * names to lowercase.
             */
            values = accessor.getNativeHeader(
                headerName.toLowerCase(Locale.ROOT));
        }

        if ((values == null) || values.isEmpty())
        {
            return null;
        }

        String headerValue = values.get(0);

        return headerValue == null
            ? null
            : headerValue.trim();
    }

    private InvalidTokenException reject(String sessionId,String reason)
    {

        log.warn(
        "STOMP authentication rejected: sessionId={}, reason={}",
        sessionId,
        reason);

        return new InvalidTokenException(reason);
    }
}