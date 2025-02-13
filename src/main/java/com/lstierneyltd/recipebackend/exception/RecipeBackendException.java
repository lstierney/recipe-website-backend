package com.lstierneyltd.recipebackend.exception;

public class RecipeBackendException extends RuntimeException {

    public RecipeBackendException(String message) {
        super(message);
    }

    public RecipeBackendException(Throwable cause) {
        super(cause);
    }
}
