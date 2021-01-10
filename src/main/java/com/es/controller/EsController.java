package com.es.controller;

import com.es.common.Result;
import com.es.model.JdContent;
import com.es.model.User;
import com.es.util.EsUtil;
import com.es.util.HtmlParseUtil;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Hexiaoshu
 * @Date 2021/1/5
 * @modify
 */
@RequestMapping("/es")
@RestController
public class EsController {
    @Resource
    private EsUtil esUtil;

    @GetMapping("/create_index")
    public Result test(String index) {
        Boolean isSuccess = esUtil.createIndex(index);
        return Result.ok(isSuccess);
    }

    @GetMapping("/exist_index")
    public Result existIndex(String index) {
        Boolean isSuccess = esUtil.existIndex(index);
        return Result.ok(isSuccess);
    }

    @GetMapping("/addDoc")
    public Result addDoc(){
        User user = new User().setId(12321L).setName("小千");
        boolean status = esUtil.addDoc("xiaoshu", user, user.getId().toString());
        return Result.ok(status);
    }

    @GetMapping("/addDocs")
    public Result addDocs(){
        User user = new User().setId(1L).setName("小白");
        User user2 = new User().setId(2L).setName("小红");
        User user3 = new User().setId(3L).setName("小蓝");
        List<Object> users = Arrays.asList(user, user2, user3);
        Boolean status = esUtil.addDocs("xiaoshu", users);
        return Result.ok(status);
    }

    @GetMapping("/getDoc")
    public Result getDoc(){
        GetResponse response = esUtil.getDocById("xiaoshu", "12321");
        return Result.ok(response);
    }

    @GetMapping("/searchDocTerm")
    public Result searchDocTerm(String name,Integer page,Integer size){
        List<Map<String, Object>> list = esUtil.searchDocTerm("jd-goods", "title", name,null,null,page, size);
        return Result.ok(list);
    }

    @GetMapping("/searchDocAll")
    public Result searchDocAll(Integer page,Integer size){
        List<Map<String, Object>> list = esUtil.searchDocAll("jd-goods", page, size);
        return Result.ok(list);
    }

    @GetMapping("/inEs")
    public Result inEs(String keywords){
        List<JdContent> list = HtmlParseUtil.parseJd(keywords);
        Boolean flag = esUtil.addDocs("jd-goods", list);
        return Result.ok(flag);
    }

    @GetMapping("/updateDoc")
    public Result updateDoc(){
        User user = new User().setId(12321L).setName("媳妇儿");
        Boolean flag = esUtil.updateDoc("xiaoshu", user, "12321");
        return Result.ok(flag);
    }

    @GetMapping("/delDoc")
    public Result delDoc(){
        Boolean flag = esUtil.delDoc("xiaoshu", "12321");
        return Result.ok(flag);
    }
}
