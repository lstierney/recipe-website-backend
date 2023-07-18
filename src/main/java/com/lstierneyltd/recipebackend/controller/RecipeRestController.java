package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeRestController {
    Recipe getRecipeById(Integer id);

    List<Recipe> getAllRecipes();

    Recipe addRecipe(MultipartFile imageFile, String recipe);

    Recipe updateRecipe(MultipartFile imageFile, String recipe);

    List<RecipeRepository.RecipePreview> getRecipesPreviewList();

    List<Recipe> getRecipesByTagName(String tagName);

    RecipeRepository.RecipePreview getLatestRecipe();

    RecipeRepository.RecipePreview getRandomRecipe();
}
