package com.lstierneyltd.recipebackend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static com.lstierneyltd.recipebackend.utils.TestStubs.*;
import static com.lstierneyltd.recipebackend.utils.TestUtils.areWithinSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testGetSetBasedOn() {
        recipe.setBasedOn(BASED_ON);
        assertThat(recipe.getBasedOn(), equalTo(BASED_ON));
    }

    @Test
    public void testGetSetServedOn() {
        ServedOn servedOn = new ServedOn();
        recipe.setServedOn(servedOn);
        assertThat(recipe.getServedOn(), equalTo(servedOn));
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
    public void testGetSetNotes() {
        List<Note> notes = List.of(getNote());
        recipe.setNotes(notes);
        assertThat(recipe.getNotes(), equalTo(notes));

        // Check to see that the recipe was set in the MethodStep too
        assertThat(recipe.getNotes().get(0).getRecipe(), sameInstance(recipe));
    }

    @Test
    public void testGetSetTags() {
        Set<Tag> tags = Set.of(getTag());
        recipe.setTags(tags);
        assertThat(recipe.getTags(), equalTo(tags));
    }

    @Test
    public void testGetSetCooked() {
        recipe.setCooked(COOKED);
        assertThat(recipe.getCooked(), equalTo(COOKED));
    }

    @Test
    public void testGetSetLastCooked() {
        recipe.setLastCooked(LAST_COOKED);
        assertThat(recipe.getLastCooked(), equalTo(LAST_COOKED));
    }

    @Test
    public void testMarkAsCooked() {
        LocalDateTime now = LocalDateTime.now();
        recipe.setCooked(COOKED);

        recipe.markedAsCooked();
        long secondsBetween = ChronoUnit.SECONDS.between(now, recipe.getLastCooked());
        assertThat(recipe.getCooked(), equalTo(COOKED + 1));
        assertThat(secondsBetween, lessThanOrEqualTo(10L));
    }

    @Test
    public void testGetSetCreatedBy() {
        recipe.setCreatedBy(CREATED_BY);
        assertThat(recipe.getCreatedBy(), equalTo(CREATED_BY));
    }

    @Test
    public void testGetSetLastUpdatedBy() {
        recipe.setLastUpdatedBy(LAST_UPDATED_BY);
        assertThat(recipe.getLastUpdatedBy(), equalTo(LAST_UPDATED_BY));
    }

    @Test
    public void testGetSetLastUpdatedDate() {
        recipe.setLastUpdatedDate(LAST_UPDATED_DATE);
        assertThat(recipe.getLastUpdatedDate(), equalTo(LAST_UPDATED_DATE));
    }

    @Test
    public void testGetSetCreatedDate() {
        recipe.setCreatedDate(CREATED_DATE);
        assertThat(recipe.getCreatedDate(), equalTo(CREATED_DATE));
    }

    @Test
    public void testMarkAsCreated() {
        recipe.markAsCreated(CREATED_BY);

        assertThat(recipe.getCreatedBy(), is(CREATED_BY));
        assertTrue(areWithinSeconds(recipe.getCreatedDate(), LocalDateTime.now(), 10));
    }

    @Test
    public void testMarkAsUpdated() {
        recipe.markAsUpdated(LAST_UPDATED_BY);

        assertThat(recipe.getLastUpdatedBy(), is(LAST_UPDATED_BY));
        assertTrue(areWithinSeconds(recipe.getLastUpdatedDate(), LocalDateTime.now(), 10));
    }

    @Test
    public void testGetSetDeleted() {
        recipe.setDeleted(true);
        assertThat(recipe.isDeleted(), is(true));

        recipe.setDeleted(false);
        assertThat(recipe.isDeleted(), is(false));
    }
}
