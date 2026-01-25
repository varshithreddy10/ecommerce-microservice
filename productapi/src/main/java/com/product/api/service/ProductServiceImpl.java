package com.product.api.service;

import com.product.api.dto.CategoryDto;
import com.product.api.dto.ProductDto;
import com.product.api.dto.UpdateProductDto;
import com.product.api.entity.ProductCategory;
import com.product.api.entity.ProductEntity;
import com.product.api.exception.ResourceNotFoundException;
import com.product.api.repository.CategoryRepository;
import com.product.api.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService
{
    @Autowired
    private ProductRepository productrepo;

    @Autowired
    private CategoryRepository catrepo;

    @Autowired
    private ModelMapper modelmapper;

    @Override
    public List<ProductDto> getAllProducts()
    {
        System.out.println("control entered getAllProducts() ");
        List<ProductEntity> allproductentities = productrepo.findAll();
        List<ProductDto> allproductdtos = allproductentities.stream()
                .map(individualproductentity->modelmapper.map(individualproductentity,ProductDto.class))
                .toList();
        return allproductdtos;
    }

    @Override
    public List<ProductDto> getAllProductsbyCategory(Long categoryId)
    {
        System.out.println("control entered getAllProductsbyCategory() "+categoryId);
        ProductCategory productcategory = catrepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId" , ""+categoryId));
        List<ProductEntity>  allproductentity = productrepo.findByCategoryCategoryId(categoryId);

        allproductentity.forEach(System.out::println);

        List<ProductDto> allproductsdto = allproductentity.stream()
                .map(individualproductentity -> modelmapper.map(individualproductentity , ProductDto.class))
                .toList();

        System.out.println("printing all products of the category");
        allproductsdto.forEach(System.out::println);

        return allproductsdto;

    }

    @Override
    public ProductDto createProduct(ProductDto productdto , Long categoryId)
    {
        System.out.println("control entered createproduct() "+productdto);
        ProductCategory productcategory = catrepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId" , ""+categoryId));

        ProductEntity productentity = modelmapper.map(productdto , ProductEntity.class);
        productentity.setCategory(productcategory);
        ProductEntity savedproductentity =productrepo.save(productentity);

        ProductDto productdtosaved = modelmapper.map(savedproductentity , ProductDto.class);

        System.out.println("savedproduct =  "+productdtosaved);

        return productdtosaved;
    }

    @Override
    public void deleteProduct(Long productId)
    {
        ProductCategory productcategory = catrepo.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product","productId" , ""+productId));
        productrepo.deleteById(productId);

    }

    @Override
    public List<ProductDto> findProductByName(String keyword)
    {
        System.out.println("control entered findProductByName() "+keyword);

        List<ProductEntity> allproducts = productrepo.findByNameLikeIgnoreCase('%' + keyword + '%');

        allproducts.forEach(System.out::println);

        List<ProductDto> allproductsdto = allproducts.stream()
                .map(individualentity -> modelmapper.map(individualentity , ProductDto.class))
                .toList();

        System.out.println("printing all prodycts by name ");
        allproductsdto.forEach(System.out::println);

        return allproductsdto;

    }

    @Override
    public CategoryDto createCategory(CategoryDto categorydto)
    {
        ProductCategory categoryentity = modelmapper.map(categorydto , ProductCategory.class);
        ProductCategory categoryentitysaved = catrepo.save(categoryentity);

        return modelmapper.map(categoryentitysaved , CategoryDto.class);
    }

    @Override
    public ProductDto getProductByproductId(Long productId)
    {
        ProductEntity productentity = productrepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product","productId",""+productId));
        return modelmapper.map(productentity , ProductDto.class);
    }

    @Override
    public String updateProduct(List<UpdateProductDto> updateproductdtos)
    {
        System.out.println("updateProduct() called, list = " + updateproductdtos);


        updateproductdtos.forEach(individualproductentity->{

            System.out.println("control entered  individualproductentity foreach");

            Long productId = individualproductentity.getProductId();
            ProductEntity productentity = productrepo.findById(productId)
                    .orElseThrow(()->new ResourceNotFoundException("product","productId",""+productId));

            Integer existingquantity = productentity.getUnitsInStock();

            Integer quantity = individualproductentity.getQuantity();
            String updateType = individualproductentity.getUpdateType();

            if(updateType.equals("ADD"))
            {
                productentity.setUnitsInStock(existingquantity + quantity);
            }
            else if(updateType.equals("REMOVE"))
            {

                productentity.setUnitsInStock(existingquantity - quantity);
                System.out.println("control entered REMOVE and quantity is = "+(existingquantity - quantity));
            }

            productrepo.save(productentity);

        });

        System.out.println("success");

        return "success";
    }
}
