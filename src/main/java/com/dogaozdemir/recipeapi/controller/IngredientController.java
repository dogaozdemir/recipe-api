package com.dogaozdemir.recipeapi.controller;

import com.dogaozdemir.recipeapi.dto.CreateIngredientDto;
import com.dogaozdemir.recipeapi.dto.IngredientDto;
import com.dogaozdemir.recipeapi.service.IngredientService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static com.dogaozdemir.recipeapi.util.ApiConstants.*;


@RestController
@AllArgsConstructor
@RequestMapping("/v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;



    @PostMapping
    @Operation(description = API_CREATE_INGREDIENT_DESCRIPTION,
            summary = API_CREATE_INGREDIENT_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = API_CREATE_INGREDIENT_201),
            @ApiResponse(code = 404, message = API_CREATE_INGREDIENT_404)
    })
    public ResponseEntity<IngredientDto> createIngredient(@Valid @RequestBody CreateIngredientDto createIngredientDto){
        var ingredientDto = ingredientService.createIngredient(createIngredientDto).convertToDto();
        return ResponseEntity.created(URI.create("/v1/ingredient/"+ingredientDto.getId())).body(ingredientDto);

    }
}
