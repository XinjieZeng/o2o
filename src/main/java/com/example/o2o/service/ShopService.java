package com.example.o2o.service;

import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.Shop;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName);
}
