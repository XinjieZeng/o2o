package com.example.o2o.service.impl;

import com.example.o2o.dao.ShopCategoryDao;
import com.example.o2o.entity.ShopCategory;
import com.example.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    private final ShopCategoryDao shopCategoryDao;

    @Autowired
    public ShopCategoryServiceImpl(ShopCategoryDao shopCategoryDao) {
        this.shopCategoryDao = shopCategoryDao;
    }
    @Override
    public List<ShopCategory> getShopCategoryList() {
        return shopCategoryDao.findAll();
    }

    @Override
    public List<ShopCategory> getShopCategoryList(Long parentId) {
        List<ShopCategory> allShops = getShopCategoryList();
        List<ShopCategory> res = new ArrayList<>();
        for (ShopCategory shopCategory : allShops) {
            if (parentId == shopCategory.getParentId()) {
                res.add(shopCategory);
            }
        }

        return res;
//        return res.stream()
//                .filter(shopCategory -> parentId.equals(shopCategory.getParentId()))
//                .collect(Collectors.toList());
    }
}
