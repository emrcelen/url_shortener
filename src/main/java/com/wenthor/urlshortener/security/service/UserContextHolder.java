package com.wenthor.urlshortener.security.service;

import org.springframework.security.core.Authentication;

public class UserContextHolder {
    private static Authentication authentication;

    public static void setAuthentication(Authentication auth) {
        authentication = auth;
    }

    public static Authentication getAuthentication() {
        return authentication;
    }
}
