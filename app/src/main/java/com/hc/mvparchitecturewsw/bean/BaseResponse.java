package com.hc.mvparchitecturewsw.bean;

import java.io.Serializable;

/**
 * 封装服务器返回数据
 */
public class BaseResponse<T> implements Serializable {
    public String code;
    public String message;

    public T data;
    public boolean success() {
        return "1000".equals(code);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
