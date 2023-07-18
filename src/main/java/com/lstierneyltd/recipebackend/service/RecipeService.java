package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    Recipe addRecipe(MultipartFile imageFile, String recipeString);

    Recipe findById(int id);

    List<Recipe> findByTagName(String tagName);

    List<Recipe> findAll();

    List<RecipeRepository.RecipePreview> findAllRecipePreviewBy();

    Recipe findLatest();
}
