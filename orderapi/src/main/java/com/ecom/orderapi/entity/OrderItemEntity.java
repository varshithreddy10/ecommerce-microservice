package com.ecom.orderapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="order_items")
@Setter
@Getter
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String imageUrl;
    private Integer quantity;
    private Double unitprice;
    private String productName;
    private Long productId;

    @ManyToOne
    @JoinColumn(name="order_id")
    private OrderEntity order;

}
