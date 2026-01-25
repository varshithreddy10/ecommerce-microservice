package com.ecom.cartapi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductDto
{
    private Long productId;
    private String name;
    private String description;
    private String title;
    private Double unitPrice;
    private String imageUrl;
    private boolean active;
    private int unitsInStock;

    private LocalDate dateCreated;
    private LocalDate lastUpdated;

    //private ProductCategory category;
}
