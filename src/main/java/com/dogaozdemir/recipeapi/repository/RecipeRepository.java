package com.dogaozdemir.recipeapi.repository;

import com.dogaozdemir.recipeapi.model.Recipe;
import com.dogaozdemir.recipeapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    Optional<Recipe> findByIdAndUser(Long id, User user);

    @Query(value = "select distinct recipe from Recipe recipe where recipe.user.id = :userId " +
            " and (:vegetarian is null or :vegetarian is false or (( select count(r) from Recipe r join Instruction i on r.id = i.recipe.id " +
            " join Ingredient ingredient on i.ingredient.id = ingredient.id " +
            " where r.id = recipe.id and vegetarian is not :vegetarian) = 0)) " +

            " and (:numberOfServing is null or :numberOfServing = recipe.numberOfServing) " +

            " and ((:includeIngredients) is null or ((select count(r) from Recipe r join Instruction i on r.id = i.recipe.id " +
            " join Ingredient ingredient on i.ingredient.id = ingredient.id " +
            " where r.id = recipe.id and ingredient.name in :includeIngredients) >= 1)) " +
            " and ((:excludeIngredients) is null or ((select count(r) from Recipe r join Instruction i on r.id = i.recipe.id " +
            " join Ingredient ingredient on i.ingredient.id = ingredient.id " +
            " where r.id = recipe.id and ingredient.name in :excludeIngredients) = 0)) " +

            " and (:instruction is null or ((select count(r) from Recipe r join Instruction i on r.id = i.recipe.id " +
            " where r.id = recipe.id and i.prepRecipe like concat('%', :instruction, '%')) >=1))" )
    List<Recipe> search(@Param("userId") Long userId,
                        @Param("vegetarian") Boolean vegetarian,
                        @Param("numberOfServing") Integer numberOfServing,
                        @Param("includeIngredients") List<String> includeIngredients,
                        @Param("excludeIngredients") List<String> excludeIngredients,
                        @Param("instruction") String instruction
                        );


}
