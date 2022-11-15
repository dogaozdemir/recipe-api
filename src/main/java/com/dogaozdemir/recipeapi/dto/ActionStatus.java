package com.dogaozdemir.recipeapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActionStatus {
    private boolean status;
    private String message;
}
