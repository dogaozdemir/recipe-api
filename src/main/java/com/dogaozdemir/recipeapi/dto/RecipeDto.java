package com.dogaozdemir.recipeapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeDto {
    private Long id;
    private String name;
    private int numberOfServing;
    private List<InstructionDto> instructionList;
}
