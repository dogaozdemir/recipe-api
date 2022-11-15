package com.dogaozdemir.recipeapi.util;

public class ApiConstants {

    public static final String API_CREATE_USER_DESCRIPTION = "Saves user to the db";
    public static final String API_CREATE_USER_SUMMARY = "Create new user";
    public static final String API_CREATE_USER_201 = "User successfully created";
    public static final String API_CREATE_USER_409 = "User already exist";

    public static final String API_CREATE_RECIPE_DESCRIPTION = "Saves recipe to the db";
    public static final String API_CREATE_RECIPE_SUMMARY = "Create new recipe";
    public static final String API_CREATE_RECIPE_201 = "Recipe successfully created";
    public static final String API_CREATE_RECIPE_404 = "We could not found the user";

    public static final String API_UPDATE_RECIPE_DESCRIPTION = "Updates recipe to the db";
    public static final String API_UPDATE_RECIPE_SUMMARY = "Update recipe";
    public static final String API_UPDATE_RECIPE_200 = "Recipe successfully updated";
    public static final String API_UPDATE_RECIPE_404 = "Recipe not found";

    public static final String API_DELETE_RECIPE_DESCRIPTION = "Deletes recipe from db";
    public static final String API_DELETE_RECIPE_SUMMARY = "Delete recipe";
    public static final String API_DELETE_RECIPE_200 = "Recipe successfully deleted";

    public static final String API_FETCH_RECIPE_DESCRIPTION = "User can filter recipe lists with vegetarian,servings,include and exclude ingredients and instruction options";
    public static final String API_FETCH_RECIPE_SUMMARY = "Recipe list that filtered by user";
    public static final String API_FETCH_RECIPE_200 = "Recipe successfully fetched";



    public static final String API_CREATE_INGREDIENT_DESCRIPTION = "Saves new valid ingredient to the db";
    public static final String API_CREATE_INGREDIENT_SUMMARY = "Create new ingredient";
    public static final String API_CREATE_INGREDIENT_201 = "Ingredient successfully created";
    public static final String API_CREATE_INGREDIENT_404 = "We could not found the ingredient";

    public static final String API_CREATE_INSTRUCTION_DESCRIPTION = "Create new instruction";
    public static final String API_CREATE_INSTRUCTION_SUMMARY = "Instruction saved in database";
    public static final String API_CREATE_INSTRUCTION_201 = "Instruction successfully created";
    public static final String API_CREATE_INSTRUCTION_404 = "We could not found the instruction";

    public static final String API_DELETE_INSTRUCTION_DESCRIPTION = "Deletes instruction from db";
    public static final String API_DELETE_INSTRUCTION_SUMMARY = "Delete instruction";
    public static final String API_DELETE_INSTRUCTION_200 = "Instruction successfully deleted";
}
