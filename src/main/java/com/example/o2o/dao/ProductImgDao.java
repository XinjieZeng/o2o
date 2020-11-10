package com.example.o2o.dao;

import com.example.o2o.entity.ProductImg;
import com.example.o2o.entity.Shop;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductImgDao extends CrudRepository<ProductImg, Long> {

    ProductImg save(ProductImg productImg);

    @Override
    Iterable<ProductImg> findAllById(Iterable<Long> iterable);

    @Override
    void deleteById(Long aLong);

    @Query("SELECT s FROM ProductImg s WHERE s.productId = :productId")
    List<ProductImg> findProductImgList(@org.apache.ibatis.annotations.Param("productId") Long productId);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tb_product_img (img_addr, img_desc, priority, create_time, product_id) " +
            "VALUES (:imgAddr, :imgDesc, :priority, :createTime, :productId) ", nativeQuery = true)
    int insertProduct(@Param("imgAddr") String imgAddr,
                      @Param("imgDesc") String imgDesc,
                      @Param("priority") Integer priority,
                      @Param("createTime") Date createTime,
                      @Param("productId") Integer productId);
}
