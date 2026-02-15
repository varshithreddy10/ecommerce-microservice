package com.product.api.controller;

import com.product.api.dto.CategoryDto;
import com.product.api.dto.ProductDto;
import com.product.api.dto.UpdateProductDto;
import com.product.api.entity.ProductCategory;
import com.product.api.exception.ResourceNotFoundException;
import com.product.api.repository.CategoryRepository;

import com.product.api.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController
{
    @Autowired
    private ProductService productservice;

    @Autowired
    private CategoryRepository catrepo;

    @Autowired
    private ModelMapper modelmapper;

    @GetMapping("/get/allproducts")
    //@PreAuthorize("hasRole('ADMIN') and hasAuthority('PRODUCT_VIEW')") . .
    public ResponseEntity<List<ProductDto>> getAllProducts()
    {
        // . hello i am varshith this is test
        log.info("NATALIE control entered the getAllProducts()");


        List<ProductDto> allproducts = productservice.getAllProducts();
        return new ResponseEntity<>(allproducts , HttpStatus.OK);
    }

    @GetMapping("/get/products/{categoryId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and hasAuthority('PRODUCT_VIEW')")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable Long categoryId)
    {


        List<ProductDto> allproducts = productservice.getAllProductsbyCategory(categoryId);
        return new ResponseEntity<>(allproducts , HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and hasAuthority('PRODUCT_VIEW')")
    public ResponseEntity<List<ProductDto>> searchProductByName(String keyword)
    {


        List<ProductDto> allproducts = productservice.findProductByName('%' + keyword + '%');
        return new ResponseEntity<>(allproducts , HttpStatus.OK);
    }

    @PostMapping("/add/product/{categoryId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN') and hasAuthority('PRODUCT_CREATE')")
    public  ResponseEntity<ProductDto> createProduct(@RequestBody  ProductDto productdto ,@PathVariable Long categoryId)
    {

        ProductDto productdtocreated = productservice.createProduct(productdto , categoryId);
        return new ResponseEntity<>(productdtocreated , HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN') and hasAuthority('PRODUCT_DELETE')")
    public  ResponseEntity<String> deleteProduct(@PathVariable Long productId)
    {

        productservice.deleteProduct(productId);
        return new ResponseEntity<>("success" , HttpStatus.OK);
    }

    @PostMapping("/create/category/")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CATEGORY_CREATE')")
    public  ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categorydto)
    {

        CategoryDto productdtocreated = productservice.createCategory(categorydto );
        return new ResponseEntity<>(productdtocreated , HttpStatus.OK);
    }

    @GetMapping("/get/all/categories")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER') and hasAuthority('CATEGORY_VIEW')")
    public ResponseEntity<List<CategoryDto>> getAllCategories()
    {

        List<ProductCategory> allcategoryentity = catrepo.findAll();
        List<CategoryDto> allcategorydtos=allcategoryentity.stream()
                .map(individualCategoryentity -> modelmapper.map(individualCategoryentity , CategoryDto.class))
                .toList();

        return new ResponseEntity<>(allcategorydtos , HttpStatus.OK);
    }

    @GetMapping("/get/category/byid/{categoryId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER') and hasAuthority('CATEGORY_VIEW')")
    public ResponseEntity<CategoryDto> getCategorybyId(@PathVariable Long categoryId)
    {

        ProductCategory categoryentity = catrepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",""+categoryId));

        CategoryDto categorydto = modelmapper.map(categoryentity , CategoryDto.class);

        return new ResponseEntity<>(categorydto , HttpStatus.OK);
    }

    @GetMapping("/get/product/{productId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER') and hasAuthority('POST_VIEW')")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId)
    {

        ProductDto productdto = productservice.getProductByproductId(productId);
        return new ResponseEntity<>(productdto , HttpStatus.OK);
    }

    @PutMapping("/update/purchasedproduct")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN') and hasAuthority('POST_VIEW')")
    public ResponseEntity<String> updateProduct( @RequestBody List<UpdateProductDto> updateproductdto)
    {

        System.out.println("control entered the product controller");
        String message  = productservice.updateProduct(updateproductdto);
        return new ResponseEntity<>(message , HttpStatus.OK);
    }
}
