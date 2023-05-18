package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.TOKEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AuthenticationResponseTest {
    private AuthenticationResponse response;

    @BeforeEach
    void init() {
        response = new AuthenticationResponse();
    }

    @Test
    public void testSetGetToken() {
        response.setToken(TOKEN);
        assertThat(response.getToken(), equalTo(TOKEN));
    }
}
