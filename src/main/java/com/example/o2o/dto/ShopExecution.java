package com.example.o2o.dto;

import com.example.o2o.entity.Shop;
import com.example.o2o.enums.ShopStateEnum;

import java.util.List;

public class ShopExecution {

    private int state;
    private String stateInfo;
    private Shop shop;
    private int count;
    private List<Shop> shopList;

    /**
     * if the shop operation is not successful
     * @param shopStateEnum
     */
    public ShopExecution(ShopStateEnum shopStateEnum) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
    }

    /**
     * if the shop operation is successful
     * @param shopStateEnum
     * @param shop
     */
    public ShopExecution(ShopStateEnum shopStateEnum, Shop shop) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shop = shop;
    }

    /**
     * if the operation on a list of shops is successful
     * @param shopStateEnum
     * @param shopList
     */
    public ShopExecution(ShopStateEnum shopStateEnum, List<Shop> shopList) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shopList = shopList;
        this.count = shopList.size();
    }
}
