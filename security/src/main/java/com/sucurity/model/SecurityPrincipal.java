package com.sucurity.model;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SecurityPrincipal implements UserDetails{
    
    private final AuthUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return user.roles()
        .stream()
        .map(SimpleGrantedAuthority::new)
        .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.userId();
    }
    
    public String getCompanyId() {
        return user.companyId();
    }

    public String getSessionId() {
        return user.sessionId();
    }
    
    public Integer getTokenVersion() {
        return user.tokenVersion();
    }

}
