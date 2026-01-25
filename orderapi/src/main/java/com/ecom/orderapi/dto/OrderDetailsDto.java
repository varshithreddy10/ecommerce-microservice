package com.ecom.orderapi.dto;

import lombok.Data;

@Data
public class OrderDetailsDto
{
    private Double totalPrice;
    private Integer totalQuantity;
    private Long customerId;

}
