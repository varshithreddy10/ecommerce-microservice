package com.ecom.orderapi.dto;

import com.ecom.orderapi.entity.AddressEntity;
import com.ecom.orderapi.entity.CustomerEntity;
import lombok.Data;


import java.time.LocalDate;

@Data
public class OrderResponseDto
{
    private Long orderId;

    private String orderTrackingNum;
    private Double totalPrice;
    private Integer totalQuantity;
    private String orderStatus;
    private LocalDate deliveyDate;
    private String paymentStatus;
    private LocalDate dateCreated;
    private LocalDate lastUpdated;

    private Long  customerId;
    private Long addressId;
}
