package com.dogaozdemir.recipeapi.model;

import com.dogaozdemir.recipeapi.dto.InstructionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String prepRecipe;
    @ManyToOne
    private Ingredient ingredient;
    @ManyToOne
    private Recipe recipe;

    public InstructionDto convertToDto(){
        return InstructionDto.builder().id(id).prepRecipe(prepRecipe).ingredientDto(ingredient.convertToDto()).build();
    }
}
