package com.lstierneyltd.recipebackend.config;

import com.lstierneyltd.recipebackend.service.CustomUserDetailsService;
import com.lstierneyltd.recipebackend.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    private final JwtService jwtService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtService jwtService) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
    }

    @Bean
    @Profile("!test")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("*"));
                    cors.setAllowedMethods(List.of("*"));
                    cors.setAllowedHeaders(List.of("*"));
                    return cors;
                }).and()
                .csrf().disable()
                .headers(headers -> headers.frameOptions().disable()) // to allow H2 console to work with Spring Security
                .addFilterAfter(new JwtAuthenticationFilter(customUserDetailsService, jwtService), UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()
//                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Profile("test")
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .build();
    }
}
