package com.ecom.cartapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CartItemEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    //@JsonIgnore
    private CartEntity cart;

    private Integer quantity;
    private double discount;
    private double productPrice;
}
