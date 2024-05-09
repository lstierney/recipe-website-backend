package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.RecipePreviewImpl;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.lstierneyltd.recipebackend.service.RecipeServiceImpl.COULD_NOT_FIND_RECIPE_WITH_ID;
import static com.lstierneyltd.recipebackend.service.RecipeServiceImpl.COULD_NOT_FIND_RECIPE_WITH_NAME;
import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getRecipe;
import static com.lstierneyltd.recipebackend.utils.TestStubs.getRecipePreview;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Mock
    private UserService userService;
    @Captor
    private ArgumentCaptor<Recipe> recipeArgumentCaptor;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(fileService, objectMapperService, recipeRepository, userService);
    }

    @Test
    public void addRecipe() {
        String json = "JSON String";
        Recipe recipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(recipe);
        given(recipeRepository.save(recipe)).willReturn(recipe);
        given(userService.getLoggedInUsername()).willReturn(USER_NAME);

        // when
        recipeService.addRecipe(multipartFile, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().save(recipeArgumentCaptor.capture());
        then(fileService).should().saveMultiPartFile(multipartFile);
        then(userService).should().getLoggedInUsername();
    }

    @Test
    public void addRecipe_noImage() {
        String json = "JSON String";
        Recipe recipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(recipe);
        given(recipeRepository.save(recipe)).willReturn(recipe);
        given(userService.getLoggedInUsername()).willReturn(USER_NAME);

        // when
        recipeService.addRecipe(null, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().save(recipe);
        then(fileService).shouldHaveNoInteractions();
        then(userService).should().getLoggedInUsername();
    }

    @Test
    public void updateRecipe() {
        String json = "JSON String";

        final Recipe updateDatedRecipe = getRecipe();
        String newDescription = "This is the new description";
        updateDatedRecipe.setDescription(newDescription);
        updateDatedRecipe.setCooked(100);

        final Recipe existingRecipe = getRecipe();
        existingRecipe.setLastCooked(LAST_COOKED);
        existingRecipe.setCooked(COOKED);

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(updateDatedRecipe);
        given(recipeRepository.findById(ID)).willReturn(Optional.of(existingRecipe));
        given(recipeRepository.save(existingRecipe)).willReturn(existingRecipe);
        given(userService.getLoggedInUsername()).willReturn(USER_NAME);

        // when
        recipeService.updateRecipe(multipartFile, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().save(recipeArgumentCaptor.capture());
        then(recipeRepository).should().findById(ID);
        then(fileService).should().saveMultiPartFile(multipartFile);

        // test properties on the updated recipe
        Recipe updatedRecipe = recipeArgumentCaptor.getValue();
        assertThat(updatedRecipe.getDescription(), is(newDescription));
        assertThat(updatedRecipe.getLastUpdatedBy(), is("lawrence"));
        assertTrue(TestUtils.areWithinSeconds(updatedRecipe.getLastUpdatedDate(), LocalDateTime.now(), 10));
        assertThat(updatedRecipe.getCooked(), is(COOKED));
        assertThat(updatedRecipe.getLastCooked(), is(LAST_COOKED));
    }

    @Test
    public void updateRecipe_noImage() {
        String json = "JSON String";
        Recipe submittedRecipe = getRecipe();
        String newDescription = "This is the new description";
        submittedRecipe.setDescription(newDescription);
        Recipe existingRecipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(submittedRecipe);
        given(recipeRepository.findById(ID)).willReturn(Optional.of(existingRecipe));
        given(recipeRepository.save(existingRecipe)).willReturn(existingRecipe);
        given(userService.getLoggedInUsername()).willReturn(USER_NAME);

        // when
        recipeService.updateRecipe(null, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().findById(ID);
        then(recipeRepository).should().save(existingRecipe);
        then(userService).should().getLoggedInUsername();
    }

    @Test
    public void updateRecipe_recipeNotFound() {
        String json = "JSON String";
        Recipe submittedRecipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(submittedRecipe);
        given(recipeRepository.findById(ID)).willReturn(Optional.empty());

        // when
        Recipe updatedRecipe = recipeService.updateRecipe(multipartFile, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().findById(ID);

        assertThat(updatedRecipe, is(nullValue()));
    }

    @Test
    public void findById() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.of(getRecipe()));

        // when
        recipeService.findById(ID);

        // then
        then(recipeRepository).should().findById(ID);
    }

    @Test
    public void findById_recipeNotFound() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.findById(ID));

        // Then
        then(recipeRepository).should().findById(ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_ID + ID));
    }

    @Test
    public void findByName() {
        // Given
        given(recipeRepository.findByName(NAME)).willReturn(Optional.of(getRecipe()));

        // when
        recipeService.findByName(NAME);

        // then
        then(recipeRepository).should().findByName(NAME);
    }

    @Test
    public void findByName_nameWithMixedCase() {
        // Given
        String name = "Mixed Case";
        given(recipeRepository.findByName("mixed case")).willReturn(Optional.of(getRecipe()));

        // when
        recipeService.findByName(name);

        // then
        then(recipeRepository).should().findByName("mixed case");
    }

    @Test
    public void findByName_nameWithHyphens() {
        // Given
        String name = "name-with-hyphens";
        given(recipeRepository.findByName("name with hyphens")).willReturn(Optional.of(getRecipe()));

        // when
        recipeService.findByName(name);

        // then
        then(recipeRepository).should().findByName("name with hyphens");
    }

    @Test
    public void findByName_nameWithMixedCaseANDHyphens() {
        // Given
        String name = "NAme-With-mixed-CASE-and-hyphens";
        given(recipeRepository.findByName("name with mixed case and hyphens")).willReturn(Optional.of(getRecipe()));

        // when
        recipeService.findByName(name);

        // then
        then(recipeRepository).should().findByName("name with mixed case and hyphens");
    }

    @Test
    public void findByName_recipeNotFound() {
        // Given
        given(recipeRepository.findByName(NAME)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.findByName(NAME));

        // Then
        then(recipeRepository).should().findByName(NAME);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_NAME + NAME));
    }

    @Test
    public void findByTagName() {
        // when
        List<String> tagNames = List.of(TAG_NAME);
        recipeService.findByTagNames(tagNames);

        // then
        then(recipeRepository).should().findByAllTagNames(tagNames, (long) tagNames.size());
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
        given(recipeRepository.findTop6RecipePreviewByOrderByIdDesc()).willReturn(List.of(recipePreview));

        // when
        List<RecipeRepository.RecipePreview> latest = recipeService.findLatest();

        // then
        then(recipeRepository).should().findTop6RecipePreviewByOrderByIdDesc();
        assertThat(latest.get(0), equalTo(recipePreview));
    }

    @Test
    public void findRandomDinners() {
        // Given
        RecipePreviewImpl recipePreview = getRecipePreview();
        given(recipeRepository.findRandomDinners()).willReturn(List.of(recipePreview));

        // when
        List<RecipeRepository.RecipePreview> randomDinners = recipeService.findRandomDinners();

        // then
        then(recipeRepository).should().findRandomDinners();
        assertThat(randomDinners.get(0), equalTo(recipePreview));
    }

    @Test
    public void findRandomDinner() {
        // Given
        RecipePreviewImpl recipePreview = getRecipePreview();
        given(recipeRepository.findRandomDinner()).willReturn(recipePreview);

        // when
        RecipeRepository.RecipePreview randomDinner = recipeService.findRandomDinner();

        // then
        then(recipeRepository).should().findRandomDinner();
        assertThat(randomDinner, equalTo(recipePreview));
    }


    @Test
    public void markAsCooked() {
        // Given
        Recipe recipe = getRecipe();
        given(recipeRepository.findById(ID)).willReturn(Optional.of(recipe));

        // when
        Recipe cookedRecipe = recipeService.markAsCooked(ID);

        // then
        then(recipeRepository).should().findById(ID);
        then(recipeRepository).should().save(recipe);

        assertThat(cookedRecipe.getCooked(), is(COOKED + 1));
    }

    @Test
    public void markAsCooked_recipeNotFound() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.markAsCooked(ID));

        // Then
        then(recipeRepository).should().findById(ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_ID + ID));
    }
}
