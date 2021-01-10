package com.es.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.SortedDocValuesField;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description ElasticSearch 7.10.0 工具类
 * @Author Hexiaoshu
 * @Date 2021/1/5
 * @modify
 */
@Slf4j
@Component
public class EsUtil {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     * @param index 索引名称
     * @return Boolean
     */
    public Boolean createIndex(String index){
        boolean flag=false;
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            flag=response.isAcknowledged();
        }catch (IOException e){
            log.error("es 连接异常");
        }
        return flag;
    }

    /**
     * 判断索引是否存在
     * @param index 索引名称
     * @return Boolean
     */
    public Boolean existIndex(String index){
        boolean flag=false;
        try {
            GetIndexRequest request = new GetIndexRequest(index);
            flag= restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        }catch (IOException e){
            log.error("es 连接异常");
        }
        return flag;
    }

    /**
     * 删除索引
     * @param index 索引名称
     * @return Boolean
     */
    public Boolean delIndex(String index){
        boolean flag=false;
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(index);
            flag=restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT).isAcknowledged();
        }catch (IOException e){
            log.error("es 连接异常");
        }
        return flag;
    }

    /**
     * 添加文档
     * @param index 索引库
     * @param o  对象
     * @param id 文档id
     * @return status
     */
    public Boolean addDoc(String index,Object o,String id){
        boolean flag=false;
        IndexRequest request = new IndexRequest(index);
        request.id(id);
        request.source(JsonUtil.toStr(o), XContentType.JSON);
        try {
            IndexResponse response = restHighLevelClient.index(request,RequestOptions.DEFAULT);
            flag= 201 == response.status().getStatus();
        } catch (IOException e) {
            log.error("es 连接异常");
        }
        return flag;
    }

    /**
     * 批量添加文档
     * @param index 索引
     * @param list  集合
     * @return Boolean
     */
    public <T> Boolean addDocs(String index, List<T> list){
        if (list==null){
            return null;
        }
        boolean flag=false;
        BulkRequest request = new BulkRequest();
        list.forEach(e->{
            request.add(new IndexRequest(index).source(JsonUtil.toStr(e),XContentType.JSON));
        });
        try {
            BulkResponse responses = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            flag=!responses.hasFailures();
        } catch (IOException e) {
            log.error("es 连接异常");
        }
        return flag;
    }



    /**
     * 更新文档
     * @param index 索引
     * @param o     对象
     * @param id    文档id
     * @return Boolean
     */
    public Boolean updateDoc(String index,Object o,String id){
        boolean flag=false;
        UpdateRequest request = new UpdateRequest(index,id).doc(JsonUtil.toStr(o), XContentType.JSON);
        try {
            UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
            flag = 200==response.status().getStatus();
        } catch (IOException e) {
            log.error("es 连接异常");
        }
        return flag;
    }

    /**
     * 删除文档
     * @param index 索引
     * @param docId 文档id
     * @return Boolean
     */
    public Boolean delDoc(String index,String docId){
        boolean flag=false;
        DeleteRequest request = new DeleteRequest(index,docId);
        try {
            DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            flag=200==response.status().getStatus();
        } catch (IOException e) {
            log.error("es 连接异常");
        }
        return flag;
    }

    /**
     * 根据id获取文档
     * @param index 索引库
     * @param docId 文档id
     * @return GetResponse
     */
    public GetResponse getDocById(String index,String docId){
        GetResponse response=null;
        GetRequest request = new GetRequest(index,docId);
        try {
             response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es 连接异常");
        }
        return response;
    }

    /**
     * 文档精确查询
     * @param index 索引
     * @param filed 属性
     * @param value 值
     * @param page 当前页
     * @param size 每页数量
     * @return SearchResponse
     */
    public List<Map<String,Object>> searchDocTerm(String index,String filed,String value,String sortFiled,String sort,Integer page,Integer size){
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(filed);
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        SearchRequest request = setQuery(index, QueryBuilders.termQuery(filed,value),highlightBuilder,sortFiled,sort,page,size);
        return getData(request,filed);
    }

    /**
     * 文档全查询
     * @param index 索引
     * @param page 当前页
     * @param size 每页数量
     * @return SearchResponse
     */
    public List<Map<String,Object>> searchDocAll(String index,Integer page,Integer size){
        SearchRequest request = setQuery(index, QueryBuilders.matchAllQuery(),null,null,null,page,size);
        return getData(request,null);
    }

    /**
     * 获取sourceMap数据
     * @param request SearchRequest
     * @return List<Map<String, Object>>
     */
    private List<Map<String, Object>> getData(SearchRequest request,String highlightName) {
        List<Map<String,Object>> list = new LinkedList<>();
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            Arrays.stream(hits).forEach(e->{
                Map<String, Object> sourceAsMap = e.getSourceAsMap();
                if (highlightName!=null){
                    HighlightField highlightField = e.getHighlightFields().get(highlightName);
                    if (highlightField!=null){
                        Text[] fragments = highlightField.fragments();
                        AtomicReference<String> highlight= new AtomicReference<>(highlightName);
                        Arrays.stream(fragments).forEach(h-> highlight.updateAndGet(v -> v + h));
                        sourceAsMap.put(highlightName,highlight.get());
                    }
                }
                list.add(sourceAsMap);
            });
        } catch (IOException e) {
            log.error("es 连接异常");
        }
        return list;
    }

    /**
     * 查询设置
     * @param index 索引
     * @param queryBuilder 查询抽象类
     * @return SearchRequest
     */
    private SearchRequest setQuery(String index,AbstractQueryBuilder queryBuilder,HighlightBuilder highlightBuilder,String sortFiled,String sort,Integer page,Integer size){
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(page==null?1:page);
        sourceBuilder.size(size==null?10:size);
        sourceBuilder.query(queryBuilder);
        if (sortFiled!=null){
            sourceBuilder.sort(sortFiled,"desc".equals(sort)?SortOrder.DESC:SortOrder.ASC );
        }
        if (highlightBuilder!=null){
            sourceBuilder.highlighter(highlightBuilder);
        }
        request.source(sourceBuilder);
        return  request;
    }



}
