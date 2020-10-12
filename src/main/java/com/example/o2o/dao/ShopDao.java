package com.example.o2o.dao;

import com.example.o2o.entity.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ShopDao extends CrudRepository<Shop, Long> {
    Shop save(Shop shop);
    Optional<Shop> findById(Long shopId);
}
