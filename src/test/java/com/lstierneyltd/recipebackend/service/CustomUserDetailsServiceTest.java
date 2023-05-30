package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.User;
import com.lstierneyltd.recipebackend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.lstierneyltd.recipebackend.service.CustomUserDetailsService.USER_NOT_FOUND_WITH_USERNAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private User user;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void before() {
        user = new User();
        user.setPassword(PASSWORD);
        user.setUsername(USERNAME);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testLoadUserByUsername_userFound() {
        // Given
        given(userRepository.findByUsername(USERNAME)).willReturn(user);

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);

        // Then
        then(userRepository).should().findByUsername(USERNAME);
        assertThat(userDetails.getUsername(), is(USERNAME));
    }

    @Test
    public void testLoadUserByUsername_userNotFound_throwsException() {
        // Given
        given(userRepository.findByUsername(USERNAME)).willReturn(null);

        // When
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(USERNAME);
        });

        // Then
        then(userRepository).should().findByUsername(USERNAME);
        assertThat(exception.getMessage(), equalTo(USER_NOT_FOUND_WITH_USERNAME + USERNAME));
    }


}
