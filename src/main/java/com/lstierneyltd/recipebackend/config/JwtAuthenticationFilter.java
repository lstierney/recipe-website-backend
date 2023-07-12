package com.lstierneyltd.recipebackend.config;

import com.lstierneyltd.recipebackend.service.CustomUserDetailsService;
import com.lstierneyltd.recipebackend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtService jwtService;

    public JwtAuthenticationFilter(CustomUserDetailsService customUserDetailsService, JwtService jwtService) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtToken(request);
            if (jwt != null && !jwtService.isTokenExpired(jwt)) {
                Authentication authentication = getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                setResponseAsUnauthorised(response, request);
                return;
            }
        } catch (JwtException e) {
            setResponseAsUnauthorised(response, request);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void setResponseAsUnauthorised(HttpServletResponse response, HttpServletRequest request) {
        StringBuffer requestUrl = request.getRequestURL();
        logger.info("Returning 401 Unauthorised for request URL: " + requestUrl);

        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("\"error\": \"Unauthorized\",");
        sb.append("\"message\": \"Invalid Token.\",");
        sb.append("\"path\": \"")
                .append(requestUrl)
                .append("\"");
        sb.append("} ");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            response.getWriter().write(sb.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String extractJwtToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private Authentication getAuthentication(String jwt) {
        String username = jwtService.extractUsername(jwt);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String servletPath = request.getServletPath();

        return method.equals("GET") || method.equals("OPTIONS") || servletPath.equals("/authenticate");
    }
}

