package com.es.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author heyonghao
 * @Description: 统一返回结果集
 * @date 2019/7/49:52
 */
@Getter
@Setter
@ToString
public class Result implements Serializable {

    private static final long serialVersionUID = -3731997440028965533L;

    private Result() {}
    private int code;
    private String message;
    private Object data;

    public static Result ok(String message) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(null);
        return result;
    }
    public static Result ok(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }
    public static Result error(String message, Object data) {
        Result result = new Result();
        result.setCode(ResultCode.FAIL.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    public static Result error(String message) {
        Result result = new Result();
        result.setCode(ResultCode.FAIL.getCode());
        result.setMessage(message);
        result.setData(null);
        return result;
    }
    public static Result error(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.FAIL.getCode());
        result.setMessage("");
        result.setData(data);
        return result;
    }

}
