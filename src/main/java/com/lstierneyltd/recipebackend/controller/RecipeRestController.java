package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;

import java.util.List;

public interface RecipeRestController {
    Recipe getRecipeById(Integer id);

    List<Recipe> getAllRecipes();

    Recipe newRecipe(Recipe recipe);

    List<RecipeRepository.RecipeIdAndName> getRecipesList();
}
