package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeRestController {
    Recipe getRecipeById(Integer id);

    Recipe getRecipeByName(String name);

    List<Recipe> getAllRecipes();

    Recipe addRecipe(MultipartFile imageFile, String recipe);

    Recipe updateRecipe(MultipartFile imageFile, String recipe);

    List<RecipeRepository.RecipePreview> getRecipesPreviewList();

    List<Recipe> getRecipesByTags(List<String> tags);

    List<RecipeRepository.RecipePreview> getLatestRecipes();

    List<RecipeRepository.RecipePreview> getRandomRecipes();

    Recipe markRecipeAsCooked(Integer id);
}
