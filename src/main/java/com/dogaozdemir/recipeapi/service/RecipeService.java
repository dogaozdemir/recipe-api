package com.dogaozdemir.recipeapi.service;

import com.dogaozdemir.recipeapi.dto.*;
import com.dogaozdemir.recipeapi.exception.RecipeNotFoundException;
import com.dogaozdemir.recipeapi.exception.UnauthorizedException;
import com.dogaozdemir.recipeapi.model.Recipe;
import com.dogaozdemir.recipeapi.model.User;
import com.dogaozdemir.recipeapi.repository.RecipeRepository;
import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class RecipeService {

    private final RecipeRepository recipeRepository;
    @Lazy
    private final UserService userService;
    @Lazy
    private final InstructionService instructionService;

    public Recipe createRecipe(CreateRecipeDto createRecipeDto,long userId){
        var user = userService.getUserById(userId);

        return recipeRepository.save(Recipe.builder()
                .name(createRecipeDto.getName())
                .numberOfServing(createRecipeDto.getNumberOfServing())
                .user(user)
                .build());
    }

    public Recipe updateRecipe(UpdateRecipeDto updateRecipeDto, long userId, long recipeId) {
        var user = userService.getUserById(userId);
        var recipe = getRecipeByIdAndUser(recipeId,user);
        if(StringUtils.isNoneEmpty(updateRecipeDto.getName())){
            recipe.setName(updateRecipeDto.getName());
        }
        if(updateRecipeDto.getNumberOfServing()>0){
            recipe.setNumberOfServing(updateRecipeDto.getNumberOfServing());
        }
        return recipeRepository.save(recipe);
    }


    public Recipe getRecipeByIdAndUser(Long id, User user){
        return recipeRepository.findByIdAndUser(id,user).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
    }

    public void delete(Long userId, Long recipeId) {
        var user = userService.getUserById(userId);
        var recipe = getRecipeByIdAndUser(recipeId,user);
        recipeRepository.delete(recipe);

    }

    public List<RecipeDto> getRecipeList(Long userId, Optional<Boolean> vegetarian, Optional<Integer> numberOfServing, Optional<List<String>> includeIngredient, Optional<List<String>> excludeIngredient, Optional<String> instruction) {
        userService.getUserById(userId);
        var recipeList = recipeRepository.search(userId,vegetarian.orElse(null),numberOfServing.orElse(null),
                includeIngredient.orElse(null), excludeIngredient.orElse(null),instruction.orElse(null));


        return recipeList.stream().map(Recipe::convertToDto).collect(Collectors.toList());

    }

    public InstructionDto createInstruction(CreateInstructionDto createInstructionDto, Long userId, Long recipeId) {
        checkIfOwnerRecipe(userId,recipeId);
       return instructionService.create(createInstructionDto,recipeId).convertToDto();
    }

    private void checkIfOwnerRecipe(Long userId,Long recipeId){
        if(!findById(recipeId).getUser().getId().equals(userId)){

            throw new UnauthorizedException("Owner not has this recipe");
        }
    }

    public Recipe findById(Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if(recipe.isPresent()){
            return recipe.get();
        }else{
            throw new RecipeNotFoundException("Sorry, recipe id : " + recipeId + " is not found");
        }

    }

    public void deleteInstruction(Long recipeId, Long instructionId, Long userId) {
        checkIfOwnerRecipe(userId,recipeId);
        instructionService.delete(instructionId);
    }


}
