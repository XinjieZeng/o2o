package com.example.o2o.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ShopStateEnum {
    CHECK(0, "under check"),
    ILLEGAL(-1, "illegal"),
    SUCCESSFUL(2, "successful"),
    UNSUCCESSFUL(3, "unsuccessful"),
    PASS(4, "pass"),
    NULL_SHOP_ID(-1000, "shop id is empty"),
    SHOP_ALREADY_EXIST(-1002, "shop already exists"),
    NULL_SHOP(-1001, "shop is null"),
    INTERNAL_ERROR(-1004, "internal error");

    private final int state;
    private final String stateInfo;

    private ShopStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }


    public static ShopStateEnum stateOf(int state) {
        Optional<ShopStateEnum> enumOptional = Arrays.stream(values())
                .filter(shopStateEnum -> shopStateEnum.getState() == state)
                .findFirst();

        return enumOptional.orElse(null);

    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
