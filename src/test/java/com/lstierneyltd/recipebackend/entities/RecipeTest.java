package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static com.lstierneyltd.recipebackend.utils.TestStubs.*;
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
    public void testGetSetId() {
        recipe.setId(ID);
        assertThat(recipe.getId(), equalTo(ID));
    }

    @Test
    public void testGetSetName() {
        recipe.setName(RECIPE_1_NAME);
        assertThat(recipe.getName(), equalTo(RECIPE_1_NAME));
    }

    @Test
    public void testGetSetDescription() {
        recipe.setDescription(RECIPE_1_DESCRIPTION);
        assertThat(recipe.getDescription(), equalTo(RECIPE_1_DESCRIPTION));
    }

    @Test
    public void testGetSetCookingTime() {
        recipe.setCookingTime(COOKING_TIME);
        assertThat(recipe.getCookingTime(), equalTo(COOKING_TIME));
    }

    @Test
    public void testGetSetIngredients() {
        List<Ingredient> ingredients = List.of(getIngredient());
        recipe.setIngredients(ingredients);
        assertThat(recipe.getIngredients(), equalTo(ingredients));

        // Check to see that the recipe was set in the Ingredient too
        assertThat(recipe.getIngredients().get(0).getRecipe(), sameInstance(recipe));
    }

    @Test
    public void testGetSetMethodSteps() {
        List<MethodStep> methodSteps = List.of(getMethodStep());
        recipe.setMethodSteps(methodSteps);
        assertThat(recipe.getMethodSteps(), equalTo(methodSteps));

        // Check to see that the recipe was set in the MethodStep too
        assertThat(recipe.getMethodSteps().get(0).getRecipe(), sameInstance(recipe));
    }

    @Test
    public void testGetSetTags() {
        List<Tag> tags = List.of(getTag());
        recipe.setTags(tags);
        assertThat(recipe.getTags(), equalTo(tags));
    }
}
