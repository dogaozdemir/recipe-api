package com.dogaozdemir.recipeapi.controller;

import com.dogaozdemir.recipeapi.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Get all recipes from a specific person")
    void givenPersonId_whenCallingGetAllRecipes_thenReturnRecipes() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        //When
        ResponseEntity<RecipeDto[]> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                RecipeDto[].class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("Get all recipes from a specific person based on multiple search parameters")
    void givenPersonIdAndMultipleParameters_whenCallingGetAllRecipes_thenReturnRecipes() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");
        boolean vegetarian = true;
        int amountOfServings = 1;

        //When
        ResponseEntity<RecipeDto[]> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe?vegetarian=" + vegetarian + "&amountOfServings=" + amountOfServings,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                RecipeDto[].class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(Objects.requireNonNull(response.getBody())[0].getName()).isEqualTo("Vegan Sausage");
    }

    @Test
    @DisplayName("Create new recipe")
    void givenNameNumberOfServingsAndPersonId_whenCallingCreateRecipe_thenReturnCreatedRecipe() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Kebab");
        createRecipeDto.setNumberOfServing(3);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        //When
        ResponseEntity<RecipeDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Kebab");
        assertThat(Objects.requireNonNull(response.getBody()).getNumberOfServing()).isEqualTo(3);
    }

    @Test
    @DisplayName("Try to create new recipe with a non-existing person")
    void givenNonExistingPerson_whenCallingCreateRecipe_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "12");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Tomato Soup");
        createRecipeDto.setNumberOfServing(4);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("User not found");
    }

    @Test
    @DisplayName("Try to create new recipe with invalid body")
    void givenWrongBody_whenCallingCreateRecipe_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("");
        createRecipeDto.setNumberOfServing(0);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("Modify recipe")
    void GivenNewRecipeInformation_WhenModifyingRecipe_ThenReturnUpdatedRecipe() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        UpdateRecipeDto updateRecipeDto = new UpdateRecipeDto();
        updateRecipeDto.setName("Potato Soup");
        updateRecipeDto.setNumberOfServing(8);
        HttpEntity<UpdateRecipeDto> requestEntity = new HttpEntity<>(updateRecipeDto, headers);

        ResponseEntity<RecipeDto> initResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );

        var patchRecipeDto = new CreateRecipeDto();
        patchRecipeDto.setName("Chicken Tonight");
        patchRecipeDto.setNumberOfServing(5);

        //When
        ResponseEntity<RecipeDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe/" + Objects.requireNonNull(initResponse.getBody()).getId(),
                    HttpMethod.PUT,
                new HttpEntity<>(patchRecipeDto, headers),
                RecipeDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(patchRecipeDto.getName());
        assertThat(response.getBody().getNumberOfServing()).isEqualTo(patchRecipeDto.getNumberOfServing());
    }

    @Test
    @DisplayName("Modify recipe that is not yours")
    void GivenNewRecipeInformation_WhenModifyingOthersRecipe_ThenThrowError() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "2");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Omelette");
        createRecipeDto.setNumberOfServing(1);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        ResponseEntity<RecipeDto> initResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );

        var updateRecipeDto = new UpdateRecipeDto();
        updateRecipeDto.setName("Omelette");
        updateRecipeDto.setNumberOfServing(5);

        var newHeaders = new HttpHeaders();
        newHeaders.set("Authorization", "5");

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe/" + Objects.requireNonNull(initResponse.getBody()).getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRecipeDto, newHeaders),
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    @DisplayName("Delete recipe")
    void GivenRecipeID_WhenCallingDeleteRecipe_ThenSucceed() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Omelette");
        createRecipeDto.setNumberOfServing(1);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        ResponseEntity<RecipeDto> initResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe/" + Objects.requireNonNull(initResponse.getBody()).getId(),
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    @DisplayName("Create new instruction")
    void givenIngredientNameAndWayOfPreperation_whenCallingCreateInstruction_thenReturnCreatedInstruction() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateInstructionDto createInstructionDto = new CreateInstructionDto();
        createInstructionDto.setIngredientName("Bacon");
        createInstructionDto.setPrepRecipe("Cook bacon for 1 minutes");
        HttpEntity<CreateInstructionDto> requestEntity = new HttpEntity<>(createInstructionDto, headers);

        //When
        ResponseEntity<InstructionDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe/1",
                HttpMethod.POST,
                requestEntity,
                InstructionDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getPrepRecipe()).isEqualTo(createInstructionDto.getPrepRecipe());
        assertThat(Objects.requireNonNull(response.getBody()).getIngredientDto().getName()).isEqualTo(createInstructionDto.getIngredientName());
    }


    @Test
    @DisplayName("Try to create new instruction with non existing ingredient")
    void givenNonExistingIngredientName_whenCallingCreateInstruction_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateInstructionDto createInstructionDto = new CreateInstructionDto();
        createInstructionDto.setIngredientName("XXX");
        createInstructionDto.setPrepRecipe("Cook at oven");
        HttpEntity<CreateInstructionDto> requestEntity = new HttpEntity<>(createInstructionDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/1",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("Try to create new instruction with non existing recipe")
    void givenNonExistingRecipe_whenCallingCreateInstruction_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateInstructionDto createInstructionDto = new CreateInstructionDto();
        createInstructionDto.setIngredientName("Milk");
        createInstructionDto.setPrepRecipe("Boil");
        HttpEntity<CreateInstructionDto> requestEntity = new HttpEntity<>(createInstructionDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/1200",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Delete instruction")
    void givenInstructionAndRecipeId_whenDeletingInstruction_thenDeleteInstruction() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe/1/instruction/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Delete instruction but non-existing instruction")
    void givenNonExistingInstruction_whenDeletingInstruction_thenDeleteInstruction() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe/1/instruction/999",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Delete instruction but non-existing recipe")
    void givenNonExistingRecipe_whenDeletingInstruction_thenDeleteInstruction() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/recipe/999/instruction/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}