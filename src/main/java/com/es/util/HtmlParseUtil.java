package com.es.util;

import com.es.model.JdContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description
 * @Author Hexiaoshu
 * @Date 2021/1/9
 * @modify
 */
public class HtmlParseUtil {


    public static List<JdContent> parseJd(String keywords){
        String url="https://search.jd.com/Search?keyword=java";
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url+ URLEncoder.encode(keywords, "utf-8")), 30000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document==null){
            return null;
        }
        Element element = document.getElementById("J_goodsList");
        Elements li = element.getElementsByTag("li");
        List<JdContent> list = new LinkedList<>();
        li.forEach(e->{
            String imgUrl = e.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = e.getElementsByClass("p-price").eq(0).text();
            String title = e.getElementsByClass("p-name").eq(0).text();
            JdContent jdContent = new JdContent().setImg(imgUrl).setPrice(price).setTitle(title);
            list.add(jdContent);
        });
        return list;
    }


}
