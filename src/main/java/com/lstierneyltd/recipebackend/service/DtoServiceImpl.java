package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.dto.RecipeDto;
import com.lstierneyltd.recipebackend.dto.RecipePreviewDto;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DtoServiceImpl implements DtoService {
    private final FileService fileService;
    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    public DtoServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public RecipeDto recipeToRecipeDto(Recipe recipe) {
        String imageFolderPath = fileUploadDirectory + "/images/" + recipe.getImageFolderName() + "/";
        List<String> imageFileNames = fileService.getFilesInDirectory(imageFolderPath);

        return new RecipeDto.Builder()
                .withId(recipe.getId())
                .withName(recipe.getName())
                .withDescription(recipe.getDescription())
                .withCooked(recipe.getCooked())
                .withLastCooked(recipe.getLastCooked())
                .withCookingTime(recipe.getCookingTime())
                .withBasedOn(recipe.getBasedOn())
                .withMethodSteps(recipe.getMethodSteps())
                .withIngredients(recipe.getIngredients())
                .withNotes(recipe.getNotes())
                .withTags(recipe.getTags())
                .withServedOn(recipe.getServedOn())
                .withDeleted(recipe.isDeleted())
                .withImageFolderPath("/images/" + recipe.getImageFolderName() + "/")
                .withImageFileNames(imageFileNames)
                .withCreatedDate(recipe.getCreatedDate())
                .withCreatedBy(recipe.getCreatedBy())
                .withLastUpdatedDate(recipe.getLastUpdatedDate())
                .withLastUpdatedBy(recipe.getLastUpdatedBy())
                .build();
    }

    @Override
    public List<RecipeDto> recipesToRecipeDtos(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::recipeToRecipeDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecipePreviewDto recipePreviewToRecipePreviewDto(RecipeRepository.RecipePreview recipePreview) {
        String imageFolderName = recipePreview.getId() + " " + "- " + recipePreview.getName(); // TODO - this is here AND in Recipe
        String imageFolderPath = fileUploadDirectory + "images/" + imageFolderName + "/";

        return new RecipePreviewDto.Builder()
                .withId(recipePreview.getId())
                .withCooked(recipePreview.getCooked())
                .withDescription(recipePreview.getDescription())
                .withName(recipePreview.getName())
                .withLastCooked(recipePreview.getLastCooked())
                .withImageFolderPath("/images/" + imageFolderName + "/")
                .withImageFileNames(fileService.getFilesInDirectory(imageFolderPath))
                .build();
    }

    @Override
    public List<RecipePreviewDto> recipePreviewsToRecipePreviewDtos(List<RecipeRepository.RecipePreview> recipePreviews) {
        return recipePreviews.stream()
                .map(this::recipePreviewToRecipePreviewDto)
                .collect(Collectors.toList());
    }
}
