package com.example.o2o.dao;

import com.example.o2o.entity.Headline;
import com.example.o2o.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeadlineDao extends CrudRepository<Headline, Long> {
    Headline save(Headline headline);
    Optional<Headline> findById(Long id);

    @Query("SELECT s FROM Headline s")
    List<Headline> findAll();
}
