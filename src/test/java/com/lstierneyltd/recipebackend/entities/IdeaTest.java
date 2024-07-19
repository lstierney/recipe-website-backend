package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IdeaTest {
    private Idea idea;

    @BeforeEach
    void before() {
        idea = new Idea();
    }

    @Test
    public void testGetSetId() {
        idea.setId(IDEA_ID);
        assertThat(idea.getId(), is(IDEA_ID));
    }

    @Test
    public void testGetSetName() {
        idea.setName(IDEA_NAME);
        assertThat(idea.getName(), is(IDEA_NAME));
    }

    @Test
    public void testGetSetUrl() {
        idea.setUrl(IDEA_URL);
        assertThat(idea.getUrl(), is(IDEA_URL));
    }
}
