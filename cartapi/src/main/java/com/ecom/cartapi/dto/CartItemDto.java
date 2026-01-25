package com.ecom.cartapi.dto;

import com.ecom.cartapi.entity.CartEntity;
import com.ecom.cartapi.entity.ProductEntity;
import lombok.Data;

@Data
public class CartItemDto
{
    private Long Id;
    private ProductEntity product;
    private CartEntity cart;
    private Integer quantity;
    private double discount;
    private double productPrice;
}
