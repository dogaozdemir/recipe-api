package com.dogaozdemir.recipeapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private List<RecipeDto> recipeList;
}
