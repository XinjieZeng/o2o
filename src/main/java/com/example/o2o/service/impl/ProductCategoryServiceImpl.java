package com.example.o2o.service.impl;

import com.example.o2o.dao.ProductCategoryDao;
import com.example.o2o.dto.ProductCategoryExecution;
import com.example.o2o.entity.ProductCategory;
import com.example.o2o.enums.ProductCategoryStateEnum;
import com.example.o2o.exceptions.ProductOperationException;
import com.example.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public List<ProductCategory> getProductCategoryList() {
        return productCategoryDao.findAll();
    }

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        List<ProductCategory> productCategoryList = getProductCategoryList();
        return productCategoryList
                .stream()
                .filter(productCategory -> productCategory.getShopId().equals(shopId))
                .collect(Collectors.toList());

    }

    @Override
    public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategories) {
        if (productCategories != null && productCategories.size() > 0) {
            try {
                productCategories.forEach(
                        productCategory -> {
                            int result = productCategoryDao.insertProductCategory(
                                    productCategory.getProductCategoryName(),
                                    productCategory.getPriority(),
                                    productCategory.getCreateTime(),
                                    productCategory.getShopId());

                            if (result <= 0) {
                                throw new ProductOperationException("failure: add new product category");
                            }
                        }
                );
            } catch (Exception e) {
                System.out.println(e.toString());
                return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
            }

            return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategories);

        }

        return new ProductCategoryExecution((ProductCategoryStateEnum.EMPTY_LIST));

    }


    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(Long productCategoryId, Long shopId) {
        //TODO: delete products under the product category with productCategoryId

        try {
            int result = productCategoryDao.deleteProductCategory(productCategoryId, shopId);

            if (result <= 0) {
                throw new ProductOperationException("fail to delte the product category");
            }


        } catch (Exception e) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
        }

        return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
    }
}
