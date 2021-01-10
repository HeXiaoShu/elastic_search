package com.es.common;

/**
 * @ClassName BusinessException
 * @Description 自定义异常
 * @Author 何小树
 * @Date 2019/11/8 13:32
 **/
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 509751730847093927L;

    public BusinessException() {}

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
    }

    public BusinessException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BusinessException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
