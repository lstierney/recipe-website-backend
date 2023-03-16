package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import org.springframework.web.multipart.MultipartFile;

public interface RecipeService {
    Recipe addRecipe(MultipartFile imageFile, String recipeString);
}
