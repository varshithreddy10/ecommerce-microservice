package com.ecom.cartapi.service;

import com.ecom.cartapi.dto.CartDto;

import java.util.List;

public interface CartService {
   public CartDto addProductToCartServ(Long productId, Integer quantity , Long customerId);

    public List<CartDto> getAllCartsServ();

    public CartDto getCustomerCartServ(Long customerId);

    public String deleteCustomerCartserv(Long cartId);


    CartDto deleteProductFromCart(Long cartId, Long productId);
}
