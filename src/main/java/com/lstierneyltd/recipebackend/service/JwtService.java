package com.lstierneyltd.recipebackend.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);

    Date extractExpirationDate(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    boolean isValidToken(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);
}
