package com.lstierneyltd.recipebackend.dto;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.utils.TestStubs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeDtoTest {
    private final Recipe recipe = TestStubs.getRecipe();
    private RecipeDto recipeDto;

    @BeforeEach
    void before() {

        RecipeDto.Builder builder = new RecipeDto.Builder();
        recipeDto = builder
                .withId(recipe.getId())
                .withName(recipe.getName())
                .withDescription(recipe.getDescription())
                .withCooked(recipe.getCooked())
                .withLastCooked(recipe.getLastCooked())
                .withCookingTime(recipe.getCookingTime())
                .withBasedOn(recipe.getBasedOn())
                .withMethodSteps(recipe.getMethodSteps())
                .withIngredients(recipe.getIngredients())
                .withNotes(recipe.getNotes())
                .withTags(recipe.getTags())
                .withServedOn(recipe.getServedOn())
                .withDeleted(recipe.isDeleted())
                .withImageFolderPath("images/")
                .withImageFileNames(Arrays.asList("image1.jpg", "image2.jpg"))
                .build();
    }

    @Test
    public void testIdSet() {
        assertEquals(recipeDto.getId(), recipe.getId());
    }

}
