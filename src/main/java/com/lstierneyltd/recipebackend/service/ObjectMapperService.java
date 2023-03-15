package com.lstierneyltd.recipebackend.service;

public interface ObjectMapperService {
    <T> T jsonStringToObject(String jsonString, Class<T> cls);
}
