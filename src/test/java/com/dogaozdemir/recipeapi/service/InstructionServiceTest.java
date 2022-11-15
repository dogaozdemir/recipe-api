package com.dogaozdemir.recipeapi.service;

import com.dogaozdemir.recipeapi.dto.CreateInstructionDto;
import com.dogaozdemir.recipeapi.exception.InstructionNotFoundException;
import com.dogaozdemir.recipeapi.model.Ingredient;
import com.dogaozdemir.recipeapi.model.Instruction;
import com.dogaozdemir.recipeapi.model.Recipe;
import com.dogaozdemir.recipeapi.model.User;
import com.dogaozdemir.recipeapi.repository.InstructionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructionServiceTest {

    Ingredient ingredient;
    Recipe recipe;
    Instruction instruction;
    User user;
    CreateInstructionDto createInstructionDto;

    @Mock
    InstructionRepository instructionRepository;

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @InjectMocks
    InstructionService instructionService;

    @BeforeEach
    void init() {
        user = User.builder().username("Doga").password("123456").build();
        ingredient = Ingredient.builder().name("Tomato").vegetarian(true).build();
        recipe = Recipe.builder().name("Tomato Soup").numberOfServing(4).user(user).build();
        createInstructionDto = CreateInstructionDto.builder().ingredientName("Tomato").prepRecipe("180 degrees in oven for 25 minutes").build();
        recipe.setId(1L);

        instruction = Instruction.builder().prepRecipe("180 degrees in oven for 25 minutes").recipe(recipe).ingredient(ingredient).build();
    }

    @Test
    @DisplayName("Find instruction by ID")
    void GivenInstructionId_WhenFetchingInstructionById_ThenReturnInstruction() {
        //Given
        when(instructionRepository.findById(anyLong()))
                .thenReturn(Optional.of(instruction));

        //When
        Instruction result = instructionService.findById(anyLong());

        //Then
        assertThat(result.getPrepRecipe()).isEqualTo(instruction.getPrepRecipe());
        assertThat(result.getIngredient()).isEqualTo(ingredient);
        assertThat(result.getRecipe()).isEqualTo(recipe);

        verify(instructionRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find instruction by non-existing ID")
    void GivenNonExistingInstructionId_WhenFetchingInstructionById_ThenThrowError() {
        //Given
        when(instructionRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //When / Then
        assertThatThrownBy(() -> instructionService.findById(1L)).isInstanceOf(InstructionNotFoundException.class);
        verify(instructionRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Successfully create new instruction")
    void GivenWayOfPreperationIngredientNameAndRecipeId_WhenCreatingInstruction_ThenReturnCreatedInstruction() {
        //Given
        when(ingredientService.findByName(anyString()))
                .thenReturn(ingredient);

        when(recipeService.findById(anyLong()))
                .thenReturn(recipe);

        when(instructionRepository.save(any()))
                .thenReturn(instruction);

        //When
        Instruction result = instructionService.create(createInstructionDto, recipe.getId());

        //Then
        assertThat(result.getPrepRecipe()).isEqualTo(instruction.getPrepRecipe());
        assertThat(result.getIngredient()).isEqualTo(ingredient);
        assertThat(result.getRecipe()).isEqualTo(recipe);

        verify(recipeService, times(1)).findById(anyLong());
        verify(ingredientService, times(1)).findByName(anyString());
        verify(instructionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Delete instruction")
    void GivenInstructionIdAndRecipe_WhenDeletingInstruction_ThenSucceed() {
        //Given
        when(instructionRepository.findById(anyLong()))
                .thenReturn(Optional.of(instruction));

        //When
        instructionService.delete(1L);

        //Then
        verify(instructionRepository, times(1)).findById(anyLong());
    }
}