package com.dogaozdemir.recipeapi.exception;

public class InstructionNotFoundException extends RuntimeException {
    public InstructionNotFoundException(String message) {
        super(message);
    }
}
