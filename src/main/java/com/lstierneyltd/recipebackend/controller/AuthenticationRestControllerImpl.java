package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.User;
import com.lstierneyltd.recipebackend.service.CustomUserDetailsService;
import com.lstierneyltd.recipebackend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
@CrossOrigin(origins = "${cross.origin.allowed.host}")
public class AuthenticationRestControllerImpl {
    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    public AuthenticationRestControllerImpl(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String storedHashedPassword = userDetails.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean passwordMatch = passwordEncoder.matches(password, storedHashedPassword); // compare raw password with hashed

        if (!passwordMatch) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }
}
