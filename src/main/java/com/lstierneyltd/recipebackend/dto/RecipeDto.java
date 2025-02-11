package com.lstierneyltd.recipebackend.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.lstierneyltd.recipebackend.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonDeserialize(builder = RecipeDto.Builder.class)
public class RecipeDto {
    private int id;
    private String name;
    private String description;
    private int cooked;
    private LocalDateTime lastCooked;
    private int cookingTime;
    private String basedOn;
    private List<MethodStep> methodSteps = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private Set<Tag> tags = new HashSet<>();
    private ServedOn servedOn;
    private boolean deleted;
    private String imageFolderPath;
    private List<String> imageFileNames;
    private LocalDateTime lastUpdatedDate;
    private LocalDateTime createdDate;
    private String createdBy;
    private String lastUpdatedBy;

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

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(String basedOn) {
        this.basedOn = basedOn;
    }

    public List<MethodStep> getMethodSteps() {
        return methodSteps;
    }

    public void setMethodSteps(List<MethodStep> methodSteps) {
        this.methodSteps = methodSteps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public ServedOn getServedOn() {
        return servedOn;
    }

    public void setServedOn(ServedOn servedOn) {
        this.servedOn = servedOn;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public String toString() {
        return "RecipeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cooked=" + cooked +
                ", lastCooked=" + lastCooked +
                ", cookingTime=" + cookingTime +
                ", basedOn='" + basedOn + '\'' +
                ", methodSteps=" + methodSteps +
                ", ingredients=" + ingredients +
                ", notes=" + notes +
                ", tags=" + tags +
                ", servedOn=" + servedOn +
                ", deleted=" + deleted +
                ", imageFolderPath='" + imageFolderPath + '\'' +
                ", imageFileNames=" + imageFileNames +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }

    // Builder class
    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private RecipeDto instance = new RecipeDto();

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

        public Builder withCookingTime(int cookingTime) {
            instance.cookingTime = cookingTime;
            return this;
        }

        public Builder withBasedOn(String basedOn) {
            instance.basedOn = basedOn;
            return this;
        }

        public Builder withMethodSteps(List<MethodStep> methodSteps) {
            instance.methodSteps = methodSteps;
            return this;
        }

        public Builder withIngredients(List<Ingredient> ingredients) {
            instance.ingredients = ingredients;
            return this;
        }

        public Builder withNotes(List<Note> notes) {
            instance.notes = notes;
            return this;
        }

        public Builder withTags(Set<Tag> tags) {
            instance.tags = tags;
            return this;
        }

        public Builder withServedOn(ServedOn servedOn) {
            instance.servedOn = servedOn;
            return this;
        }

        public Builder withDeleted(boolean del) {
            instance.deleted = del;
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

        public Builder withCreatedDate(LocalDateTime createdDate) {
            instance.createdDate = createdDate;
            return this;
        }

        public Builder withLastUpdatedDate(LocalDateTime lastUpdatedDate) {
            instance.lastUpdatedDate = lastUpdatedDate;
            return this;
        }

        public Builder withLastUpdatedBy(String lastUpdatedBy) {
            instance.lastUpdatedBy = lastUpdatedBy;
            return this;
        }

        public Builder withCreatedBy(String createdBy) {
            instance.createdBy = createdBy;
            return this;
        }

        public RecipeDto build() {
            return instance;
        }
    }
}
