package com.example.o2o.dao;

import com.example.o2o.entity.Area;
import com.example.o2o.entity.PersonInfo;
import com.example.o2o.entity.Shop;
import com.example.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ShopDao extends CrudRepository<Shop, Long> {
    Shop save(Shop shop);
    Optional<Shop> findById(Long shopId);


    @Query("SELECT s FROM Shop s WHERE (:userId is null or s.owner.userId = :userId) " +
            "and (:shopCategoryId is null or s.shopCategory.shopCategoryId = :shopCategoryId) " +
            "and (:areaId is null or s.area.areaId = :areaId) " +
            "and (:shopName is null or s.shopName like concat('%', :shopName, '%'))")
    List<Shop> findShopList(@Param("shopCategoryId") Long shopCategoryId,
                            @Param("areaId") Integer areaId,
                            @Param("shopName") String shopName,
                            @Param("userId") Long userId);
}
