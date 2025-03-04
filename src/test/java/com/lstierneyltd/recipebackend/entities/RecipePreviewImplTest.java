package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RecipePreviewImplTest {
    private RecipePreviewImpl preview;

    @BeforeEach
    void init() {
        preview = new RecipePreviewImpl();
    }

    @Test
    public void testSetGetId() {
        preview.setId(ID);
        assertThat(preview.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetDescription() {
        preview.setDescription(DESCRIPTION);
        assertThat(preview.getDescription(), equalTo(DESCRIPTION));
    }

    @Test
    public void testSetGetName() {
        preview.setName(NAME);
        assertThat(preview.getName(), equalTo(NAME));
    }

    @Test
    public void testSetGetCooked() {
        preview.setCooked(COOKED);
        assertThat(preview.getCooked(), equalTo(COOKED));
    }

    @Test
    public void testSetGetLastCooked() {
        preview.setLastCooked(LAST_COOKED);
        assertThat(preview.getLastCooked(), equalTo(LAST_COOKED));
    }
}
