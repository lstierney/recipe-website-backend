package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.DESCRIPTION;
import static com.lstierneyltd.recipebackend.utils.TestConstants.ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CrockeryTest {
    private Crockery crockery;

    @BeforeEach
    void init() {
        crockery = new Crockery();
    }

    @Test
    public void testSetGetId() {
        crockery.setId(ID);
        assertThat(crockery.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetDescription() {
        crockery.setDescription(DESCRIPTION);
        assertThat(crockery.getDescription(), equalTo(DESCRIPTION));
    }
}
