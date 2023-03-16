package com.lstierneyltd.recipebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lstierneyltd.recipebackend.exception.RecipeBackendException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class ObjectMapperServiceImplTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JsonNode jsonNode;

    @InjectMocks
    private ObjectMapperServiceImpl objectMapperService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(objectMapper);
    }

    @Test
    public void testJsonToObject() throws JsonProcessingException {
        // Given
        String jsonString = "JSON String";
        given(objectMapper.readTree(jsonString)).willReturn(jsonNode);

        // When
        objectMapperService.jsonStringToObject(jsonString, Record.class);

        // then
        then(objectMapper).should().readTree(jsonString);
        then(objectMapper).should().treeToValue(jsonNode, Record.class);
    }

    @Test
    public void testJsonToObject_exception() throws JsonProcessingException {
        // Given
        String jsonString = "JSON String";
        String jsonException = "JSON Exception";

        given(objectMapper.readTree(jsonString)).willThrow(new JsonMappingException(jsonException));

        // When
        Exception exception = assertThrows(RecipeBackendException.class, () -> {
            objectMapperService.jsonStringToObject(jsonString, Record.class);
        });

        // then
        assertThat(exception.getMessage(), equalTo("Could not convert JSON string to object: JSON Exception"));
    }
}
