package com.dogaozdemir.recipeapi.service;

import com.dogaozdemir.recipeapi.dto.UserCreateDto;
import com.dogaozdemir.recipeapi.exception.UserAlreadyExistException;
import com.dogaozdemir.recipeapi.exception.UserNotFoundException;
import com.dogaozdemir.recipeapi.model.User;
import com.dogaozdemir.recipeapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User createUser(UserCreateDto userCreateDto){
        if(!isUserExist(userCreateDto.getUsername())){
            return userRepository.save( User.builder()
                    .username(userCreateDto.getUsername())
                    .password(userCreateDto.getPassword())
                    .build());
        }else{
            throw new UserAlreadyExistException("Sorry, " + userCreateDto.getUsername() + " is already exist");
        }

    }

    public boolean isUserExist(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }



}
