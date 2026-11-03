package com.sucurity.utils;

import com.sucurity.constants.SecurityConstants;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class TokenExtractor {

    public static String extract(HttpServletRequest request) {

        String authHeader = request.getHeader(SecurityConstants.AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {

            return authHeader.substring(7);
        }

        if (request.getCookies() != null) {

            for (Cookie cookie : request.getCookies()) {

                if (SecurityConstants.ACCESS_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
