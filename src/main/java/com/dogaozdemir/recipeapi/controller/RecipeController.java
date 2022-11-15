package com.dogaozdemir.recipeapi.controller;

import com.dogaozdemir.recipeapi.dto.*;
import com.dogaozdemir.recipeapi.service.RecipeService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.dogaozdemir.recipeapi.util.ApiConstants.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;


    @PostMapping
    @Operation(description = API_CREATE_RECIPE_DESCRIPTION,
            summary = API_CREATE_RECIPE_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = API_CREATE_RECIPE_201),
            @ApiResponse(code = 404, message = API_CREATE_RECIPE_404)
    })
    public ResponseEntity<RecipeDto> createRecipe(@Valid @RequestBody CreateRecipeDto createRecipeDto, @RequestHeader(HttpHeaders.AUTHORIZATION) long userId){
        var recipeDto = recipeService.createRecipe(createRecipeDto,userId).convertToDto();
        return ResponseEntity.created(URI.create("/v1/recipe/"+recipeDto.getId())).body(recipeDto);

    }



    @PutMapping("/{recipeId}")
    @Operation(description = API_UPDATE_RECIPE_DESCRIPTION,
            summary = API_UPDATE_RECIPE_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_UPDATE_RECIPE_200),
            @ApiResponse(code = 404, message = API_UPDATE_RECIPE_404)
    })
    public ResponseEntity<RecipeDto> updateRecipe(@Valid @RequestBody UpdateRecipeDto updateRecipeDto,@PathVariable long recipeId, @RequestHeader(HttpHeaders.AUTHORIZATION) long userId){
        var recipeDto = recipeService.updateRecipe(updateRecipeDto,userId,recipeId).convertToDto();
        return ResponseEntity.ok(recipeDto);

    }


    @DeleteMapping("/{recipeId}")
    @Operation(description = API_DELETE_RECIPE_DESCRIPTION,
            summary = API_DELETE_RECIPE_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_DELETE_RECIPE_200)
    })
    public ResponseEntity<ActionStatus> deleteRecipe(@PathVariable long recipeId, @RequestHeader(HttpHeaders.AUTHORIZATION) Long userId){
       recipeService.delete(userId,recipeId);
        return ResponseEntity.ok(ActionStatus.builder().status(true).message("Recipe deleted").build());

    }

    @GetMapping
    @Operation(summary = API_FETCH_RECIPE_SUMMARY,
                description = API_FETCH_RECIPE_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_FETCH_RECIPE_200)
    })
    public ResponseEntity<List<RecipeDto>> getAllRecipes(@RequestHeader(HttpHeaders.AUTHORIZATION) Long userId,
                                                   @RequestParam(required = false) Optional<Boolean> vegetarian,
                                                   @RequestParam(required = false) Optional<Integer> numberOfServing,
                                                   @RequestParam(required = false) Optional<List<String>> includeIngredient,
                                                   @RequestParam(required = false) Optional<List<String>> excludeIngredient,
                                                   @RequestParam(required = false) Optional<String> instruction){

        var recipeList = recipeService.getRecipeList(userId,vegetarian,numberOfServing,includeIngredient,excludeIngredient,instruction);

        return ResponseEntity.ok(recipeList);
    }


    @PostMapping("/{recipeId}")
    @Operation(description = API_CREATE_INSTRUCTION_DESCRIPTION,
            summary = API_CREATE_INSTRUCTION_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = API_CREATE_INSTRUCTION_201),
            @ApiResponse(code = 404, message = API_CREATE_INSTRUCTION_404)
    })
    public ResponseEntity<InstructionDto> createInstruction(@Valid @RequestBody CreateInstructionDto createInstructionDto,@PathVariable long recipeId,@RequestHeader(HttpHeaders.AUTHORIZATION) Long userId){

        var instructionDto = recipeService.createInstruction(createInstructionDto,userId,recipeId);
        return ResponseEntity.created(URI.create("/v1/instruction/" +instructionDto.getId())).body(instructionDto);
    }
    @DeleteMapping("/{recipeId}/instruction/{instructionId}")
    @Operation(description = API_DELETE_INSTRUCTION_DESCRIPTION,
            summary = API_DELETE_INSTRUCTION_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = API_DELETE_INSTRUCTION_200)
    })
    public ResponseEntity<ActionStatus> deleteInstruction(@PathVariable Long recipeId,@PathVariable Long instructionId,@RequestHeader(HttpHeaders.AUTHORIZATION) Long userId){
        recipeService.deleteInstruction(recipeId,instructionId,userId);
        return ResponseEntity.ok(ActionStatus.builder().status(true).message("Instruction deleted").build());
    }


}
