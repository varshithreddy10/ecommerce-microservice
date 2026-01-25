package com.ecom.cartapi.dto;

import com.ecom.cartapi.entity.CartItemEntity;
import com.ecom.cartapi.entity.CustomerEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDto
{
    private Long cartId;
    private CustomerEntity user;
    private List<CartItemEntity> cartitem = new ArrayList<>();
    private Double totalPrice;
}
