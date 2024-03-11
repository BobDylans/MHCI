package com.example.mhcidemo.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result<T> {
    // getter和setter方法
    private int status; // 状态码，例如200表示成功，400表示请求错误等
    private String message; // 响应消息，用于给出成功或错误的简短描述
    private T data; // 泛型数据，可以是任何类型的数据，用于存放实际需要发送给前端的数据
    private String token; // 新增的token字段

    // 构造方法，用于创建不带token的Result实例
    public Result(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 构造方法，用于创建带token的Result实例
    public Result(int status, String message, T data, String token) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    // 快捷静态方法用于创建Result实例
    // 现有的success方法，返回不带token的结果
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success", data);
    }

    // 新增的重载success方法，返回带token的结果
    public static <T> Result<T> success(T data, String token) {
        return new Result<>(200, "Success", data, token);
    }

    // 创建错误响应的静态方法
    public static <T> Result<T> error(int status, String message) {
        return new Result<>(status, message, null, null);
    }
}

