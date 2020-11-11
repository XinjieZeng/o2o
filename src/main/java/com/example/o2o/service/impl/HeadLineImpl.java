package com.example.o2o.service.impl;

import com.example.o2o.dao.HeadlineDao;
import com.example.o2o.entity.Headline;
import com.example.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HeadLineImpl implements HeadLineService {

    @Autowired
    private HeadlineDao headlineDao;

    @Override
    public Headline getHeadLineList(Long id) {
        return headlineDao.findById(id).orElse(null);
    }

    @Override
    public List<Headline> getHeadLineList() {
        return headlineDao.findAll();
    }
}
