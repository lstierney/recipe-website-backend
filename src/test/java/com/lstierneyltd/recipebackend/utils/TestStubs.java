package com.lstierneyltd.recipebackend.utils;

import com.lstierneyltd.recipebackend.entities.*;

import java.util.List;
import java.util.Set;

import static com.lstierneyltd.recipebackend.utils.TestConstants.*;

public final class TestStubs {
    private TestStubs() {
    }

    public static Unit getUnit() {
        final Unit unit = new Unit();
        unit.setId(UNIT_ID);
        unit.setName(NAME);
        unit.setAbbreviation(ABBREVIATION);
        return unit;
    }

    public static Recipe getRecipe() {
        final Recipe recipe = getRecipeNoIngredient();
        final Ingredient ingredient = getIngredientNoRecipe();
        final MethodStep methodStep = getMethodStepNoRecipe();

        ingredient.setRecipe(recipe);
        recipe.setIngredients(List.of(ingredient));

        methodStep.setRecipe(recipe);
        recipe.setMethodSteps(List.of(methodStep));

        recipe.setTags(Set.of(getTag()));

        return recipe;
    }

    public static RecipePreviewImpl getRecipePreview() {
        RecipePreviewImpl recipePreview = new RecipePreviewImpl();
        recipePreview.setId(ID);
        recipePreview.setDescription(DESCRIPTION);
        recipePreview.setName(NAME);
        recipePreview.setImageFileName(RECIPE_1_IMAGE_FILENAME);
        return recipePreview;
    }

    private static Recipe getRecipeNoIngredient() {
        final Recipe recipe = new Recipe();
        recipe.setId(ID);
        recipe.setName(RECIPE_1_NAME);
        recipe.setDescription(RECIPE_1_DESCRIPTION);
        recipe.setCookingTime(COOKING_TIME);
        return recipe;
    }

    private static Ingredient getIngredientNoRecipe() {
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(ID);
        ingredient.setQuantity(QUANTITY);
        ingredient.setUnit(getUnit());
        ingredient.setDescription(DESCRIPTION);
        return ingredient;
    }

    private static MethodStep getMethodStepNoRecipe() {
        final MethodStep methodStep = new MethodStep();
        methodStep.setId(ID);
        methodStep.setOrdering(ORDERING);
        methodStep.setDescription(DESCRIPTION);
        return methodStep;
    }

    private static Note getNoteNoRecipe() {
        final Note note = new Note();
        note.setId(ID);
        note.setOrdering(ORDERING);
        note.setDescription(DESCRIPTION);
        return note;
    }

    public static Note getNote() {
        final Note note = getNoteNoRecipe();
        note.setRecipe(getRecipe());
        return note;
    }

    public static MethodStep getMethodStep() {
        final MethodStep methodStep = getMethodStepNoRecipe();
        methodStep.setRecipe(getRecipe());
        return methodStep;
    }

    public static Ingredient getIngredient() {
        final Recipe recipe = getRecipeNoIngredient();
        final Ingredient ingredient = getIngredientNoRecipe();

        ingredient.setRecipe(recipe);
        recipe.setIngredients(List.of(ingredient));

        return ingredient;
    }

    public static Tag getTag() {
        final Tag tag = new Tag();
        tag.setId(TAG_ID);
        tag.setName(TAG_NAME);
        tag.setDescription(TAG_DESCRIPTION);
        return tag;
    }
}
