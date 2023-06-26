package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import com.lstierneyltd.recipebackend.repository.TagRepository;
import com.lstierneyltd.recipebackend.repository.UnitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.lstierneyltd.recipebackend.service.RecipeServiceImpl.COULD_NOT_FIND_TAG_WITH_ID;
import static com.lstierneyltd.recipebackend.service.RecipeServiceImpl.COULD_NOT_FIND_UNIT_WITH_ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.TAG_ID;
import static com.lstierneyltd.recipebackend.utils.TestConstants.UNIT_ID;
import static com.lstierneyltd.recipebackend.utils.TestStubs.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {
    @Mock
    private UnitRepository unitRepository;

    @Mock
    private TagRepository tagRepository;

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

    private String json;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(unitRepository, tagRepository, fileService,
                objectMapperService, recipeRepository);
    }

    @Test
    public void addRecipe_happyPath() {
        String json = "JSON String";
        Recipe recipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(recipe);
        given(unitRepository.findById(UNIT_ID)).willReturn(Optional.of(getUnit()));
        given(tagRepository.findById(TAG_ID)).willReturn(Optional.of(getTag()));
        given(recipeRepository.save(recipe)).willReturn(recipe);

        // when
        recipeService.addRecipe(multipartFile, json);

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(unitRepository).should().findById(UNIT_ID);
        then(tagRepository).should().findById(TAG_ID);
        then(recipeRepository).should().save(recipe);
        then(fileService).should().saveMultiPartFile(multipartFile);
    }

    @Test
    public void addRecipe_exception_unitNotFound() {
        json = "JSON String";
        Recipe recipe = getRecipe();

        // given
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(recipe);
        given(unitRepository.findById(UNIT_ID)).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.addRecipe(multipartFile, json));

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(unitRepository).should().findById(UNIT_ID);
        then(recipeRepository).shouldHaveNoInteractions();

        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_UNIT_WITH_ID + UNIT_ID));
    }

    @Test
    public void addRecipe_exception_TagNotFound() {
        // given
        final Recipe recipe = getRecipe();
        given(objectMapperService.jsonStringToObject(json, Recipe.class)).willReturn(recipe);
        given(unitRepository.findById(UNIT_ID)).willReturn(Optional.of(getUnit()));
        given(tagRepository.findById(TAG_ID)).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.addRecipe(multipartFile, json));

        // then
        then(objectMapperService).should().jsonStringToObject(json, Recipe.class);
        then(unitRepository).should().findById(UNIT_ID);
        then(tagRepository).should().findById(TAG_ID);
        then(recipeRepository).shouldHaveNoInteractions();

        assertThat(exception.getMessage(), equalTo(COULD_NOT_FIND_TAG_WITH_ID + TAG_ID));
    }
}
