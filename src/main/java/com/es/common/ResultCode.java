package com.es.common;

/**
 * @author heyonghao
 * @Title: Result
 * @ProjectName xlkoffical
 * @Description: 定义统一状态码
 * @date 2019/7/310:25
 */
public enum ResultCode {
    //未知错误
    UNKNOWN_ERROR(1, "未知错误"),
    //成功
    SUCCESS(200, "请求成功"),
    //失败
    FAIL(500, "请求失败");

    private final  Integer code;
    private final  String message;
    ResultCode(Integer code, String message) {
        this.code=code;
        this.message = message;
    }
    public Integer getCode() {return code;}
    public String getMessage() {return message;}

}
