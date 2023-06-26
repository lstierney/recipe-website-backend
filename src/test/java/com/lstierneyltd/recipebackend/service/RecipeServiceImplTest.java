package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.Recipe;
import com.lstierneyltd.recipebackend.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static com.lstierneyltd.recipebackend.utils.TestStubs.getRecipe;
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
}
