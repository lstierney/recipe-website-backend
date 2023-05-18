package com.lstierneyltd.recipebackend.config;

import com.lstierneyltd.recipebackend.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String secret;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(String secret, CustomUserDetailsService customUserDetailsService) {
        this.secret = secret;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtToken(request);
            if (jwt != null && validateJwtToken(jwt)) {
                Authentication authentication = getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            throw new RuntimeException("Exception whilst extracting/validating JWT Token: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private boolean validateJwtToken(String jwt) {
        // TODO - implement this
        return true; // Return true if the token is valid, false otherwise
    }

    private Authentication getAuthentication(String jwt) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
        String username = claims.getSubject();
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

