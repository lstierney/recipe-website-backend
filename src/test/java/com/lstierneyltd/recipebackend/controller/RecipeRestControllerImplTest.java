package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.service.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.lstierneyltd.recipebackend.utils.TestConstants.ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.NAME;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
public class RecipeRestControllerImplTest {
    @Mock
    private RecipeService recipeService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private RecipeRestControllerImpl recipeRestController;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(recipeService);
    }

    @Test
    public void testGetRecipeById() {

        // When
        recipeRestController.getRecipeById(ID);

        // Then
        then(recipeService).should().findById(ID);
    }

    @Test
    public void testGetRecipeByName() {

        // When
        recipeRestController.getRecipeByName(NAME);

        // Then
        then(recipeService).should().findByName(NAME);
    }

    @Test
    public void testGetAll() {
        // When
        recipeRestController.getAllActiveRecipes();

        // then
        then(recipeService).should().findAllActiveRecipes();
    }

    @Test
    public void testGetRecipesList() {
        // When
        recipeRestController.getRecipesPreviewList();

        // then
        then(recipeService).should().findAllActiveRecipePreview();
    }

    @Test
    public void testAddRecipe() {
        // when
        String jsonString = "JSON String";
        recipeRestController.addRecipe(multipartFile, jsonString);

        // then
        then(recipeService).should().addRecipe(multipartFile, jsonString);
    }

    @Test
    public void testGetRecipesByTagName() {
        final List<String> tagNames = Arrays.asList("Pasta");

        // When
        recipeRestController.getRecipesByTags(tagNames);

        // then
        then(recipeService).should().findByTagNames(tagNames);
    }

    @Test
    public void testUpdateRecipe() {
        // when
        String jsonString = "JSON String";
        recipeRestController.updateRecipe(multipartFile, jsonString);

        // then
        then(recipeService).should().updateRecipe(multipartFile, jsonString);
    }

    @Test
    public void testGetLatestRecipe() {
        // when
        recipeRestController.getLatestRecipePreviews();

        // then
        then(recipeService).should().findLatestPreviews();
    }

    @Test
    public void testGetRandomDinners() {
        // when
        recipeRestController.getRandomDinnerPreviews();

        // then
        then(recipeService).should().findRandomDinnersPreviews();
    }

    @Test
    public void testGetRandomDinner() {
        // when
        recipeRestController.getRandomDinnerPreview();

        // then
        then(recipeService).should().findRandomDinnerPreview();
    }

    @Test
    public void testMarkRecipeAsCooked() {
        // when
        recipeRestController.markRecipeAsCooked(ID);

        // then
        then(recipeService).should().markAsCooked(ID);
    }

    @Test
    public void testMarkAsDeleted() {
        // when
        recipeRestController.markRecipeAsDeleted(ID);

        // then
        then(recipeService).should().markAsDeleted(ID);
    }

    @Test
    public void testRestoreRecipe() {
        // when
        recipeRestController.restore(ID);

        // then
        then(recipeService).should().restore(ID);
    }

    @Test
    public void testFindAllIgnoreDeleted() {
        // when
        recipeRestController.getAllRecipesIgnoreDeleted();

        // then
        then(recipeService).should().findAll();
    }
}
