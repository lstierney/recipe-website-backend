package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getRecipe;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getUnit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class IngredientTest {
    private Ingredient ingredient;

    @BeforeEach
    void init() {
        ingredient = new Ingredient();
    }

    @Test
    public void testSetGetId() {
        ingredient.setId(ID);
        assertThat(ingredient.getId(), equalTo(ID));
    }

    @Test
    public void testSetGetRecipe() {
        final Recipe recipe = getRecipe();
        ingredient.setRecipe(recipe);

        assertThat(ingredient.getRecipe(), equalTo(recipe));
    }

    @Test
    public void testSetGetUnit() {
        final Unit unit = getUnit();
        ingredient.setUnit(unit);

        assertThat(ingredient.getUnit(), equalTo(unit));
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
