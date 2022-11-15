package com.dogaozdemir.recipeapi.service;

import com.dogaozdemir.recipeapi.dto.CreateIngredientDto;
import com.dogaozdemir.recipeapi.exception.IngredientAlreadyExistException;
import com.dogaozdemir.recipeapi.exception.IngredientNotFoundException;
import com.dogaozdemir.recipeapi.model.Ingredient;
import com.dogaozdemir.recipeapi.repository.IngredientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;


    public Ingredient createIngredient(CreateIngredientDto createIngredientDto) {

        if (!isIngredientExist(createIngredientDto.getName())) {
            return ingredientRepository.save(Ingredient.builder().name(createIngredientDto.getName()).vegetarian(createIngredientDto.isVegetarian()).build());
        } else {
            throw new IngredientAlreadyExistException("Sorry, " + createIngredientDto.getName() + " is already exist");
        }


    }

    private boolean isIngredientExist(String name) {
        return ingredientRepository.findByName(name).isPresent();

    }

    public Ingredient findByName(String ingredientName) {
        if (isIngredientExist(ingredientName)) {
            return ingredientRepository.findByName(ingredientName).get();
        } else {
            throw new IngredientNotFoundException("Sorry, ingredient name: " + ingredientName + " is not found");
        }
    }
}
