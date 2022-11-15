package com.dogaozdemir.recipeapi.controller;

import com.dogaozdemir.recipeapi.dto.CreateIngredientDto;
import com.dogaozdemir.recipeapi.dto.IngredientDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IngredientControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    @DisplayName("Create new ingredient")
    void givenNameAndVegetarian_whenCallingCreateIngredient_thenReturnCreatedIngredient() {
        //Given


        CreateIngredientDto createIngredientDto = new CreateIngredientDto();
        createIngredientDto.setName("Bean");
        createIngredientDto.setVegetarian(true);

        HttpEntity<CreateIngredientDto> requestEntity = new HttpEntity<>(createIngredientDto);

        //When
        ResponseEntity<IngredientDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/ingredient",
                HttpMethod.POST,
                requestEntity,
                IngredientDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }


    @Test
    @DisplayName("Try to create new ingredient with invalid body")
    void givenTooLittleCharacters_whenCallingCreateIngredient_thenReturnException() {
        //Given
        CreateIngredientDto createIngredientDto = new CreateIngredientDto();
        createIngredientDto.setName("");
        createIngredientDto.setVegetarian(true);
        HttpEntity<CreateIngredientDto> requestEntity = new HttpEntity<>(createIngredientDto);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/ingredient",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

}