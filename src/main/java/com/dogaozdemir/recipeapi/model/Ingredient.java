package com.dogaozdemir.recipeapi.model;

import com.dogaozdemir.recipeapi.dto.IngredientDto;
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
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private boolean vegetarian;


    public IngredientDto convertToDto(){
        return IngredientDto.builder().id(id).name(name).vegetarian(vegetarian).build();
    }
}
