package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.dto.RecipeDto;
import com.lstierneyltd.recipebackend.dto.RecipePreviewDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeRestController {
    RecipeDto getRecipeById(Integer id);

    RecipeDto getRecipeByName(String name);

    List<RecipeDto> getAllActiveRecipes();

    RecipeDto addRecipe(MultipartFile imageFile, String recipe);

    RecipeDto updateRecipe(MultipartFile imageFile, String recipe);

    List<RecipePreviewDto> getRecipesPreviewList();

    List<RecipeDto> getRecipesByTags(List<String> tags);

    List<RecipePreviewDto> getLatestRecipePreviews();

    List<RecipePreviewDto> getRandomDinnerPreviews();

    RecipePreviewDto getRandomDinnerPreview();

    RecipeDto markRecipeAsCooked(Integer id);

    RecipeDto markRecipeAsDeleted(Integer id);

    RecipeDto restore(Integer id);

    List<RecipeDto> getAllRecipesIgnoreDeleted();
}
