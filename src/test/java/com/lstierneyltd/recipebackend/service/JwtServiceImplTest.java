package com.lstierneyltd.recipebackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {
    private static final String SECRET = "This secret is only for tests";

    private static final int EXPIRATION = 86400; // one day

    private static final String USERNAME = "lawrence";
    private static final String PASSWORD = "password";

    private UserDetails userDetails;

    private JwtService jwtService;

    private String token;

    @BeforeEach
    void init() {
        userDetails = new org.springframework.security.core.userdetails.User(USERNAME, PASSWORD, new ArrayList<>());

        jwtService = spy(new JwtServiceImpl());
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
    public void isValidToken_true() {
        assertTrue(jwtService.isValidToken(token, userDetails));
    }

    @Test
    public void isValidToken_false_userNamesDontMatch() {
        given(jwtService.extractUsername(token)).willReturn("Another User");

        assertFalse(jwtService.isValidToken(token, userDetails));
    }

    @Test
    public void isValidToken_false_tokenExpired() {
        given(jwtService.isTokenExpired(token)).willReturn(true);

        assertFalse(jwtService.isValidToken(token, userDetails));
    }
}

