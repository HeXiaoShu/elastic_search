package com.es.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description 京东商品数据爬取
 * @Author Hexiaoshu
 * @Date 2021/1/9
 * @modify
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JdContent {

    private String title;
    private String img;
    private String price;

}
