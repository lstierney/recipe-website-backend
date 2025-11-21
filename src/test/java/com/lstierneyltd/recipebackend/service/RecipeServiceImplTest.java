package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.dto.RecipeDto;
import com.lstierneyltd.recipebackend.dto.RecipePreviewDto;
import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.entities.RecipePreviewImpl;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.lstierneyltd.recipebackend.service.RecipeServiceImpl.COULD_NOT_FIND_RECIPE_WITH_ID;
import static com.lstierneyltd.recipebackend.service.RecipeServiceImpl.COULD_NOT_FIND_RECIPE_WITH_NAME;
import static com.lstierneyltd.recipebackend.utils.TestConstants.*;
import static com.lstierneyltd.recipebackend.utils.TestStubs.*;
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
    private DtoService dtoService;

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

    private List<Recipe> recipes;

    private Recipe recipe;

    private RecipeDto recipeDto;

    private List<RecipeDto> recipeDtos;

    private RecipePreviewImpl recipePreview;

    private List<RecipeRepository.RecipePreview> recipePreviews;

    private RecipePreviewDto recipePreviewDto;

    private List<RecipePreviewDto> recipePreviewDtos;

    @BeforeEach
    void before() {
        recipe = getRecipe();
        recipes = List.of(recipe);
        recipeDto = getRecipeDto();
        recipeDtos = List.of(recipeDto);

        recipePreview = getRecipePreview();
        recipePreviews = List.of(recipePreview);
        recipePreviewDto = getRecipePreviewDto();
        recipePreviewDtos = List.of(recipePreviewDto);

    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(fileService, objectMapperService, recipeRepository, userService, dtoService);
    }

    @Test
    public void addRecipe() {
        String json = "JSON String";

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(recipe);
        given(recipeRepository.save(recipe)).willReturn(recipe);
        given(userService.getLoggedInUsername()).willReturn(USER_NAME);

        // when
        recipeService.addRecipe(multipartFile, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().save(recipeArgumentCaptor.capture());
        then(fileService).should().createImageFolder(recipe);
        then(fileService).should().addImageToRecipe(multipartFile, recipe);
        then(userService).should().getLoggedInUsername();
        then(dtoService).should().recipeToRecipeDto(recipe);
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
        then(fileService).should().createImageFolder(existingRecipe);
        then(fileService).should().addImageToRecipe(multipartFile, existingRecipe);
        then(dtoService).should().recipeToRecipeDto(existingRecipe);

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
        then(dtoService).should().recipeToRecipeDto(existingRecipe);
    }

    @Test
    public void updateRecipe_recipeNotFound() {
        String json = "JSON String";
        Recipe submittedRecipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(submittedRecipe);
        given(recipeRepository.findById(ID)).willReturn(Optional.empty());

        // when
        RecipeDto updatedRecipeDto = recipeService.updateRecipe(multipartFile, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(recipeRepository).should().findById(ID);

        assertThat(updatedRecipeDto, is(nullValue()));
    }

    @Test
    public void findById() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.of(recipe));

        // when
        recipeService.findById(ID);

        // then
        then(recipeRepository).should().findById(ID);
        then(dtoService).should().recipeToRecipeDto(recipe);
    }

    @Test
    public void findById_recipeNotFound() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoSuchElementException.class, () -> recipeService.findById(ID));

        // Then
        then(recipeRepository).should().findById(ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_ID + ID));
    }

    @Test
    public void findByName() {
        // Given
        given(recipeRepository.findActiveByName(NAME)).willReturn(Optional.of(recipe));

        // when
        recipeService.findByName(NAME);

        // then
        then(recipeRepository).should().findActiveByName(NAME);
        then(dtoService).should().recipeToRecipeDto(recipe);
    }

    @Test
    public void findByName_nameWithMixedCase() {
        // Given
        String name = "Mixed Case";
        given(recipeRepository.findActiveByName("mixed case")).willReturn(Optional.of(recipe));

        // when
        recipeService.findByName(name);

        // then
        then(recipeRepository).should().findActiveByName("mixed case");
        then(dtoService).should().recipeToRecipeDto(recipe);
    }

    @Test
    public void findByName_nameWithHyphens() {
        // Given
        String name = "name-with-hyphens";
        given(recipeRepository.findActiveByName("name with hyphens")).willReturn(Optional.of(recipe));

        // when
        recipeService.findByName(name);

        // then
        then(recipeRepository).should().findActiveByName("name with hyphens");
        then(dtoService).should().recipeToRecipeDto(recipe);
    }

    @Test
    public void findByName_nameWithMixedCaseANDHyphens() {
        // Given
        String name = "NAme-With-mixed-CASE-and-hyphens";
        given(recipeRepository.findActiveByName("name with mixed case and hyphens")).willReturn(Optional.of(recipe));

        // when
        recipeService.findByName(name);

        // then
        then(recipeRepository).should().findActiveByName("name with mixed case and hyphens");
        then(dtoService).should().recipeToRecipeDto(recipe);
    }

    @Test
    public void findByName_recipeNotFound() {
        // Given
        given(recipeRepository.findActiveByName(NAME)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoSuchElementException.class, () -> recipeService.findByName(NAME));

        // Then
        then(recipeRepository).should().findActiveByName(NAME);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_NAME + NAME));
    }

    @Test
    public void findByTagName() {
        // Given
        List<String> tagNames = List.of(TAG_NAME);
        given(recipeRepository.findByAllActiveRecipesByTagNames(tagNames, (long) tagNames.size())).willReturn(recipes);
        given(dtoService.recipesToRecipeDtos(recipes)).willReturn(recipeDtos);

        // when
        recipeService.findByTagNames(tagNames);

        // then
        then(recipeRepository).should().findByAllActiveRecipesByTagNames(tagNames, (long) tagNames.size());
        then(dtoService).should().recipesToRecipeDtos(recipes);
    }

    @Test
    public void findAllActiveRecipes() {
        given(recipeRepository.findActiveRecipes()).willReturn(recipes);
        given(dtoService.recipesToRecipeDtos(recipes)).willReturn(recipeDtos);

        // when
        recipeService.findAllActiveRecipes();

        // then
        then(recipeRepository).should().findActiveRecipes();
    }

    @Test
    public void findAll() {
        // Given
        given(recipeRepository.findAll()).willReturn(recipes);

        // when
        recipeService.findAll();

        // then
        then(recipeRepository).should().findAll();
        then(dtoService).should().recipesToRecipeDtos(recipes);
    }

    @Test
    public void findAllRecipePreview() {
        // Given
        given(recipeRepository.findAllActiveRecipePreviews()).willReturn(recipePreviews);
        given(dtoService.recipePreviewsToRecipePreviewDtos(recipePreviews)).willReturn(recipePreviewDtos);

        // when
        recipeService.findAllActiveRecipePreview();

        // then
        then(recipeRepository).should().findAllActiveRecipePreviews();
        then(dtoService).should().recipePreviewsToRecipePreviewDtos(recipePreviews);
    }

    @Test
    public void findLatestPreviews() {
        // Given
        given(recipeRepository.findSixLatestActiveDinnerPreviews()).willReturn(recipePreviews);
        given(dtoService.recipePreviewsToRecipePreviewDtos(recipePreviews)).willReturn(List.of(recipePreviewDto));

        // when
        List<RecipePreviewDto> latest = recipeService.findLatestPreviews();

        // then
        then(recipeRepository).should().findSixLatestActiveDinnerPreviews();
        then(dtoService).should().recipePreviewsToRecipePreviewDtos(recipePreviews);
        assertThat(latest.get(0), equalTo(recipePreviewDto));
    }

    @Test
    public void findRandomDinners() {
        // Given
        given(recipeRepository.findSixRandomActiveDinners()).willReturn(recipePreviews);
        given(dtoService.recipePreviewsToRecipePreviewDtos(recipePreviews)).willReturn(recipePreviewDtos);

        // when
        List<RecipePreviewDto> randomDinners = recipeService.findRandomDinnersPreviews();

        // then
        then(recipeRepository).should().findSixRandomActiveDinners();
        then(dtoService).should().recipePreviewsToRecipePreviewDtos(recipePreviews);
        assertThat(randomDinners.get(0), equalTo(recipePreviewDto));
    }

    @Test
    public void findRandomDinner() {
        // Given
        given(recipeRepository.findRandomActiveDinnerPreview()).willReturn(recipePreview);
        given(dtoService.recipePreviewToRecipePreviewDto(recipePreview)).willReturn(recipePreviewDto);

        // when
        RecipePreviewDto randomDinner = recipeService.findRandomDinnerPreview();

        // then
        then(recipeRepository).should().findRandomActiveDinnerPreview();
        then(dtoService).should().recipePreviewToRecipePreviewDto(recipePreview);

        assertThat(randomDinner, equalTo(recipePreviewDto));
    }

    @Test
    public void markAsCooked() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.of(recipe));
        given(dtoService.recipeToRecipeDto(recipe)).willReturn(recipeDto);

        // when
        recipeService.markAsCooked(ID);

        // then
        then(recipeRepository).should().findById(ID);
        then(recipeRepository).should().save(recipeArgumentCaptor.capture());
        then(dtoService).should().recipeToRecipeDto(recipe);

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();

        assertThat(capturedRecipe.getCooked(), is(COOKED + 1));
    }

    @Test
    public void markAsCooked_recipeNotFound() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoSuchElementException.class, () -> recipeService.markAsCooked(ID));

        // Then
        then(recipeRepository).should().findById(ID);
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_ID + ID));
    }

    @Test
    public void markAsDeleted() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.of(recipe));
        given(userService.getLoggedInUsername()).willReturn(USER_NAME);
        given(dtoService.recipeToRecipeDto(recipe)).willReturn(recipeDto);
        given(recipeRepository.save(recipe)).willReturn(recipe);

        // when
        recipeService.markAsDeleted(ID);

        // then
        then(recipeRepository).should().findById(ID);
        then(recipeRepository).should().save(recipeArgumentCaptor.capture());
        then(userService).should().getLoggedInUsername();
        then(dtoService).should().recipeToRecipeDto(recipe);

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();
        assertThat(capturedRecipe.isDeleted(), is(true));
    }

    @Test
    public void markAsDeleted_recipeNotFound() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoSuchElementException.class, () -> recipeService.markAsDeleted(ID));

        // Then
        then(recipeRepository).should().findById(ID);
        then(userService).shouldHaveNoInteractions();
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_ID + ID));
    }

    @Test
    public void restore() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.of(recipe));
        given(userService.getLoggedInUsername()).willReturn(USER_NAME);
        given(dtoService.recipeToRecipeDto(recipe)).willReturn(recipeDto);

        // when
        RecipeDto restoredRecipeDto = recipeService.restore(ID);

        // then
        then(recipeRepository).should().findById(ID);
        then(recipeRepository).should().save(recipe);
        then(userService).should().getLoggedInUsername();
        then(dtoService).should().recipeToRecipeDto(recipe);

        assertThat(restoredRecipeDto.isDeleted(), is(false));
    }

    @Test
    public void restore_recipeNotFound() {
        // Given
        given(recipeRepository.findById(ID)).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoSuchElementException.class, () -> recipeService.restore(ID));

        // Then
        then(recipeRepository).should().findById(ID);
        then(userService).shouldHaveNoInteractions();
        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_RECIPE_WITH_ID + ID));
    }
}
