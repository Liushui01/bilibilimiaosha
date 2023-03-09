package com.example.javamiaosha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum RespBeanEnum {
    //通用
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    //登录
    LOGIN_ERROR(50021,"用户名或密码错误"),
    USER_NULL(50023,"用户不存在"),
    BIND_ERROR(50024,"参数校验异常"),
    EMPTY_STOCK(500500,"库存不足"),
    REPEATE_ERROR(500501,"该商品每人限购一件");
    private final Integer code;
    private final String message;
}
