package com.dogaozdemir.recipeapi.service;

import com.dogaozdemir.recipeapi.dto.CreateInstructionDto;
import com.dogaozdemir.recipeapi.exception.InstructionNotFoundException;
import com.dogaozdemir.recipeapi.model.Ingredient;
import com.dogaozdemir.recipeapi.model.Instruction;
import com.dogaozdemir.recipeapi.model.Recipe;
import com.dogaozdemir.recipeapi.repository.InstructionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class InstructionService {

    private final InstructionRepository instructionRepository;
    private final IngredientService ingredientService;
    private final RecipeService recipeService;

    public Instruction create(CreateInstructionDto createInstructionDto, Long recipeId){
        Ingredient ingredient = ingredientService.findByName(createInstructionDto.getIngredientName());
        Recipe recipe = recipeService.findById(recipeId);

        return instructionRepository.save(Instruction.builder().ingredient(ingredient).prepRecipe(createInstructionDto.getPrepRecipe()).recipe(recipe).build());

    }


    public void delete(Long instructionId) {
        Instruction instruction = findById(instructionId);
               instructionRepository.delete(instruction);

    }

    public Instruction findById(Long instructionId){
        Optional<Instruction> instruction = instructionRepository.findById(instructionId);

        if(instruction.isPresent()){
           return instruction.get();
        }else {
            throw new InstructionNotFoundException(""+instructionId);
        }
    }
}
