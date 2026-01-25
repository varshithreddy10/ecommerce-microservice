package com.ecom.cartapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name =("cartentity"))
public class CartEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private CustomerEntity user;

    @OneToMany(mappedBy = "cart",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    @JsonManagedReference
    private List<CartItemEntity> cartitem = new ArrayList<>();

    private Double totalPrice;

    public  void setCartitems(CartItemEntity cartitems)
    {
        cartitem.add(cartitems);
    }
}
