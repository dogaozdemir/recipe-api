package com.dogaozdemir.recipeapi.controller;

import com.dogaozdemir.recipeapi.service.UserService;
import com.dogaozdemir.recipeapi.dto.UserCreateDto;
import com.dogaozdemir.recipeapi.dto.UserDto;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static com.dogaozdemir.recipeapi.util.ApiConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(description = API_CREATE_USER_DESCRIPTION,
            summary = API_CREATE_USER_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = API_CREATE_USER_201),
            @ApiResponse(code = 409, message = API_CREATE_USER_409)
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto){

       var userDto =  userService.createUser(userCreateDto).convertToDto();

       return  ResponseEntity.created(URI.create("/v1/user/"+userDto.getId())).body(userDto);

    }

}
