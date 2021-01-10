package com.es.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Description json转化工具类
 * @Author Hexiaoshu
 * @Date 2021/1/9
 * @modify
 */
public class JsonUtil {


    /**
     * json字符串转对象
     * @param json json字符串
     * @param c    对象class字节码
     * @param <T> T
     * @return T
     */
    public static <T> T toObj(String json, Class<T> c){
        return JSON.toJavaObject(JSONObject.parseObject(json),c);
    }

    /**
     * json转 List
     * @param json json字符串
     * @param c    对象class字节码
     * @param <T>  T
     * @return List
     */
    public static  <T> List toList(String json, Class<T> c){
        return JSONArray.parseArray(json, c);
    }

    /**
     * Object转json
     * @param o obj
     * @return String
     */
    public static String toStr(Object o){
        return JSON.toJSONString(o);
    }


}
