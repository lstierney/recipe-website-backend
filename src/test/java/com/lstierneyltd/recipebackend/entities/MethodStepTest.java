package com.lstierneyltd.recipebackend.entities;

import com.lstierneyltd.recipebackend.utils.TestStubs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MethodStepTest {
    private MethodStep methodStep;

    @BeforeEach
    void init() {
        methodStep = new MethodStep();
    }

    @Test
    public void testSetGetId() {
        methodStep.setId(ID);
        assertThat(methodStep.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetRecipe() {
        final Recipe recipe = TestStubs.getRecipe();

        methodStep.setRecipe(recipe);
        assertThat(methodStep.getRecipe(), equalTo(recipe));
    }

    @Test
    public void testSetGetDescription() {
        methodStep.setDescription(DESCRIPTION);
        assertThat(methodStep.getDescription(), equalTo(DESCRIPTION));
    }

    @Test
    public void testSetGetOrdering() {
        methodStep.setOrdering(ORDERING);
        assertThat(methodStep.getOrdering(), equalTo(ORDERING));
    }
}
