package com.example.o2o.dto;

public class Result<T> {
    private boolean success;
    private T data;
    private String errMsg;
    private int errorCode;

    public Result() {}

    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public Result(boolean success, int errorCode, String errMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }


    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", data=" + data +
                ", errMsg='" + errMsg + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
