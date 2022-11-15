package com.dogaozdemir.recipeapi.exception;

public class IngredientAlreadyExistException extends RuntimeException {
    public IngredientAlreadyExistException(String message) {
        super(message);
    }
}
