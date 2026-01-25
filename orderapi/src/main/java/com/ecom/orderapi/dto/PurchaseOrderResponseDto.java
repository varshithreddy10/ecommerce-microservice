package com.ecom.orderapi.dto;

import lombok.Data;

@Data
public class PurchaseOrderResponseDto
{
    private Long orderId;
    private String razorpayOrderId;
    private String orderStatus;
    private String orderTrackingNumber;
    private String paymentStatus;
}
