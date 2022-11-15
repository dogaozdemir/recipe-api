package com.dogaozdemir.recipeapi.model;

import com.dogaozdemir.recipeapi.dto.InstructionDto;
import com.dogaozdemir.recipeapi.dto.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private int numberOfServing;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Instruction> instructionList;
    @ManyToOne
    private User user;

    public Recipe(String name, int numberOfServing) {
        this.name = name;
        this.numberOfServing = numberOfServing;
    }

    public RecipeDto convertToDto() {
        return RecipeDto.builder()
                .id(id)
                .name(name)
                .numberOfServing(numberOfServing)
                .instructionList(convertToInstructionListDto(instructionList))
                .build();
    }

    private static List<InstructionDto> convertToInstructionListDto(List<Instruction> instructionList){
        if(instructionList == null || instructionList.isEmpty()){
            return Collections.emptyList();
        }else{
            return instructionList.stream()
                    .map(i -> InstructionDto.builder().id(i.getId()).prepRecipe(i.getPrepRecipe()).ingredientDto(i.convertToDto().getIngredientDto()).build()).collect(Collectors.toList());

        }
    }




}
