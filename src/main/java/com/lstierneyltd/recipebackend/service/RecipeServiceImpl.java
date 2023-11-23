package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    public RecipeServiceImpl(FileService fileService, ObjectMapperService objectMapperService, RecipeRepository recipeRepository, UserService userService) {
        this.fileService = fileService;
        this.objectMapperService = objectMapperService;
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }

    @Override
    public Recipe addRecipe(MultipartFile imageFile, String recipeString) {
        final Recipe recipe = objectMapperService.jsonStringToObject(recipeString, Recipe.class);
        recipe.markAsCreated(userService.getLoggedInUsername());

        logger.info("Adding new recipe: " + recipe);

        if (imageFile != null) {
            handleUploadedFile(imageFile, recipe);
        }

        final Recipe newRecipe = recipeRepository.save(recipe);

        logger.info("New recipe added. Id: " + newRecipe.getId());

        return newRecipe;
    }

    @Override
    public Recipe updateRecipe(MultipartFile imageFile, String recipeString) {
        final Recipe submittedRecipe = objectMapperService.jsonStringToObject(recipeString, Recipe.class);

        logger.info("Updating Recipe: " + submittedRecipe.getId() + ": " + submittedRecipe.getName());

        final Optional<Recipe> existingRecipeOptional = recipeRepository.findById(submittedRecipe.getId());
        if (existingRecipeOptional.isPresent()) {
            Recipe existingRecipe = existingRecipeOptional.get();
            existingRecipe.markAsUpdated(userService.getLoggedInUsername()); // We do this manually as @PreUpdate doesn't work for nested collections
            existingRecipe.setName(submittedRecipe.getName());
            existingRecipe.setDescription(submittedRecipe.getDescription());
            existingRecipe.setImageFileName(submittedRecipe.getImageFileName());
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
                handleUploadedFile(imageFile, submittedRecipe);
            }

            recipeRepository.save(existingRecipe);

            logger.info("Successfully updated Recipe: " + existingRecipe.getId() + ": " + existingRecipe.getName());

            return existingRecipe;
        } else {
            logger.warn("Could not find Recipe with Id: " + submittedRecipe.getId() + " to update");
            return null;
        }
    }

    private void handleUploadedFile(MultipartFile imageFile, Recipe recipe) {
        fileService.saveMultiPartFile(imageFile);
        recipe.setImageFileName(imageFile.getOriginalFilename());
    }

    @Override
    public Recipe findById(int id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(COULD_NOT_FIND_RECIPE_WITH_ID + id));
    }

    @Override
    public Recipe findByName(String name) {
        String formattedName = getFormattedRecipeName(name);
        return recipeRepository.findByName(formattedName).orElseThrow(() -> new ResourceNotFoundException(COULD_NOT_FIND_RECIPE_WITH_NAME + formattedName));
    }

    private String getFormattedRecipeName(String name) {
        return name.replace('-', ' ').toLowerCase();
    }

    @Override
    public List<Recipe> findByTagNames(List<String> tagNames) {
        return recipeRepository.findByAllTagNames(tagNames, (long) tagNames.size());
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public List<RecipeRepository.RecipePreview> findAllRecipePreviewBy() {
        return recipeRepository.findAllRecipePreviewBy();
    }

    @Override
    public List<RecipeRepository.RecipePreview> findLatest() {
        return recipeRepository.findTop6RecipePreviewByOrderByIdDesc();
    }

    @Override
    public List<RecipeRepository.RecipePreview> findRandom() {
        return recipeRepository.findRecipePreviewsOrderByRand();
    }

    @Override
    public Recipe markAsCooked(Integer id) {
        Recipe recipe = findById(id);
        recipe.markedAsCooked();
        recipeRepository.save(recipe);
        return recipe;
    }
}

