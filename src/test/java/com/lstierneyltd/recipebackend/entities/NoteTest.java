package com.lstierneyltd.recipebackend.entities;

import com.lstierneyltd.recipebackend.utils.TestStubs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class NoteTest {
    private Note note;

    @BeforeEach
    void init() {
        note = new Note();
    }

    @Test
    public void testSetGetId() {
        note.setId(ID);
        assertThat(note.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetRecipe() {
        final Recipe recipe = TestStubs.getRecipe();

        note.setRecipe(recipe);
        assertThat(note.getRecipe(), equalTo(recipe));
    }

    @Test
    public void testSetGetDescription() {
        note.setDescription(DESCRIPTION);
        assertThat(note.getDescription(), equalTo(DESCRIPTION));
    }

    @Test
    public void testSetGetOrdering() {
        note.setOrdering(ORDERING);
        assertThat(note.getOrdering(), equalTo(ORDERING));
    }
}
