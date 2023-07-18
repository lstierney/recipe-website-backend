package com.lstierneyltd.recipebackend.entities;

import com.lstierneyltd.recipebackend.annotation.Generated;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;

public class RecipePreviewImpl implements RecipeRepository.RecipePreview {
    private int id;
    private String name;
    private String description;
    private String imageFileName;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Generated
    @Override
    public String toString() {
        return "RecipePreviewImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageFileName='" + imageFileName + '\'' +
                '}';
    }
}
