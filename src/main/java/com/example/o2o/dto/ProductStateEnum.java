package com.example.o2o.dto;

public enum ProductStateEnum {
    OFFLINE(-1, "ILLEGAL"),
    DOWN(0, "OFF THE SHELF"),
    SUCCESS(1, "SUCCESS"),
    INNER_ERROR(-1001, "FAIL"),
    EMPTY(-1002, "EMPTY PRODUCT");

    private int state;

    private String stateInfo;

    private ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static ProductStateEnum stateOf(int index) {
        for (ProductStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
