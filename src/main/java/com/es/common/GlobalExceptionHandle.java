package com.es.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hexiaoshu
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e, HttpServletRequest request){
        log.error("服务器报错，"+"报错请求路径:"+request.getRequestURI());
        e.printStackTrace();
        return Result.error(e.getMessage(),"system exception");
    }

}