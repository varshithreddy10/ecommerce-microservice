package com.ecom.cartapi.service;

import com.ecom.cartapi.dto.CartDto;
import com.ecom.cartapi.dto.CustomerDto;
import com.ecom.cartapi.dto.ProductDto;
import com.ecom.cartapi.entity.CartEntity;
import com.ecom.cartapi.entity.CartItemEntity;
import com.ecom.cartapi.entity.CustomerEntity;
import com.ecom.cartapi.entity.ProductEntity;
import com.ecom.cartapi.exception.GenericApiException;
import com.ecom.cartapi.exception.ResourceNotFoundException;
import com.ecom.cartapi.feignclient.Customerapi_feignclient;
import com.ecom.cartapi.feignclient.Productapi_feignclient;
import com.ecom.cartapi.repository.CartRepository;
import com.ecom.cartapi.repository.CustomerRepository;
import com.ecom.cartapi.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService
{

    @Autowired
    private CartRepository cartrepo;

    @Autowired
    private CustomerRepository customerrepo;

    @Autowired
    private ProductRepository productrepo;

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private Customerapi_feignclient customerapiclient;

    @Autowired
    private Productapi_feignclient productapiclient;

    @Override
    public CartDto addProductToCartServ(Long productId, Integer quantity , Long customerId)
    {
        /*
           step-1 = check product exists with productid or not
           step-2 = check if the customer exists with the customerid or not
           step-3 = check if the user asked quantity is lessthan the existing quantity
           step-4 = check if the product is already present in the cart if already present in  the cart then update the cart +1
           step-5 =
         */



        /*CustomerEntity customerentity = customerrepo.findById(customerId)
                .orElseThrow(()-> new ResourceNotFoundException("customer","customerId",""+customerId));
         */

        // interservice communication
        CustomerDto customerdto = customerapiclient.getCustomerDetails(customerId);
        CustomerEntity customerentity = modelmapper.map(customerdto , CustomerEntity.class);


       /* ProductEntity productentity = productrepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product","productId",""+productId));
        */

        // interservice communication
        ProductDto productdto = productapiclient.getProductDetails(productId);
        ProductEntity productentity = modelmapper.map(productdto , ProductEntity.class);




        /*

          CartEntity cart = createCart(customerentity);
        List<CartItemEntity> allcartitems = cart.getCartitem();
         allcartitems.stream()
                 .filter(individualcartitem-> individualcartitem.getProduct().getProductId()==productId)
                 .map(individualcartitem->{
                     individualcartitem.setQuantity(individualcartitem.getQuantity()+1);
                     individualcartitem.setProductPrice(individualcartitem.getProductPrice()+productentity.getUnitPrice());
                 });

         */
        CartEntity cart = createCart(customerentity);

        Optional<CartItemEntity> existingItem = cart.getCartitem().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();   // stream TERMINATES here

        // question = how the changing the value in the exitstingItem is reflecting in the customerentity

        if (existingItem.isPresent())
        {
            CartItemEntity item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            item.setProductPrice(item.getQuantity() * productentity.getUnitPrice());
            CartEntity savedcart =cartrepo.save(cart);
            return modelmapper.map(savedcart , CartDto.class);
        }

        CartItemEntity newcartitem = new CartItemEntity();
        newcartitem.setQuantity(quantity);
        newcartitem.setProduct(productentity);
        newcartitem.setCart(cart);
        newcartitem.setProductPrice(quantity*productentity.getUnitPrice());
        newcartitem.setDiscount(10);

        //doubt
        cart.setCartitems(newcartitem);
        cart.setTotalPrice(cart.getTotalPrice()+ newcartitem.getProductPrice());
        CartEntity savedcart = cartrepo.save(cart);

        return modelmapper.map(savedcart , CartDto.class);
    }

    private CartEntity createCart(CustomerEntity customerentity)
    {
        /*
           question = how jpa takes the customerId form the customerentity if we use the findByUser(customerentity) in the
                      customerrepo
         */
        CartEntity cart = cartrepo.findByUserCustomerId(customerentity.getCustomerId());

        if(cart !=null)
        {
            return cart;
        }

        CartEntity newcart = new CartEntity();
        newcart.setUser(customerentity);
        newcart.setTotalPrice(0.0);
        cartrepo.save(newcart);

        return newcart;
    }

    @Override
    public List<CartDto> getAllCartsServ()
    {
        List<CartEntity> allcartentities = cartrepo.findAll();
        List<CartDto> allcartdtos = allcartentities.stream()
                .map(individualcartentity -> modelmapper.map(individualcartentity , CartDto.class))
                .toList();

        return allcartdtos;
    }

    @Override
    public CartDto getCustomerCartServ(Long customerId)
    {
        /*
        CustomerEntity customerentity = customerrepo.findById(customerId)
                .orElseThrow(()-> new ResourceNotFoundException("customer","customerId",""+customerId));
         */

        // interservice communication
        CustomerDto customerdto = customerapiclient.getCustomerDetails(customerId);
        CustomerEntity customerentity = modelmapper.map(customerdto , CustomerEntity.class);


        //doubt = why cant we use orelsethrow here
        CartEntity cartentity = cartrepo.findByUserCustomerId(customerId);
        if(cartentity==null)
        {
            throw new GenericApiException("there is no cart for the user with customerId "+customerId);
        }
        return modelmapper.map(cartentity , CartDto.class);
    }

    @Override
    public String deleteCustomerCartserv(Long cartId)
    {
        //doubt = why cant we use orelsethrow here
        CartEntity cartentity = cartrepo.findById(cartId)
                .orElseThrow(()->new GenericApiException("there is no cart for the user with customerId "+cartId));

        cartrepo.deleteById(cartId);
        return "success";
    }

    @Override
    public CartDto deleteProductFromCart(Long cartId, Long productId)
    {
        CartEntity cartentity = cartrepo.findById(cartId)
                .orElseThrow(()->new GenericApiException("there is no cart for the user with customerId "+cartId));


        /*ProductEntity productentity = productrepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product","productId",""+productId));
         */

        // interservice communication
        ProductDto productdto = productapiclient.getProductDetails(productId);
        ProductEntity productentity = modelmapper.map(productdto , ProductEntity.class);

        // check if the product is present in the cart or not
        List<CartItemEntity> allcartitementities = cartentity.getCartitem();
        CartItemEntity carttiem = allcartitementities.stream()
                .filter(individualcartitem->individualcartitem.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(()->new GenericApiException("there is no product with the productId "+productId+" inside the cart with the cartId "+cartId));

        //cartentity.getCartitem().remove(carttiem);
        allcartitementities.remove(carttiem);

        cartentity.setCartitem(null);
        cartentity.setCartitem(allcartitementities);
        CartEntity savedcartentity = cartrepo.save(cartentity);

        return modelmapper.map(savedcartentity , CartDto.class);
    }
}
