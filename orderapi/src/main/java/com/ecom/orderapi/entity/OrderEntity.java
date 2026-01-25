package com.ecom.orderapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "orders")
public class OrderEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String orderTrackingNum;
    private Double totalPrice;
    private Integer totalQuantity;
    private String orderStatus;
    private LocalDate deliveyDate;
    private String paymentStatus;
    private String razorpayOrderId;
    private String razorpayPaymentId;

    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private LocalDate dateCreated;

    @UpdateTimestamp
    @Column(name = "date_updated", insertable = false)
    private LocalDate lastUpdated;

    private String customerEmail;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "addr_id")
    private AddressEntity shippingAddress;
}
