package com.example.o2o.dao;

import com.example.o2o.entity.Product;
import com.example.o2o.entity.Shop;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ProductDao extends CrudRepository<Product, Long> {

    Product save(Product product);
    Optional<Product> findById(Long shopId);

    @Override
    void deleteById(Long aLong);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tb_product (product_name, product_desc, img_addr, " +
            "normal_price, promotion_price, priority, create_time, last_edit_time, " +
            "enable_status, product_category_id, shop_id) " +
            "VALUES (:productName, :productDesc, :imgAddr, :normalPrice, :promotionPrice, :priority, " +
            ":createTime, :lastEditTime, :enableStatus, :productCategoryId, :shopId) ", nativeQuery = true)
    int insertProduct(@Param("productName") String productName,
                              @Param("productDesc") String productDesc,
                              @Param("imgAddr") String imgAddr,
                              @Param("normalPrice") String normalPrice,
                              @Param("promotionPrice") String promotionPrice,
                              @Param("priority") Integer priority,
                              @Param("createTime") Date createTime,
                              @Param("lastEditTime") Date lastEditTime,
                              @Param("enableStatus") Integer enableStatus,
                              @Param("productCategoryId") Integer productCategoryId,
                              @Param("shopId") Long shopId);
}
