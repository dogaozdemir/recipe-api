package com.dogaozdemir.recipeapi.controller;

import com.dogaozdemir.recipeapi.dto.UserCreateDto;
import com.dogaozdemir.recipeapi.dto.UserDto;
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

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Create new person")
    void givenUsernameAndPassword_whenCallingCreateUser_thenReturnCreatedPerson() {
        //Given
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("adminuser");
        userCreateDto.setPassword("123");
        HttpEntity<UserCreateDto> requestEntity = new HttpEntity<>(userCreateDto);

        //When
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/user",
                HttpMethod.POST,
                requestEntity,
                UserDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getUsername()).isEqualTo("adminuser");

    }

    @Test
    @DisplayName("Try to create new duplicate person")
    void givenDuplicateUsername_whenCallingCreateUser_thenReturnException() {
        //Given
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("duplicate");
        userCreateDto.setPassword("password");
        HttpEntity<UserCreateDto> requestEntity = new HttpEntity<>(userCreateDto);

        testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/user",
                HttpMethod.POST,
                requestEntity,
                UserDto.class
        );

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/user",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("duplicate");

    }


}