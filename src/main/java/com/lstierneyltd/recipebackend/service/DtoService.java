package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.dto.RecipeDto;
import com.lstierneyltd.recipebackend.dto.RecipePreviewDto;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;

import java.util.List;

public interface DtoService {
    RecipeDto recipeToRecipeDto(Recipe recipe);

    List<RecipeDto> recipesToRecipeDtos(List<Recipe> recipes);

    RecipePreviewDto recipePreviewToRecipePreviewDto(RecipeRepository.RecipePreview recipePreview);

    List<RecipePreviewDto> recipePreviewsToRecipePreviewDtos(List<RecipeRepository.RecipePreview> recipePreviews);
}
