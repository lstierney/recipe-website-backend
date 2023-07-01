package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.service.RecipeService;
import com.lstierneyltd.recipebackend.utils.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
public class RecipeRestControllerImplTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeService recipeService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private RecipeRestControllerImpl recipeRestController;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(recipeRepository, recipeService);
    }

    @Test
    public void testGetRecipeById() {

        // When
        recipeRestController.getRecipeById(TestConstants.ID);

        // Then
        then(recipeService).should().findById(TestConstants.ID);
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
        recipeRestController.getRecipesList();

        // then
        then(recipeService).should().findAllRecipeIdAndNameBy();
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
}
