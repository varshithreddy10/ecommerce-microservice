package com.ecom.cartapi.controller;

import com.ecom.cartapi.dto.CartDto;
import com.ecom.cartapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController
{
    //.
    @Autowired
    private CartService cartservice;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}/customer/{customerId}")
    @PreAuthorize("hasRole('USER') and hasAuthority('CART_ADD_ITEM')")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long productId , @PathVariable Integer quantity , @PathVariable Long customerId)
    {
        CartDto cartdto = cartservice.addProductToCartServ(productId , quantity , customerId);
        return new ResponseEntity<>(cartdto , HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CART_VIEW) and hasAuthority('CART_VIEW_ALL')")
    public ResponseEntity<List<CartDto>> getAllCarts()
    {
        List<CartDto> allcartdto = cartservice.getAllCartsServ();
        return new ResponseEntity<>(allcartdto , HttpStatus.CREATED);
    }

    @GetMapping("/get/cart/{customerId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and hasAuthority('CART_VIEW')")
    public  ResponseEntity<CartDto> getCustomerCart(@PathVariable Long customerId)
    {
        CartDto cartdto = cartservice.getCustomerCartServ(customerId);
        return new ResponseEntity<>(cartdto , HttpStatus.FOUND);
    }

    @DeleteMapping("/delete/{cartId}")
    @PreAuthorize("hasRole('USER') and hasAuthority('CART_CLEAR')")
    public ResponseEntity<String> deleteCustomerCart(@PathVariable Long cartId)
    {
        String message = cartservice.deleteCustomerCartserv(cartId);
        return new ResponseEntity<>(message , HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartId}/{productId}")
    @PreAuthorize("hasRole('USER') and hasAuthority('CART_REMOVE_ITEM')")
    public ResponseEntity<CartDto> deleteProductfronCart(@PathVariable Long cartId , @PathVariable Long productId)
    {
        CartDto cartdto= cartservice.deleteProductFromCart(cartId , productId);
        return new ResponseEntity<>(cartdto , HttpStatus.OK);
    }


}
