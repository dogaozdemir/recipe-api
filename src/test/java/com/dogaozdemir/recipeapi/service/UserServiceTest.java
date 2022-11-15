package com.dogaozdemir.recipeapi.service;

import com.dogaozdemir.recipeapi.dto.UserCreateDto;
import com.dogaozdemir.recipeapi.exception.UserAlreadyExistException;
import com.dogaozdemir.recipeapi.exception.UserNotFoundException;
import com.dogaozdemir.recipeapi.model.User;
import com.dogaozdemir.recipeapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    User user;
    UserCreateDto userCreateDto;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void init(){
        user = User.builder().username("Doga").password("123456").build();
        userCreateDto = UserCreateDto.builder().username("Doga").password("123456").build();
    }



    @Test
    @DisplayName("Get user by ID")
    void testFindUser_WhenGivenDataValid_shouldReturnUser() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));


        User result = userService.getUserById(1L);


        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Not find user by ID")
    void testFindUser_WhenGivenDataNotValid_shouldThrowUserNotFoundException() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> userService.getUserById(10L)).isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(anyLong());
    }







    @Test
    @DisplayName("Create user succesfully")
    void testCreateUser_WhenGivenDataValid_shouldReturnCreatedUser(){

        when(userRepository.save(any())).thenReturn(user);
        User result = userService.createUser(userCreateDto);

        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("Throw exception on trying to create duplicate user")
    void testCreateUser_WhenGivenDataDuplicate_shouldReturnUserAlreadyExistException() {

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));


        assertThatThrownBy(() -> userService.createUser(userCreateDto)).isInstanceOf(UserAlreadyExistException.class);
        verify(userRepository, times(0)).save(any());
    }

}