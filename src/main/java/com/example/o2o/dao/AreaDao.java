package com.example.o2o.dao;
import com.example.o2o.entity.Area;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AreaDao extends CrudRepository<Area, Integer> {
    List<Area> findAll();
}
