package com.example.spring2.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequest { //sipariş oluşturma isteği
    //@NotNull
    //private Long customerId;

    @NotEmpty
    @Valid
    private List<PlaceOrderItemRequest> items;
}
