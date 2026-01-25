package com.product.api.dto;

import lombok.Data;

@Data
public class UpdateProductDto
{
    private  Long productId;
    private Integer quantity;
    private String updateType;
}
