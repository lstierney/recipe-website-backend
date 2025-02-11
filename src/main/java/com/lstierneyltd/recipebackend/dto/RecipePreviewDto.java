package com.lstierneyltd.recipebackend.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;
import java.util.List;

@JsonDeserialize(builder = RecipePreviewDto.Builder.class)
public class RecipePreviewDto {

    private int id;
    private String name;
    private String description;
    private int cooked;
    private LocalDateTime lastCooked;
    private String imageFolderPath;
    private List<String> imageFileNames;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCooked() {
        return cooked;
    }

    public void setCooked(int cooked) {
        this.cooked = cooked;
    }

    public LocalDateTime getLastCooked() {
        return lastCooked;
    }

    public void setLastCooked(LocalDateTime lastCooked) {
        this.lastCooked = lastCooked;
    }

    public String getImageFolderPath() {
        return imageFolderPath;
    }

    public void setImageFolderPath(String imageFolderPath) {
        this.imageFolderPath = imageFolderPath;
    }

    public List<String> getImageFileNames() {
        return imageFileNames;
    }

    public void setImageFileNames(List<String> imageFileNames) {
        this.imageFileNames = imageFileNames;
    }

    @Override
    public String toString() {
        return "RecipePreviewDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cooked=" + cooked +
                ", lastCooked=" + lastCooked +
                ", imageFolderPath='" + imageFolderPath + '\'' +
                ", imageFileNames=" + imageFileNames +
                '}';
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private RecipePreviewDto instance = new RecipePreviewDto();

        public Builder withId(int id) {
            instance.id = id;
            return this;
        }

        public Builder withName(String name) {
            instance.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            instance.description = description;
            return this;
        }

        public Builder withCooked(int cooked) {
            instance.cooked = cooked;
            return this;
        }

        public Builder withLastCooked(LocalDateTime lastCooked) {
            instance.lastCooked = lastCooked;
            return this;
        }

        public Builder withImageFolderPath(String path) {
            instance.imageFolderPath = path;
            return this;
        }

        public Builder withImageFileNames(List<String> names) {
            instance.imageFileNames = names;
            return this;
        }

        public RecipePreviewDto build() {
            return instance;
        }
    }
}