package com.dogaozdemir.recipeapi.service;

import com.dogaozdemir.recipeapi.dto.*;
import com.dogaozdemir.recipeapi.exception.RecipeNotFoundException;
import com.dogaozdemir.recipeapi.model.Ingredient;
import com.dogaozdemir.recipeapi.model.Instruction;
import com.dogaozdemir.recipeapi.model.Recipe;
import com.dogaozdemir.recipeapi.model.User;
import com.dogaozdemir.recipeapi.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    User user;
    Recipe recipe;
    CreateRecipeDto createRecipeDto;
    UpdateRecipeDto updateRecipeDto;
    CreateInstructionDto createInstructionDto;
    Instruction instruction;

    Ingredient ingredient;


    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UserService userService;
    @Mock
    InstructionService instructionService;

    @InjectMocks
    RecipeService recipeService;

    @BeforeEach
    void init() {
        user = User.builder().username("Doga").password("123456").build();
        recipe = Recipe.builder().name("Tomato Soup").numberOfServing(4).user(user).build();
        createRecipeDto = CreateRecipeDto.builder().name("Tomato Soup").numberOfServing(4).build();
        updateRecipeDto = UpdateRecipeDto.builder().name("Tomato Soup").numberOfServing(4).build();
        ingredient = Ingredient.builder().name("Tomato").vegetarian(true).build();
        instruction = Instruction.builder().prepRecipe("180 degrees in oven for 25 minutes").recipe(recipe).ingredient(ingredient).build();
    }

    @Test
    @DisplayName("Find recipe by ID")
    void GivenRecipeId_WhenFindingRecipe_ThenReturnRecipe() {
        //Given
        recipe.setId(1L);

        when(recipeRepository.findById(anyLong()))
                .thenReturn(Optional.of(recipe));

        //When
        Recipe result = recipeService.findById(recipe.getId());

        //Then
        assertThat(result.getName()).isEqualTo(recipe.getName());
        assertThat(result.getNumberOfServing()).isEqualTo(recipe.getNumberOfServing());
        assertThat(result.getUser().getId()).isEqualTo(user.getId());

        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find non-existing recipe by ID")
    void GivenNonExistingRecipeId_WhenFindingRecipe_ThenThrowException() {
        //Given
        when(recipeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //When / Then
        assertThatThrownBy(() -> recipeService.findById(1L)).isInstanceOf(RecipeNotFoundException.class);
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find all recipes of person")
    void GivenPersonId_WhenFindingAllRecipesOfPerson_ThenReturnListOfRecipes() {
        //Given
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(Recipe.builder().name("Tomato Soup").numberOfServing(4).user(user).build());
        recipes.add(Recipe.builder().name("Pizza").numberOfServing(2).user(user).build());

        when(userService.getUserById(anyLong()))
                .thenReturn(user);

        lenient().when(recipeRepository.search(anyLong(), any(), any(), any(), any(), any()))
                .thenReturn(recipes);

        //When
        List<RecipeDto> result = recipeService.getRecipeList(1L, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        //Then
        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("Successfully create new recipe")
    void GivenNameNumberOfServingsAndPersonId_WhenCreatingRecipe_ThenReturnCreatedRecipe() {
        //Given
        when(recipeRepository.save(any()))
                .thenReturn(recipe);

        when(userService.getUserById(anyLong()))
                .thenReturn(user);

        //When
        Recipe result = recipeService.createRecipe(createRecipeDto, 1L);

        //Then
        assertThat(result.getName()).isEqualTo(recipe.getName());
        assertThat(result.getNumberOfServing()).isEqualTo(recipe.getNumberOfServing());
        assertThat(result.getUser().getId()).isEqualTo(user.getId());

        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Update recipe")
    void GivenUpdatedValues_WhenUpdatingRecipe_ThenReturnUpdatedRecipe() {
        //Given
        recipe.setId(1L);
        user.setId(1L);


        recipe.setNumberOfServing(4);
        recipe.setName("ModifiedRecipe");


        when(recipeRepository.findByIdAndUser(anyLong(),any()))
                .thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any()))
                .thenReturn(recipe);

        //When
        Recipe result = recipeService.updateRecipe(updateRecipeDto,1L, 1L);

        //Then
        assertThat(result.getName()).isEqualTo(recipe.getName());
        assertThat(result.getNumberOfServing()).isEqualTo(recipe.getNumberOfServing());
        assertThat(result.getUser().getId()).isEqualTo(user.getId());

        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Delete recipe")
    void GivenPersonAndRecipeId_WhenDeletingRecipe_ThenSucceed() {
        //Given
        recipe.setId(1L);
        user.setId(1L);


        when(recipeRepository.findByIdAndUser(anyLong(),any()))
                .thenReturn(Optional.of(recipe));
        //When
        recipeService.delete(1L, 1L);

        //Then
        verify(recipeRepository, times(1)).delete(any());

    }

    @Test
    @DisplayName("Successfully create new instruction")
    void GivenVariablesForInstruction_WhenCreatingInstruction_ThenReturnCreatedInstruction() {
        //Given
        when(instructionService.create(any(), anyLong()))
                .thenReturn(instruction);

        recipe.setId(1L);
        user.setId(1L);

        when(recipeRepository.findById(anyLong()))
                .thenReturn(Optional.of(recipe));

        //When
        InstructionDto result = recipeService.createInstruction(any(), 1L, 1L);

        //Then

        assertThat(result.getIngredientDto()).isEqualTo(instruction.getIngredient().convertToDto());
        assertThat(result.getPrepRecipe()).isEqualTo(instruction.getPrepRecipe());

    }

    @Test
    @DisplayName("Delete instruction")
    void GivenVariablesForInstruction_WhenDeletingInstruction_ThenSucceed() {
        //Given
        recipe.setId(1L);
        user.setId(1L);

        when(recipeRepository.findById(anyLong()))
                .thenReturn(Optional.of(recipe));


        //When
        recipeService.deleteInstruction(1L, 1L, 1L);

        //Then
        verify(instructionService, times(1)).delete(anyLong());
    }


}