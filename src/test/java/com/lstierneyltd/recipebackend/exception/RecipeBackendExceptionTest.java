package com.lstierneyltd.recipebackend.exception;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RecipeBackendExceptionTest {
    @Test
    public void testConstructorWithMessge() {
        String msg = "The exception message";
        RecipeBackendException recipeBackendException = new RecipeBackendException(msg);
        assertThat(recipeBackendException.getMessage(), equalTo(msg));
    }
}
