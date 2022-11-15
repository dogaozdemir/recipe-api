package com.dogaozdemir.recipeapi.model;

import com.dogaozdemir.recipeapi.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String password;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Recipe> recipeList;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDto convertToDto(){
       return UserDto.builder().id(getId()).username(getUsername()).password(getPassword())
               .build();
    }

}
