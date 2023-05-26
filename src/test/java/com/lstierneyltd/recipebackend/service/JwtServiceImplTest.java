package com.lstierneyltd.recipebackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class JwtServiceImplTest {
    private static final String SECRET = "This secret is only for tests";

    private static final int EXPIRATION = 86400; // one day

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private UserDetails userDetails;

    private JwtService jwtService;

    private String token;

    @BeforeEach
    void init() {
        userDetails = new org.springframework.security.core.userdetails.User(USERNAME, PASSWORD, new ArrayList<>());

        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);
        token = jwtService.generateToken(userDetails);
    }

    @Test
    public void testExtractUsername() {
        assertThat(jwtService.extractUsername(token), equalTo(USERNAME));
    }

    @Test
    public void testExtractExpirationDate() {
        Date expirationDate = jwtService.extractExpirationDate(token);
        Date now = new Date();

        long difference = Math.abs(expirationDate.getTime() / 1000 - now.getTime() / 1000);
        boolean isWithinADay = difference - 5000 <= EXPIRATION;

        assertThat(isWithinADay, is(true));
    }

    @Test
    public void testValidateToken() {
        boolean isTokenValid = jwtService.isValidToken(token, userDetails);
        assertThat(isTokenValid, is(true));
    }
}

