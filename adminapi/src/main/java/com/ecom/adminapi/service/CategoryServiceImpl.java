package com.ecom.adminapi.service;

import com.ecom.adminapi.dto.CategoryDto;
import com.ecom.adminapi.entity.ProductCategory;
import com.ecom.adminapi.exception.ResourceNotFoundException;
import com.ecom.adminapi.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService
{
   @Autowired
   private ModelMapper modelmapper;

   @Autowired
   private CategoryRepository catrepo;


    @Override
    public CategoryDto createCategory(CategoryDto categorydto)
    {
        //1.check if category is already present or not by using name
        //2.convert categorydto to entity and save in repo
        //3.convert savedcategoryentity to dto and return

        ProductCategory categoryentity = modelmapper.map(categorydto , ProductCategory.class);
        ProductCategory savedcaterory = catrepo.save(categoryentity);

        return modelmapper.map(savedcaterory , CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getallCategory()
    {
        List<ProductCategory> allcategoryentity = catrepo.findAll();
        List<CategoryDto> allcategorydtos=allcategoryentity.stream()
                .map(individualCategoryentity -> modelmapper.map(individualCategoryentity , CategoryDto.class))
                .toList();
        return allcategorydtos;
    }

    @Override
    public CategoryDto getCategoryByIdserv(Long categoryId)
    {
        /*
            1.check if category exists or not with id if not throw a exception
            2.if present fetch and return
         */

        ProductCategory categoryentity = catrepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",""+categoryId));

        CategoryDto categorydto = modelmapper.map(categoryentity , CategoryDto.class);
        return categorydto;
    }

    @Override
    public String deleteCategorybyId(Long categoryId)
    {
        ProductCategory categoryentity = catrepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",""+categoryId));
        catrepo.deleteById(categoryId);
        return "success";
    }

    @Override
    public CategoryDto updateCategoryserv(CategoryDto categorydto , Long categoryId)
    {
        ProductCategory categoryentity = catrepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",""+categoryId));

        categoryentity.setActive(categorydto.getActive());
        categoryentity.setCategoryName(categorydto.getCategoryName());

        ProductCategory savedcategoryentity = catrepo.save(categoryentity);

        return modelmapper.map(savedcategoryentity , CategoryDto.class);
    }
}
