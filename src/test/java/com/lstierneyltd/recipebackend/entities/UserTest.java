package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserTest {
    private User user;

    @BeforeEach
    void init() {
        user = new User();
    }

    @Test
    public void testSetGetUsername() {
        user.setUsername(USER_NAME);
        assertThat(user.getUsername(), equalTo(USER_NAME));
    }

    @Test
    public void testSetGetId() {
        user.setId(ID);
        assertThat(user.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetPassword() {
        user.setPassword(PASSWORD);
        assertThat(user.getPassword(), equalTo(PASSWORD));
    }
}

