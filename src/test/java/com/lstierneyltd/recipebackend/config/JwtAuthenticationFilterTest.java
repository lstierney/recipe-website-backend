package com.lstierneyltd.recipebackend.config;

import com.lstierneyltd.recipebackend.service.CustomUserDetailsService;
import com.lstierneyltd.recipebackend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String USERNAME = "username";

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(customUserDetailsService, jwtService, request,
                response, filterChain);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testDoFilterInternalWithValidJwtToken() throws ServletException, IOException {
        // Given
        String jwtToken = "validToken";
        UserDetails userDetails = mock(UserDetails.class);
        given(jwtService.isTokenExpired(jwtToken)).willReturn(false);
        given(jwtService.extractUsername(jwtToken)).willReturn(USERNAME);
        given(customUserDetailsService.loadUserByUsername(USERNAME)).willReturn(userDetails);
        given(request.getHeader(AUTHORIZATION)).willReturn(BEARER + jwtToken);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        then(jwtService).should().isTokenExpired(jwtToken);
        then(jwtService).should().extractUsername(jwtToken);
        then(customUserDetailsService).should().loadUserByUsername(USERNAME);
        then(filterChain).should().doFilter(request, response);

        Authentication resultAuthentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(resultAuthentication, isA(UsernamePasswordAuthenticationToken.class));
        assertThat(resultAuthentication.getPrincipal(), is(userDetails));
        assertThat(resultAuthentication.getCredentials(), is(nullValue()));
    }

    @Test
    public void testDoFilterInternalWithExpiredJwtToken() throws ServletException, IOException {
        // Given
        String jwtToken = "expiredToken";
        given(request.getHeader(AUTHORIZATION)).willReturn(BEARER + jwtToken);
        given(jwtService.isTokenExpired(jwtToken)).willReturn(true);
        given(response.getWriter()).willReturn(new PrintWriter(System.out));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verifyUnauthorisedResponse();
        then(jwtService).should().isTokenExpired(jwtToken);

        Authentication resultAuthentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(resultAuthentication, is(nullValue()));
    }

    @Test
    public void testDoFilterInternalWithInvalidJwtToken() throws ServletException, IOException {
        // Given
        String jwtToken = "invalidToken";
        given(request.getHeader(AUTHORIZATION)).willReturn(BEARER + jwtToken);
        given(jwtService.isTokenExpired(jwtToken)).willThrow(JwtException.class);
        given(response.getWriter()).willReturn(new PrintWriter(System.out));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verifyUnauthorisedResponse();
        then(jwtService).should().isTokenExpired(jwtToken);

        Authentication resultAuthentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(resultAuthentication, is(nullValue()));
    }

    @Test
    public void testDoFilterInternalWithNoJwtToken() throws ServletException, IOException {
        // Given
        given(request.getHeader(AUTHORIZATION)).willReturn(null);
        given(response.getWriter()).willReturn(new PrintWriter(System.out));
        given(response.getWriter()).willReturn(new PrintWriter(System.out));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verifyUnauthorisedResponse();
        verifyNoInteractions(jwtService, customUserDetailsService);

        Authentication resultAuthentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(resultAuthentication, is(nullValue()));
    }

    private void verifyUnauthorisedResponse() throws IOException {
        then(response).should().setContentType(MediaType.APPLICATION_JSON_VALUE);
        then(response).should().setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        then(response).should().getWriter();
        then(request).should().getRequestURL();
    }

    @Test
    public void testShouldNotFilterWithGetRequest() {
        // Given
        given(request.getMethod()).willReturn(HttpMethod.GET.name());

        // When
        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        // Then
        then(request).should().getMethod();
        then(request).should().getServletPath();

        assertThat(result, is(true));
    }

    @Test
    public void testShouldNotFilterWithOptionsRequest() {
        // Given
        given(request.getMethod()).willReturn(HttpMethod.OPTIONS.name());

        // When
        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        // Then
        then(request).should().getMethod();
        then(request).should().getServletPath();

        assertThat(result, is(true));
    }

    @Test
    public void testShouldNotFilterWithAuthenticateServletPath() {
        // Given
        given(request.getMethod()).willReturn(HttpMethod.POST.name());
        given(request.getServletPath()).willReturn("/authenticate");

        // When
        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        // Then
        then(request).should().getMethod();
        then(request).should().getServletPath();

        assertThat(result, is(true));
    }

    @Test
    public void testShouldNotFilterWithOtherRequest() {
        // Given
        given(request.getMethod()).willReturn("POST");
        given(request.getServletPath()).willReturn("/other");

        // When
        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        // Then
        then(request).should().getMethod();
        then(request).should().getServletPath();
        assertThat(result, is(false));
    }
}

