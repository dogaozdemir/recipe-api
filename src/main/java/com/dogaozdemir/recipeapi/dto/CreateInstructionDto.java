package com.dogaozdemir.recipeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateInstructionDto {
    @NotEmpty
    private String prepRecipe;
    @NotEmpty
    private String ingredientName;
}
