package com.example.o2o.service.impl;

import com.example.o2o.dao.ShopDao;
import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.*;
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
import java.util.List;
import java.util.Optional;

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

    @Override
    public Shop getShopById(Long shopId) {
        return Optional.of(shopDao.findById(shopId)).get().orElse(new Shop());
    }

    @Override
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }

        Shop tempShop = getShopById(shop.getShopId());

        if (shopImgInputStream != null &&  !"".equals(fileName)) {
            if (tempShop.getShopImg() != null) {
                ImageUtil.deleteFileOrPath(tempShop.getShopImg());
            }
            addShopImage(shop, shopImgInputStream, fileName);
            tempShop.setShopImg(shop.getShopImg());
        }
        tempShop.setShopName(shop.getShopName());
        tempShop.setArea(shop.getArea());
        tempShop.setShopDesc(shop.getShopDesc());
        tempShop.setPhone(shop.getPhone());
        tempShop.setShopAddr(shop.getShopAddr());
        tempShop.setLastEditTime(new Date());
        shopDao.save(tempShop);

        return new ShopExecution(ShopStateEnum.SUCCESSFUL);
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition) {
        Long shopCategoryId = null;
        Integer areaId = null;
        String shopName = shopCondition.getShopName();
        Integer enableStatus = shopCondition.getEnableStatus();
        Long ownerId = null;

        if (shopCondition.getShopCategory() != null) {
            shopCategoryId = shopCondition.getShopCategory().getShopCategoryId();
        }

        if (shopCondition.getArea() != null) {
            areaId = shopCondition.getArea().getAreaId();
        }

        if (shopCondition.getOwner() != null) {
            ownerId = shopCondition.getOwner().getUserId();
        }

        List<Shop> shopList = shopDao.findShopList(shopCategoryId, areaId, shopName, ownerId);

        if(shopList == null) {
            return new ShopExecution(ShopStateEnum.INTERNAL_ERROR);
        }
        return new ShopExecution(ShopStateEnum.SUCCESSFUL, shopList);
    }


    private void addShopImage(Shop shop, InputStream shopImgInputStream, String fileName) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String relativeAdd = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
        shop.setShopImg(relativeAdd);
    }
}
