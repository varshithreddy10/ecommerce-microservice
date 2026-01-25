package com.ecom.orderapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseOrderRequestDto
{
    private Long customerId;
    private AddressDto addressDto;
    private OrderDetailsDto orderDetailsDto;
    //private List<OrderItemDto> orderItemDtoList;
    private List<CartItemDto> cartitemdto;
}
