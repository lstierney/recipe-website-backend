package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.stubs.TestStubs.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class IngredientTest {
    private Ingredient ingredient;

    @BeforeEach
    void init() {
        ingredient = new Ingredient();
    }

    @Test
    public void testConstructor() {
        assertThat(INGREDIENT.getId(), equalTo(ID));
        assertThat(INGREDIENT.getRecipe(), equalTo(RECIPE));
        assertThat(INGREDIENT.getUnit(), equalTo(UNIT_1));
        assertThat(INGREDIENT.getDescription(), equalTo(DESCRIPTION));
        assertThat(INGREDIENT.getQuantity(), equalTo(QUANTITY));
    }

    @Test
    public void testSetGetId() {
        ingredient.setId(ID);
        assertThat(ingredient.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetRecipe() {
        ingredient.setRecipe(RECIPE);
        assertThat(ingredient.getRecipe(), equalTo(RECIPE));
    }

    @Test
    public void testSetGetUnit() {
        ingredient.setUnit(UNIT_1);
        assertThat(ingredient.getUnit(), equalTo(UNIT_1));
    }

    @Test
    public void testSetGetDescription() {
        ingredient.setDescription(DESCRIPTION);
        assertThat(ingredient.getDescription(), equalTo(DESCRIPTION));
    }

    @Test
    public void testSetGetQuantity() {
        ingredient.setQuantity(QUANTITY);
        assertThat(ingredient.getQuantity(), equalTo(QUANTITY));
    }
}
