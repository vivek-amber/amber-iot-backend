package com.automation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sucurity.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter authFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.csrf(csrf -> csrf.disable())
        .sessionManagement(session ->
            session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        )
        .authorizeHttpRequests(req->
            req.anyRequest()
            .authenticated()
        )
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
