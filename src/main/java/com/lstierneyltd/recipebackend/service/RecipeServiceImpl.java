package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.dto.RecipeDto;
import com.lstierneyltd.recipebackend.dto.RecipePreviewDto;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    public static final String COULD_NOT_FIND_RECIPE_WITH_ID = "Could not find RECIPE with id: ";
    public static final String COULD_NOT_FIND_RECIPE_WITH_NAME = "Could not find RECIPE with name: ";
    private final Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);
    private final FileService fileService;
    private final ObjectMapperService objectMapperService;
    private final RecipeRepository recipeRepository;
    private final UserService userService;
    private final DtoService dtoService;

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    public RecipeServiceImpl(FileService fileService, ObjectMapperService objectMapperService, RecipeRepository recipeRepository, UserService userService, DtoService dtoService) {
        this.fileService = fileService;
        this.objectMapperService = objectMapperService;
        this.recipeRepository = recipeRepository;
        this.userService = userService;
        this.dtoService = dtoService;
    }

    @Override
    public RecipeDto addRecipe(MultipartFile imageFile, String recipeString) {
        final Recipe transientRecipe = objectMapperService.jsonStringToObject(recipeString, Recipe.class);
        transientRecipe.markAsCreated(userService.getLoggedInUsername());

        logger.info("Adding new recipe: " + transientRecipe);

        final Recipe persistentRecipe = recipeRepository.save(transientRecipe);

        handleUploadedFile(imageFile, persistentRecipe);

        logger.info("New recipe added. Id: " + persistentRecipe.getId());

        return dtoService.recipeToRecipeDto(persistentRecipe);
    }

    @Override
    public RecipeDto updateRecipe(MultipartFile imageFile, String recipeString) {
        final Recipe submittedRecipe = objectMapperService.jsonStringToObject(recipeString, Recipe.class);

        logger.info("Updating Recipe: " + submittedRecipe.getId() + ": " + submittedRecipe.getName());

        final Optional<Recipe> existingRecipeOptional = recipeRepository.findById(submittedRecipe.getId());
        if (existingRecipeOptional.isPresent()) {
            Recipe existingRecipe = existingRecipeOptional.get();
            existingRecipe.markAsUpdated(userService.getLoggedInUsername()); // We do this manually as @PreUpdate doesn't work for nested collections
            existingRecipe.setName(submittedRecipe.getName());
            existingRecipe.setDescription(submittedRecipe.getDescription());
            existingRecipe.setCookingTime(submittedRecipe.getCookingTime());
            existingRecipe.setBasedOn(submittedRecipe.getBasedOn());

            existingRecipe.getMethodSteps().clear();
            existingRecipe.getMethodSteps().addAll(submittedRecipe.getMethodSteps());

            existingRecipe.getIngredients().clear();
            existingRecipe.getIngredients().addAll(submittedRecipe.getIngredients());

            existingRecipe.getNotes().clear();
            existingRecipe.getNotes().addAll(submittedRecipe.getNotes());

            existingRecipe.getTags().clear();
            existingRecipe.getTags().addAll(submittedRecipe.getTags());

            existingRecipe.setServedOn(submittedRecipe.getServedOn());

            if (imageFile != null) {
                handleUploadedFile(imageFile, existingRecipe);
            }

            recipeRepository.save(existingRecipe);

            logger.info("Successfully updated Recipe: " + existingRecipe.getId() + ": " + existingRecipe.getName());

            return dtoService.recipeToRecipeDto(existingRecipe);
        } else {
            logger.warn("Could not find Recipe with Id: " + submittedRecipe.getId() + " to update");
            return null;
        }
    }

    private void handleUploadedFile(MultipartFile imageFile, Recipe recipe) {
        fileService.createImageFolder(recipe);
        fileService.addImageToRecipe(imageFile, recipe);
    }

    @Override
    public RecipeDto findById(int id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(COULD_NOT_FIND_RECIPE_WITH_ID + id));
        return dtoService.recipeToRecipeDto(recipe);
    }

    @Override
    public RecipeDto findByName(String name) {
        String formattedName = getFormattedRecipeName(name);
        return dtoService.recipeToRecipeDto(recipeRepository.findActiveByName(formattedName).orElseThrow(() -> new NoSuchElementException(COULD_NOT_FIND_RECIPE_WITH_NAME + formattedName)));
    }

    private String getFormattedRecipeName(String name) {
        return name.replace('-', ' ').toLowerCase();
    }

    @Override
    public List<RecipeDto> findByTagNames(List<String> tagNames) {
        return dtoService.recipesToRecipeDtos(recipeRepository.findByAllActiveRecipesByTagNames(tagNames, (long) tagNames.size()));
    }

    @Override
    public List<RecipeDto> findAllActiveRecipes() {
        return dtoService.recipesToRecipeDtos(recipeRepository.findActiveRecipes());
    }

    @Override
    public List<RecipeDto> findAll() {
        return dtoService.recipesToRecipeDtos(recipeRepository.findAll());
    }


    @Override
    public List<RecipePreviewDto> findAllActiveRecipePreview() {
        return dtoService.recipePreviewsToRecipePreviewDtos(recipeRepository.findAllActiveRecipePreviews());
    }

    @Override
    public List<RecipePreviewDto> findLatestPreviews() {
        return dtoService.recipePreviewsToRecipePreviewDtos(recipeRepository.findSixLatestActiveDinnerPreviews());
    }

    @Override
    public List<RecipePreviewDto> findRandomDinnersPreviews() {
        List<RecipeRepository.RecipePreview> sixRandomActiveDinners = recipeRepository.findSixRandomActiveDinners();
        List<RecipePreviewDto> recipePreviewDtos = dtoService.recipePreviewsToRecipePreviewDtos(sixRandomActiveDinners);
        return recipePreviewDtos;
    }

    @Override
    public RecipePreviewDto findRandomDinnerPreview() {
        return dtoService.recipePreviewToRecipePreviewDto(recipeRepository.findRandomActiveDinnerPreview());
    }

    @Override
    public RecipeDto markAsCooked(Integer id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(COULD_NOT_FIND_RECIPE_WITH_ID + id));
        recipe.markedAsCooked();
        recipeRepository.save(recipe);
        return dtoService.recipeToRecipeDto(recipe);
    }

    @Override
    public RecipeDto markAsDeleted(Integer id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(COULD_NOT_FIND_RECIPE_WITH_ID + id));
        recipe.setDeleted(true);
        recipe.markAsUpdated(userService.getLoggedInUsername());
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return dtoService.recipeToRecipeDto(updatedRecipe);
    }

    @Override
    public RecipeDto restore(Integer id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(COULD_NOT_FIND_RECIPE_WITH_ID + id));
        recipe.setDeleted(false);
        recipe.markAsUpdated(userService.getLoggedInUsername());
        recipeRepository.save(recipe);
        return dtoService.recipeToRecipeDto(recipe);
    }
}

