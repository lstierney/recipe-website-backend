package com.lstierneyltd.recipebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lstierneyltd.recipebackend.exception.RecipeBackendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapperServiceImpl implements ObjectMapperService {
    private final Logger logger = LoggerFactory.getLogger(ObjectMapperServiceImpl.class);

    private final ObjectMapper objectMapper;

    public ObjectMapperServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T jsonStringToObject(String jsonString, Class<T> cls) {
        T object;
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            object = objectMapper.treeToValue(jsonNode, cls);
        } catch (Exception e) {
            String message = "Could not convert JSON string to object: " + e.getMessage();
            logger.error(message);
            throw new RecipeBackendException(message);
        }
        return object;
    }
}
