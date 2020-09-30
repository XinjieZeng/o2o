package com.example.o2o.service;

import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.Shop;
import java.io.File;

public interface ShopService {
    ShopExecution addShop(Shop shop, File shopImg);
}
