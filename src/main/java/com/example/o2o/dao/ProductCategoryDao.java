package com.example.o2o.dao;

import com.example.o2o.entity.ProductCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductCategoryDao extends CrudRepository<ProductCategory, Long> {
    List<ProductCategory> findAll();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tb_product_category (product_category_name, priority, create_time, shop_id) " +
            "VALUES (:productCategoryName, :priority, :createTime, :shopId) ", nativeQuery = true)
    int insertProductCategory(@Param("productCategoryName") String product_category_name,
                              @Param("priority") Integer priority,
                              @Param("createTime") Date createTime,
                              @Param("shopId") Long shopId);

    @Modifying
    @Transactional
    @Query(value = "delete from tb_product_category where " +
            "product_category_id = :productCategoryId and " +
            "shop_id = :shopId", nativeQuery = true)
    int deleteProductCategory(@Param("productCategoryId") Long productCategoryId,
                              @Param("shopId") Long shopId);


}
