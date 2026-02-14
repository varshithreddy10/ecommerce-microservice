package com.ecom.adminapi.service;

import com.ecom.adminapi.dto.CategoryDto;

import java.util.List;

public interface CategoryService 
{
    // hello this is test
    
    public CategoryDto createCategory(CategoryDto categorydto);

    public List<CategoryDto> getallCategory();

    CategoryDto getCategoryByIdserv(Long categoryId);

    String deleteCategorybyId(Long categoryId);

    CategoryDto updateCategoryserv(CategoryDto categorydto , Long categoryId);
}
