package com.dogaozdemir.recipeapi.service;

import com.dogaozdemir.recipeapi.dto.CreateIngredientDto;
import com.dogaozdemir.recipeapi.dto.IngredientDto;
import com.dogaozdemir.recipeapi.exception.IngredientAlreadyExistException;
import com.dogaozdemir.recipeapi.exception.IngredientNotFoundException;
import com.dogaozdemir.recipeapi.model.Ingredient;
import com.dogaozdemir.recipeapi.model.User;
import com.dogaozdemir.recipeapi.repository.IngredientRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {
    Ingredient ingredient;
    IngredientDto ingredientDto;
    User user;

    CreateIngredientDto createIngredientDto;
    @Mock
    IngredientRepository ingredientRepository;

    @InjectMocks
    IngredientService ingredientService;


    @BeforeEach
    void init(){
        ingredient = Ingredient.builder().name("Tomato").vegetarian(true).build();
        ingredientDto = IngredientDto.builder().name("Tomato").vegetarian(true).build();
        createIngredientDto = CreateIngredientDto.builder().name("Tomato").vegetarian(true).build();

    }

    @Test
    @DisplayName("Create ingredient successfully")
    void GivenAName_WhenCreatingIngredient_ThenReturnCreatedIngredient(){

        when(ingredientRepository.save(any())).thenReturn(ingredient);

        Ingredient result = ingredientService.createIngredient(createIngredientDto);

        assertThat(result.getName()).isEqualTo(ingredient.getName());
        assertThat(result.isVegetarian()).isEqualTo(ingredient.isVegetarian());

        verify(ingredientRepository,times(1)).save(any());

    }

    @Test
    @DisplayName("Throw exception on trying duplicate ingredient")
    void GivenADuplicateName_WhenCreatingIngredient_ThenThrowException(){


        when(ingredientRepository.findByName(anyString())).thenReturn(Optional.of(ingredient));

        assertThatThrownBy(()->ingredientService.createIngredient(createIngredientDto)).isInstanceOf(IngredientAlreadyExistException.class);
        verify(ingredientRepository,times(0)).save(any());

    }


    @Test
    @DisplayName("Find ingredient by name")
    void GivenIngredientName_WhenFindingIngredient_ThenReturnIngredient(){

        when(ingredientRepository.findByName(anyString())).thenReturn(Optional.of(ingredient));

        Ingredient result = ingredientService.findByName(ingredient.getName());

        assertThat(result.getName()).isEqualTo(ingredient.getName());
        assertThat(result.isVegetarian()).isEqualTo(ingredient.isVegetarian());

        verify(ingredientRepository,times(2)).findByName(anyString());

    }

    @Test
    @DisplayName("Not find ingredient by name")
    void GivenNonExistingIngredientName_WhenFindingIngredient_ThenThrowException(){

        when(ingredientRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(IngredientNotFoundException.class,() -> ingredientService.findByName(ingredient.getName()));

        verify(ingredientRepository,times(1)).findByName(anyString());

    }


}