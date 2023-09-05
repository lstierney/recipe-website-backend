package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.service.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

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
        recipeRestController.getAllRecipes();

        // then
        then(recipeService).should().findAll();
    }

    @Test
    public void testGetRecipesList() {
        // When
        recipeRestController.getRecipesPreviewList();

        // then
        then(recipeService).should().findAllRecipePreviewBy();
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
        final String tagName = "pasta";
        // When
        recipeRestController.getRecipesByTagName(tagName);

        // then
        then(recipeService).should().findByTagName(tagName);
    }

    @Test
    public void testUpdateRecipe() {
        // when
        String jsonString = "JSON String";
        recipeRestController.updateRecipe(multipartFile, jsonString);

        // then
        then(recipeService).should().addRecipe(multipartFile, jsonString);
    }

    @Test
    public void testGetLatestRecipe() {
        // when
        recipeRestController.getLatestRecipes();

        // then
        then(recipeService).should().findLatest();
    }

    @Test
    public void testGetRandomRecipe() {
        // when
        recipeRestController.getRandomRecipe();

        // then
        then(recipeService).should().findRandom();
    }

    @Test
    public void testMarkRecipeAsCooked() {
        // when
        recipeRestController.markRecipeAsCooked(ID);

        // then
        then(recipeService).should().markAsCooked(ID);
    }
}
