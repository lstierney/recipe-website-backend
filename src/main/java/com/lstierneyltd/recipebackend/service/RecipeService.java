package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    Recipe addRecipe(MultipartFile imageFile, String recipeString);

    Recipe findById(int id);

    Recipe findByName(String name);

    List<Recipe> findByTagName(String tagName);

    List<Recipe> findAll();

    List<RecipeRepository.RecipePreview> findAllRecipePreviewBy();

    List<RecipeRepository.RecipePreview> findLatest();

    RecipeRepository.RecipePreview findRandom();

    Recipe markAsCooked(Integer id);
}

