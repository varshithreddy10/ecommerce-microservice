package com.ecom.orderapi.service;

import com.ecom.orderapi.dto.*;
import com.ecom.orderapi.entity.*;
import com.ecom.orderapi.exception.GenericApiException;
import com.ecom.orderapi.exception.ResourceNotFoundException;
import com.ecom.orderapi.feignclient.CustomerApi_feignclient;
import com.ecom.orderapi.feignclient.ProductApi_feignclient;
import com.ecom.orderapi.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements  OrderService
{
    @Autowired
    private CustomerRepository customerrepo;

    @Autowired
    private OrderRepository orderrepo;

    @Autowired
    private ProductRepository productrepo;

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressrepo;

    @Autowired
    private CustomerApi_feignclient customerfeignclient;

    @Autowired
    private ProductApi_feignclient productfeignclient;


    @Override
    public PurchaseOrderResponseDto cancelOrderServ(Long orderId)
    {
        System.out.println("entered the cancel method for orderId = "+orderId);
        // 1.fetch order details
        OrderEntity orderentity = orderrepo.findById(orderId)
                .orElseThrow(()->new GenericApiException("there is no order with the orderId = "+orderId));
        orderentity.setOrderStatus("CANCELED");
        orderentity.setPaymentStatus("REFUND SUCCESS");
        orderentity.setDeliveyDate(null);

        OrderEntity savedorderentity = orderrepo.save(orderentity);

        PurchaseOrderResponseDto cancelresponse = new PurchaseOrderResponseDto();
        cancelresponse.setOrderTrackingNumber(savedorderentity.getOrderTrackingNum());
        cancelresponse.setPaymentStatus(savedorderentity.getPaymentStatus());
        cancelresponse.setOrderTrackingNumber(savedorderentity.getOrderTrackingNum());
        cancelresponse.setRazorpayOrderId(savedorderentity.getRazorpayOrderId());

        List<OrderItemEntity> allorderitems = orderItemRepository.findAllByOrder_OrderId(orderId);

        System.out.println("these are the orderItems present in the order");
        allorderitems.forEach(System.out::println);

        List<UpdateProductDto> updatepurchasedproducts  = new ArrayList<>();

        allorderitems.forEach(individualorderitem->{
            Long productId = individualorderitem.getProductId();
            Integer quantity = individualorderitem.getQuantity();
            /*
            ProductEntity productentity = productrepo.findById(productId)
                    .orElseThrow(()-> new ResourceNotFoundException("product","productId",""+productId));

                    interservice communication
            */
            ProductDto productdto = productfeignclient.getProductByProductid(productId);
            ProductEntity productentity = modelmapper.map(productdto , ProductEntity.class);

            System.out.println("productquantity after ordering = "+productentity.getUnitsInStock());

            Integer quantityafter = productentity.getUnitsInStock()+quantity;
            System.out.println("productquantity after cancelling = "+quantityafter);
            productentity.setUnitsInStock(quantityafter);
            UpdateProductDto updateproductdto = new UpdateProductDto();
            updateproductdto.setProductId(productId);
            updateproductdto.setQuantity(quantity);
            updateproductdto.setUpdateType("ADD");

            updatepurchasedproducts.add(updateproductdto);

            //ProductEntity savedproduct = productrepo.save(productentity);
            //System.out.println("productquantity after cancelling = "+savedproduct.getUnitsInStock());

        });

        // interservice communication
        productfeignclient.updateAllProducts(updatepurchasedproducts);

        return cancelresponse;
    }

    @Override
    public List<OrderResponseDto> getAllOrdersServ()
    {
        List<OrderEntity> orderentity = orderrepo.findAll();
        List<OrderResponseDto> allorderdtos = orderentity.stream()
                .map(individualorderitem->modelmapper.map(individualorderitem , OrderResponseDto.class))
                .toList();
        return allorderdtos;
    }

    @Override
    public List<OrderResponseDto> getAllOrdersOfCustomerServ(Long customerId)
    {
        //interservice communication checking if the customer is actually present or not
        CustomerDto customerdto = customerfeignclient.getCustomerFeign(customerId);


        List<OrderEntity> orderentity = orderrepo.findByCustomer_CustomerId(customerId);
        List<OrderResponseDto> allorderdtos = orderentity.stream()
                .map(individualorderitem->modelmapper.map(individualorderitem , OrderResponseDto.class))
                .toList();
        return allorderdtos;
    }

    @Override
    public OrderResponseDto getOrderByOrderIdServ(Long orderId)
    {
        OrderEntity orderentity = orderrepo.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("order","orderId",""+orderId));

        return modelmapper.map(orderentity , OrderResponseDto.class);
    }



    public String updateProductDetails(PlaceOrderDto placeorderdto)
    {
        List<CartItemDto> allcartitems=placeorderdto.getCartitemdto();
        allcartitems.forEach(individualcartitem->{

        });


        return null;
    }

    @Override
    public PurchaseOrderResponseDto placeOrderServ(PlaceOrderDto placeorderdto)
    {
        // 1.
        Long customerId = placeorderdto.getCustomerId();
        Long addressId = placeorderdto.getAddressId();
        System.out.println("addressId = "+addressId);
        OrderDetailsDto orderdetailsdto = placeorderdto.getOrderDetailsDto();
        List<CartItemDto> cartitemdto = placeorderdto.getCartitemdto();

//===========================================================================================================================================================================================================================
        // 2. getting customer details from customer repo later get from the customerapi using feignclient interservice communication
        /*CustomerEntity customerentity = customerrepo.findById(customerId)
                .orElseThrow(()-> new ResourceNotFoundException("customer","customerId",""+customerId));*/

        // A. calling customerfeignclient
        CustomerDto customerdto = customerfeignclient.getCustomerFeign(customerId);
        CustomerEntity customerentity = modelmapper.map(customerdto,CustomerEntity.class);

        System.out.println("feignclient customer result = "+customerdto);
//===========================================================================================================================================================================================================================
        //3. convert OrderDetailsDto to OrderDto
        OrderDto orderdto = new OrderDto();

        orderdto.setTotalPrice(orderdetailsdto.getTotalPrice());
        orderdto.setTotalQuantity(orderdetailsdto.getTotalQuantity());
        orderdto.setCustomerEmail(customerentity.getEmail());

        OrderEntity orderentity = modelmapper.map(orderdto , OrderEntity.class);
        orderentity.setCustomer(customerentity);

//===========================================================================================================================================================================================================================
        //4. convert List<CartItemDto> to List<OrderItemDto>

      /*  List<OrderItemDto> orderitemdtos = new ArrayList<>();
                cartitemdto.stream()
                .map(individualcartitem->{
                    OrderItemDto orderitemdto = new OrderItemDto();
                    orderitemdto.setImageUrl(null);
                    orderitemdto.setUnitprice(null);
                    orderitemdto.setQuantity(null);
                    orderitemdto.setProductName(null);
                    orderitemdtos.add(orderitemdto);
                })
      */

        List<OrderItemEntity> orderitementities  = new ArrayList<>();

        List<UpdateProductDto> updatepurchasedproducts  = new ArrayList<>();


        cartitemdto.forEach(individualcartitem -> {
            OrderItemEntity orderitementity = new OrderItemEntity();

            ProductDto productdto = productfeignclient.getProductByProductid(individualcartitem.getProductId());
            ProductEntity productentity = modelmapper.map(productdto,ProductEntity.class);

            /*ProductEntity productentity = productrepo.findById(individualcartitem.getProductId())
                    .orElseThrow(()->new ResourceNotFoundException("product","productId",""+individualcartitem.getProductId()));
            */

            orderitementity.setImageUrl(productentity.getImageUrl());
            orderitementity.setUnitprice(productentity.getUnitPrice());
            orderitementity.setQuantity(individualcartitem.getQuantity());
            orderitementity.setProductName(productentity.getName());
            orderitementity.setProductId(productentity.getProductId());
            orderitementity.setOrder(orderentity);
            orderitementities.add(orderitementity);

            // updating the quantity after placing the order

            UpdateProductDto updateproductdto = new UpdateProductDto();
            updateproductdto.setProductId(individualcartitem.getProductId());
            updateproductdto.setQuantity(individualcartitem.getQuantity());
            updateproductdto.setUpdateType("REMOVE");
            updatepurchasedproducts.add(updateproductdto);

            //productentity.setUnitsInStock(productentity.getUnitsInStock()-individualcartitem.getQuantity());
        });

        String message = productfeignclient.updateAllProducts(updatepurchasedproducts);
        System.out.println("after updating purchased products = "+message +" "+updatepurchasedproducts);


//===========================================================================================================================================================================================================================
        /*
               5. check if the address is saved in the customer with the addressId if not saved first save it using
                  feignclient interservice communication
                -> we have to check it in the customerapi
        */

        /*AddressEntity shippingaddress = addressrepo.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("address","addressId",""+addressId));

         */

        // using the customer feignclient

        AddressDto addressdto = customerfeignclient.getAddressFeign(addressId);
        System.out.println("addressdto from customerfeignclient = "+addressdto);
        AddressEntity shippingaddress = modelmapper.map(addressdto , AddressEntity.class);

                //AddressEntity addressentity = modelmapper.map(addressdto , AddressEntity.class);
//===========================================================================================================================================================================================================================
       /*
                6. setting ordertrackingnumber , razorpay details
        */

        orderentity.setRazorpayOrderId("1234");
        orderentity.setOrderTrackingNum("1234");
        orderentity.setPaymentStatus("COMPLETED");
        orderentity.setOrderStatus("CREATED");
        orderentity.setRazorpayPaymentId("1234");
        orderentity.setShippingAddress(shippingaddress);

        OrderEntity savedorderentity = orderrepo.save(orderentity);

 //===========================================================================================================================================================================================================================
        /*
           update the quantity of the product after successfully placing the order
         */


//===========================================================================================================================================================================================================================

        /*
             7.
         */

        System.out.println("orderitementities foreach");

        orderitementities.forEach(individualorderitem->{

            orderItemRepository.save(individualorderitem);
        });

        System.out.println("after orderitementities foreach");
//===========================================================================================================================================================================================================================

        //8.

        PurchaseOrderResponseDto responseDto = new PurchaseOrderResponseDto();
        responseDto.setOrderId(savedorderentity.getOrderId());
        responseDto.setOrderStatus(savedorderentity.getOrderStatus());
        responseDto.setRazorpayOrderId(savedorderentity.getRazorpayOrderId());
        responseDto.setPaymentStatus(savedorderentity.getPaymentStatus());
        responseDto.setOrderTrackingNumber(savedorderentity.getOrderTrackingNum());
//===========================================================================================================================================================================================================================
        return responseDto;


    }

    @Override
    public CustomerDto getCustomerDetails()
    {
        //Long testcustomerId = SecurityContextHolder.getContext().getAuthentication().getPrincipal()();

        try
        {
            Long customerId = Long.valueOf((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            log.info("NATALIE customerId is = "+customerId);
            // A. calling customerfeignclient
           CustomerDto customerdto = customerfeignclient.getCustomerDetailsByHeader();
            log.info("NATALIE  CustomerDto customerdto = "+customerdto);

            return customerdto;
        }
        catch (Exception e)
        {
            log.info("exception occured in the oderserviceimpl = "+e.getMessage());
        }

        return null;

    }

    @Override
    public OrderDto modifyOrderServ(OrderDto orderdto)
    {
        return null;
    }

}
