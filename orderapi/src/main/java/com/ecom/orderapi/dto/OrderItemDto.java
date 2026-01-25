package com.ecom.orderapi.dto;

import lombok.Data;

@Data
public class OrderItemDto
{
    private Integer itemId;
    private String imageUrl;
    private Integer quantity;
    private Double unitprice;
    private String productName;
}
