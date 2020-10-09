package com.example.o2o.service.impl;

import com.example.o2o.dao.ShopDao;
import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.Shop;
import com.example.o2o.enums.ShopStateEnum;
import com.example.o2o.exceptions.ShopOperationException;
import com.example.o2o.service.ShopService;
import com.example.o2o.util.ImageUtil;
import com.example.o2o.util.PathUtil;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {

    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);
    private final ShopDao shopDao;

    @Autowired
    public ShopServiceImpl(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    @Override
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }


        shop.setEnableStatus(0);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());

        shopDao.save(shop);

        if (shopImgInputStream != null) {
            addShopImage(shop, shopImgInputStream, fileName);
            shopDao.save(shop);
        }

        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImage(Shop shop, InputStream shopImgInputStream, String fileName) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String relativeAdd = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
        shop.setShopImg(relativeAdd);
    }
}
