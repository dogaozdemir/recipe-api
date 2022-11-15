package com.dogaozdemir.recipeapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstructionDto {
    private Long id;
    private String prepRecipe;
    private IngredientDto ingredientDto;
}
