package com.sucurity.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sucurity.constants.SecurityConstants;
import com.sucurity.exception.InvalidTokenException;
import com.sucurity.model.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(AuthUser user) {

        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(user.userId())
                .claim(SecurityConstants.COMPANY_ID, user.companyId())
                .claim(SecurityConstants.ROLES,user.roles())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(
                    Keys.hmacShaKeyFor(
                        secret.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }
    
    public void validateToken(String token) {

        try {

            getClaims(token);

        } catch (ExpiredJwtException ex) {

            throw new InvalidTokenException("Token Expired");
        } catch (JwtException | IllegalArgumentException ex){
            throw new InvalidTokenException("Invalid Token");
        }
    }
    

    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(
                        Keys.hmacShaKeyFor(
                                secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public List<String> getRoles(String token) {

        Object roles = getClaims(token)
                .get(SecurityConstants.ROLES);
    
        if (roles instanceof List<?> list) {
    
            return list.stream()
                    .map(String::valueOf)
                    .toList();
        }
    
        return List.of();
    }
    
    public AuthUser getAuthUser(String token) {

        Claims claims = getClaims(token);

        return new AuthUser(
            claims.getSubject(),
            claims.get(SecurityConstants.COMPANY_ID,String.class),
            getRoles(token)
        );
    }
}
