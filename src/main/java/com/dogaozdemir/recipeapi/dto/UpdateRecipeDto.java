package com.dogaozdemir.recipeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecipeDto {
        private String name;
        @Min(value = 1, message = "Serving must be at least {min} ")
        private int numberOfServing;

}
