package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Recipe;

import java.util.List;

public interface RecipeRestController {
    Recipe getRecipeById(Integer id);

    List<Recipe> getAllRecipes();

    Recipe newRecipe(Recipe recipe);
}
