package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.repository.UnitRepository;
import com.lstierneyltd.recipebackend.utils.TestConstants;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.lstierneyltd.recipebackend.controller.RecipeRestControllerImpl.COULD_NOT_FIND_RECIPE_WITH_ID;
import static com.lstierneyltd.recipebackend.controller.RecipeRestControllerImpl.COULD_NOT_FIND_UNIT_WITH_ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.UNIT_ID;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getRecipe;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getUnit;
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
    private UnitRepository unitRepository;

    @InjectMocks
    private RecipeRestControllerImpl recipeRestController;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(recipeRepository, unitRepository);
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
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            recipeRestController.getRecipeById(TestConstants.ID);
        });

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
    public void testNewRecipe() {
        // given
        final Recipe recipe = getRecipe();
        given(unitRepository.findById(UNIT_ID)).willReturn(Optional.of(getUnit()));

        // when
        recipeRestController.newRecipe(recipe);

        // then
        then(unitRepository).should().findById(UNIT_ID);
        then(recipeRepository).should().save(recipe);
    }

    @Test
    public void testNewRecipe_unitNotFound() {
        // given
        given(unitRepository.findById(UNIT_ID)).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            recipeRestController.newRecipe(getRecipe());
        });

        // then
        then(unitRepository).should().findById(UNIT_ID);
        then(recipeRepository).shouldHaveNoInteractions();

        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_UNIT_WITH_ID + UNIT_ID));
    }
}
