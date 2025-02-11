package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.dto.RecipeDto;
import com.lstierneyltd.recipebackend.dto.RecipePreviewDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    RecipeDto addRecipe(MultipartFile imageFile, String recipeString);

    RecipeDto updateRecipe(MultipartFile imageFile, String recipeString);

    RecipeDto findById(int id);

    RecipeDto findByName(String name);

    List<RecipeDto> findByTagNames(List<String> tagNames);

    List<RecipeDto> findAllActiveRecipes();

    List<RecipeDto> findAll();

    List<RecipePreviewDto> findAllActiveRecipePreview();

    List<RecipePreviewDto> findLatestPreviews();

    List<RecipePreviewDto> findRandomDinnersPreviews();

    RecipePreviewDto findRandomDinnerPreview();

    RecipeDto markAsCooked(Integer id);

    RecipeDto markAsDeleted(Integer id);

    RecipeDto restore(Integer id);
}

