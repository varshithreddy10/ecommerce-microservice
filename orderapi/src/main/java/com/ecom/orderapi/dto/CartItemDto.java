package com.ecom.orderapi.dto;

import lombok.Data;

@Data
public class CartItemDto
{
    private Integer quantity;
    private double unitprice;
    private  Long productId;
}
