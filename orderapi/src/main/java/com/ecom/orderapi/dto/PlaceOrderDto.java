package com.ecom.orderapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderDto
{
    private Long customerId;
    private Long addressId;
    private OrderDetailsDto orderDetailsDto;
    //private List<OrderItemDto> orderItemDtoList;
    private List<CartItemDto> cartitemdto;
}
