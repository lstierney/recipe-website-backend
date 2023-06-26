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
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.lstierneyltd.recipebackend.controller.RecipeRestControllerImpl.COULD_NOT_FIND_RECIPE_WITH_ID;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getRecipe;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
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
    public void testGetRecipeById_recipeFound() {
        // Given
        given(recipeRepository.findById(TestConstants.ID)).willReturn(Optional.of(getRecipe()));

        // When
        recipeRestController.getRecipeById(TestConstants.ID);

        // Then
        then(recipeRepository).should().findById(TestConstants.ID);
    }

    @Test
    public void testGetRecipeById_recipeNotFound() {
        // Given
        given(recipeRepository.findById(TestConstants.ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeRestController.getRecipeById(TestConstants.ID));

        // Then
        then(recipeRepository).should().findById(TestConstants.ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_ID + TestConstants.ID));
    }

    @Test
    public void testGetAll() {
        // When
        recipeRestController.getAllRecipes();

        // then
        then(recipeRepository).should().findAll();
    }

    @Test
    public void testGetRecipesList() {
        // When
        recipeRestController.getRecipesList();

        // then
        then(recipeRepository).should().findAllRecipeIdAndNameBy();
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
        then(recipeRepository).should().findByTagName(tagName);
    }
}
