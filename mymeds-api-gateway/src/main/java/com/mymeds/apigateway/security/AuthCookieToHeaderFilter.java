package com.mymeds.apigateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthCookieToHeaderFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(AuthCookieToHeaderFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                .filter(cookie -> "authToken".equals(cookie.getName()))
                .findFirst()
                .ifPresentOrElse(
                    authCookie -> request.setAttribute(HttpHeaders.AUTHORIZATION, "Bearer " + authCookie.getValue()),
                    () -> LOG.info("action=AuthCookieToHeaderFilter.doFilterInternal, message=couldn't extract token")
                );
        }
        filterChain.doFilter(request, response);
    }
}
