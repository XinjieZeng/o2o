package com.example.o2o.service;

import com.example.o2o.entity.Headline;

import java.util.List;

public interface HeadLineService {
    Headline getHeadLineList(Long id);
    List<Headline> getHeadLineList();

}
