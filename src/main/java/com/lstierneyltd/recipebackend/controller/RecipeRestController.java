package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeRestController {
    Recipe getRecipeById(Integer id);

    List<Recipe> getAllRecipes();

    Recipe addRecipe(MultipartFile imageFile, String recipe);

    List<RecipeRepository.RecipeIdAndName> getRecipesList();

    List<Recipe> getRecipesByTagName(String tagName);
}
