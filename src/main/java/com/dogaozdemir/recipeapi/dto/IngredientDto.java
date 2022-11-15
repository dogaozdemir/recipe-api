package com.dogaozdemir.recipeapi.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IngredientDto {
    private Long id;
    private String name;
    private boolean vegetarian;
}
