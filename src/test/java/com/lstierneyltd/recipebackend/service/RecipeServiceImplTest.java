package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.RecipePreviewImpl;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.utils.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.lstierneyltd.recipebackend.service.RecipeServiceImpl.COULD_NOT_FIND_RANDOM_RECIPE;
import static com.lstierneyltd.recipebackend.service.RecipeServiceImpl.COULD_NOT_FIND_RECIPE_WITH_ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_NAME;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getRecipe;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getRecipePreview;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {
    @Mock
    private FileService fileService;

    @Mock
    private ObjectMapperService objectMapperService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(fileService, objectMapperService, recipeRepository);
    }

    @Test
    public void addRecipe() {
        String json = "JSON String";
        Recipe recipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(recipe);
        given(recipeRepository.save(recipe)).willReturn(recipe);

        // when
        recipeService.addRecipe(multipartFile, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().save(recipe);
        then(fileService).should().saveMultiPartFile(multipartFile);
    }

    @Test
    public void addRecipe_noImage() {
        String json = "JSON String";
        Recipe recipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(recipe);
        given(recipeRepository.save(recipe)).willReturn(recipe);

        // when
        recipeService.addRecipe(null, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().save(recipe);
        then(fileService).shouldHaveNoInteractions();
    }

    @Test
    public void findById() {
        // Given
        given(recipeRepository.findById(TestConstants.ID)).willReturn(Optional.of(getRecipe()));

        // when
        recipeService.findById(ID);

        // then
        then(recipeRepository).should().findById(ID);
    }

    @Test
    public void findById_recipeNotFound() {
        // Given
        given(recipeRepository.findById(TestConstants.ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.findById(TestConstants.ID));

        // Then
        then(recipeRepository).should().findById(TestConstants.ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_ID + TestConstants.ID));
    }

    @Test
    public void findByTagName() {
        // when
        recipeService.findByTagName(TAG_NAME);

        // then
        then(recipeRepository).should().findByTagName(TAG_NAME);
    }

    @Test
    public void findAll() {
        // when
        recipeService.findAll();

        // then
        then(recipeRepository).should().findAll();
    }

    @Test
    public void findAllRecipePreviewBy() {
        // when
        recipeService.findAllRecipePreviewBy();

        // then
        then(recipeRepository).should().findAllRecipePreviewBy();
    }

    @Test
    public void findLatest() {
        // Given
        RecipePreviewImpl recipePreview = getRecipePreview();
        given(recipeRepository.findTop3RecipePreviewByOrderByIdDesc()).willReturn(List.of(recipePreview));

        // when
        List<RecipeRepository.RecipePreview> latest = recipeService.findLatest();

        // then
        then(recipeRepository).should().findTop3RecipePreviewByOrderByIdDesc();
        assertThat(latest.get(0), equalTo(recipePreview));
    }

    @Test
    public void findRandom() {
        // Given
        RecipePreviewImpl recipePreview = getRecipePreview();
        given(recipeRepository.findRecipePreviewOrderByRand()).willReturn(Optional.of(recipePreview));

        // when
        RecipeRepository.RecipePreview random = recipeService.findRandom();

        // then
        then(recipeRepository).should().findRecipePreviewOrderByRand();
        assertThat(random, equalTo(recipePreview));
    }

    @Test
    public void findRandom_notFound() {
        // Given
        given(recipeRepository.findRecipePreviewOrderByRand()).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.findRandom());

        // Then
        then(recipeRepository).should().findRecipePreviewOrderByRand();
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RANDOM_RECIPE));
    }
}
