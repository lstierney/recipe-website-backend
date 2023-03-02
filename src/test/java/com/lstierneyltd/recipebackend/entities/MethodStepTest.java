package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.stubs.TestStubs.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MethodStepTest {
    private MethodStep methodStep;

    @BeforeEach
    void init() {
        methodStep = new MethodStep();
    }

    @Test
    public void testConstructor() {
        assertThat(METHOD_STEP.getId(), equalTo(ID));
        assertThat(METHOD_STEP.getDescription(), equalTo(DESCRIPTION));
        assertThat(METHOD_STEP.getRecipe(), equalTo(RECIPE_1));
        assertThat(METHOD_STEP.getOrdering(), equalTo(ORDERING));
    }

    @Test
    public void testSetGetId() {
        methodStep.setId(ID);
        assertThat(methodStep.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetRecipe() {
        methodStep.setRecipe(RECIPE_1);
        assertThat(methodStep.getRecipe(), equalTo(RECIPE_1));
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
