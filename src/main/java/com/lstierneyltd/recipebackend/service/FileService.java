package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void createImageFolder(Recipe recipe);

    void addImageToRecipe(MultipartFile imageFile, Recipe recipe);

    List<String> getFilesInDirectory(String imageFolderPath);
}
