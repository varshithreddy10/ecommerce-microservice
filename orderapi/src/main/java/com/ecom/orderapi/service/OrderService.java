package com.ecom.orderapi.service;

import com.ecom.orderapi.dto.*;

import java.util.List;

public interface OrderService 
{

    //public PurchaseOrderResponseDto createOrderServ(PurchaseOrderRequestDto purchaseorder);

    public PurchaseOrderResponseDto cancelOrderServ(Long orderId);

    public List<OrderResponseDto> getAllOrdersServ();

    public List<OrderResponseDto> getAllOrdersOfCustomerServ(Long customerId);

    public OrderResponseDto getOrderByOrderIdServ(Long orderId);

    public OrderDto modifyOrderServ(OrderDto orderdto);

    public PurchaseOrderResponseDto placeOrderServ(PlaceOrderDto placeorderdto);

    CustomerDto getCustomerDetails();
}
