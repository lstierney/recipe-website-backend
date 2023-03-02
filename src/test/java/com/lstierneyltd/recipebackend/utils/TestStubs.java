package com.lstierneyltd.recipebackend.utils;

import com.lstierneyltd.recipebackend.entities.Ingredient;
import com.lstierneyltd.recipebackend.entities.MethodStep;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.Unit;

import java.util.List;

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

        ingredient.setRecipe(recipe);
        recipe.setIngredients(List.of(ingredient));

        return recipe;
    }

    private static Recipe getRecipeNoIngredient() {
        final Recipe recipe = new Recipe();
        recipe.setId(ID);
        recipe.setDescription(DESCRIPTION);
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

    public static MethodStep getMethodStep() {
        final MethodStep methodStep = new MethodStep();
        methodStep.setId(ID);
        methodStep.setOrdering(ORDERING);
        methodStep.setDescription(DESCRIPTION);
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
}
