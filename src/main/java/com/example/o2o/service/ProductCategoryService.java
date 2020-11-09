package com.example.o2o.service;

import com.example.o2o.dto.ProductCategoryExecution;
import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.ProductCategory;
import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList();
    List<ProductCategory> getProductCategoryList(Long shopId);
    ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategories);
    ProductCategoryExecution deleteProductCategory(Long productCategoryId, Long shopId);

}
