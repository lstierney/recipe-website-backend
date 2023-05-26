package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.User;
import com.lstierneyltd.recipebackend.service.CustomUserDetailsService;
import com.lstierneyltd.recipebackend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class AuthenticationRestControllerImpl {
    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    private final AuthenticationConfiguration authenticationConfiguration;

    public AuthenticationRestControllerImpl(JwtService jwtService, CustomUserDetailsService userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user)
            throws Exception {
        try {
            authenticationConfiguration.getAuthenticationManager().authenticate(

                    new UsernamePasswordAuthenticationToken(user.getUsername(),
                            user.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user.getUsername());

        final String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }
}
