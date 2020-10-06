package com.example.o2o.dao;

import com.example.o2o.entity.ShopCategory;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ShopCategoryDao extends CrudRepository<ShopCategory, Long>{
    List<ShopCategory> findAll();
}
