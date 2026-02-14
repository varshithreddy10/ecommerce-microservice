package com.ecom.orderapi.controller;

import com.ecom.orderapi.dto.*;
import com.ecom.orderapi.repository.OrderRepository;
import com.ecom.orderapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController
{
    //.
    @Autowired
    private OrderService orderservice;

    @Autowired
    private OrderRepository orderrepo;

//    @PostMapping("/createorder")
//    public ResponseEntity<PurchaseOrderResponseDto> createOrder(@RequestBody PurchaseOrderRequestDto purchaseorder)
//    {
//        PurchaseOrderResponseDto purchaseresponse = orderservice.createOrderServ(purchaseorder);
//        return new ResponseEntity<>(purchaseresponse , HttpStatus.CREATED);
//    }

    @GetMapping("/getall/orders")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('ORDER_VIEW_ALL')")
    public  ResponseEntity<List<OrderResponseDto>> getAllOrders()
    {
        List<OrderResponseDto> allorderdtos = orderservice.getAllOrdersServ();
        return new ResponseEntity<>(allorderdtos , HttpStatus.OK);
    }

    @GetMapping("/getall/orders/{customerId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and hasAuthority('ORDER_VIEW')")
    public  ResponseEntity<List<OrderResponseDto>> getAllOrdersofCustomer(@PathVariable Long customerId)
    {
        List<OrderResponseDto> allorderdtos = orderservice.getAllOrdersOfCustomerServ(customerId);
        return new ResponseEntity<>(allorderdtos, HttpStatus.OK);
    }

    @GetMapping("/get/order/byorderid/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and hasAuthority('ORDER_VIEW')")
    public  ResponseEntity<OrderResponseDto> getOrderByOrderId(@PathVariable Long orderId)
    {
        OrderResponseDto orderdto = orderservice.getOrderByOrderIdServ(orderId);
        return new ResponseEntity<>(orderdto , HttpStatus.OK);
    }

    @PutMapping("/modify/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('ORDER_UPDATE')")
    public  ResponseEntity<OrderDto> modifyOrder(@PathVariable Long orderId,@RequestBody OrderDto orderdto)
    {
        OrderDto modifiedorderdto = orderservice.modifyOrderServ(orderdto);

        return new ResponseEntity<>(modifiedorderdto ,HttpStatus.OK);
    }

    @DeleteMapping("/cancel/order/byorderid/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and hasAuthority('ORDER_CANCEL')")
    public  ResponseEntity<PurchaseOrderResponseDto> cancelOrder(@PathVariable Long orderId)
    {
        System.out.println("entered the cancel controller for orderId = "+orderId);
        PurchaseOrderResponseDto purchaseresponse = orderservice.cancelOrderServ(orderId);
        return new ResponseEntity<>(purchaseresponse , HttpStatus.ACCEPTED);
    }

    @PostMapping("/placeorder")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and hasAuthority('ORDER_CREATE')")
    public ResponseEntity<PurchaseOrderResponseDto> placeOrder(@RequestBody PlaceOrderDto placeorderdto)
    {
        PurchaseOrderResponseDto purchaseresponse = orderservice.placeOrderServ(placeorderdto);
       return new ResponseEntity<>(purchaseresponse , HttpStatus.CREATED);
        //return new ResponseEntity<>(new PurchaseOrderResponseDto() , HttpStatus.CREATED);
    }

    @GetMapping("/get/customerdetails/from/orderapi")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER') and hasAuthority('USER_VIEW')")
    public ResponseEntity<CustomerDto> getUserDetails()
    {
        CustomerDto customerdto = orderservice.getCustomerDetails();
        return new ResponseEntity<>(customerdto , HttpStatus.OK);
        //return new ResponseEntity<>(new PurchaseOrderResponseDto() , HttpStatus.CREATED);
    }


}
