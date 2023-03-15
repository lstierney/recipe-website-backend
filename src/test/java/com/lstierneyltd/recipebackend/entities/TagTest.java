package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TagTest {
    private Tag tag;

    @BeforeEach
    void init() {
        tag = new Tag();
    }

    @Test
    public void testSetGetDescription() {
        tag.setDescription(DESCRIPTION);
        assertThat(tag.getDescription(), equalTo(DESCRIPTION));
    }

    @Test
    public void testSetGetId() {
        tag.setId(ID);
        assertThat(tag.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetName() {
        tag.setName(NAME);
        assertThat(tag.getName(), equalTo(NAME));
    }
}


