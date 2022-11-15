package com.dogaozdemir.recipeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngredientDto {

    @NotEmpty
    private String name;
    @NotNull
    private boolean vegetarian;
}
