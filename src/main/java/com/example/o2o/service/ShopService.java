package com.example.o2o.service;

import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName);
    Shop getShopById(Long ShopId);
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName);
    ShopExecution getShopList(Shop shop);
}
