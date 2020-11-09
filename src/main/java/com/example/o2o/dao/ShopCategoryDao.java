package com.example.o2o.dao;
import com.example.o2o.entity.ShopCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShopCategoryDao extends CrudRepository<ShopCategory, Long>{
    List<ShopCategory> findAll();

}
