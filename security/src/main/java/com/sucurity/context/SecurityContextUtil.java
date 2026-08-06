package com.sucurity.context;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sucurity.exception.UnauthorizedException;
import com.sucurity.model.AuthUser;
import com.sucurity.model.SecurityPrincipal;

public class SecurityContextUtil {
    
    private SecurityContextUtil() {}
    
    public static AuthUser currentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null
        ) {

            throw new UnauthorizedException("User not authenticated");

        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof SecurityPrincipal securityPrincipal)) {

            throw new UnauthorizedException("Invalid security information.");

        }
        
        return securityPrincipal.getUser();
        
    }

    public static String currentUserId() {
        
        return currentUser().userId();
    }

    public static String currentCompanyId() {
        
        return currentUser().companyId();
    }

    public static List<String> currentRoles() {
        
        return currentUser().roles();
    }
}
