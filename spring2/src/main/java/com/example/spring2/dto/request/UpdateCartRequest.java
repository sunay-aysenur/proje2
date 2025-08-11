package com.example.spring2.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartRequest {

    @NotEmpty(message = "Cart items cannot be empty")
    @Valid
    private List<UpdateCartItemRequest> items;
}
