package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.lstierneyltd.recipebackend.stubs.TestStubs.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;

public class RecipeTest {
    private Recipe recipe;

    @BeforeEach
    void init() {
        recipe = new Recipe();
    }

    @Test
    public void testConstructor1() {
        assertThat(RECIPE_2.getId(), equalTo(ID));
        assertThat(RECIPE_2.getName(), equalTo(NAME));
        assertThat(RECIPE_2.getDescription(), equalTo(DESCRIPTION));
        assertThat(RECIPE_2.getCookingTime(), equalTo(COOKING_TIME));
    }

    @Test
    public void testConstructor2() {
        assertThat(RECIPE_3.getId(), equalTo(ID));
        assertThat(RECIPE_3.getName(), equalTo(NAME));
        assertThat(RECIPE_3.getDescription(), equalTo(DESCRIPTION));
        assertThat(RECIPE_3.getCookingTime(), equalTo(COOKING_TIME));
        assertThat(RECIPE_3.getCookingTime(), equalTo(COOKING_TIME));
        assertThat(RECIPE_3.getMethodSteps().get(0), equalTo(METHOD_STEP));
        assertThat(RECIPE_3.getIngredients().get(0), equalTo(INGREDIENT));
    }

    @Test
    public void testGetSetId() {
        recipe.setId(ID);
        assertThat(recipe.getId(), equalTo(ID));
    }

    @Test
    public void testGetSetName() {
        recipe.setName(NAME);
        assertThat(recipe.getName(), equalTo(NAME));
    }

    @Test
    public void testGetSetDescription() {
        recipe.setDescription(DESCRIPTION);
        assertThat(recipe.getDescription(), equalTo(DESCRIPTION));
    }

    @Test
    public void testGetSetCookingTime() {
        recipe.setCookingTime(COOKING_TIME);
        assertThat(recipe.getCookingTime(), equalTo(COOKING_TIME));
    }

    @Test
    public void testGetSetIngredients() {
        List<Ingredient> ingredients = List.of(INGREDIENT);
        recipe.setIngredients(ingredients);
        assertThat(recipe.getIngredients(), equalTo(ingredients));

        // Check to see that the recipe was set in the Ingredient too
        assertThat(recipe.getIngredients().get(0).getRecipe(), sameInstance(recipe));
    }

    @Test
    public void testGetSetMethodSteps() {
        List<MethodStep> methodSteps = List.of(METHOD_STEP);
        recipe.setMethodSteps(methodSteps);
        assertThat(recipe.getMethodSteps(), equalTo(methodSteps));

        // Check to see that the recipe was set in the Ingredient too
        assertThat(recipe.getMethodSteps().get(0).getRecipe(), sameInstance(recipe));
    }
}
