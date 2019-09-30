package com.qsccc.yyjhservice.controller;


import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import com.qsccc.yyjhservice.service.datasource.TDataSourceService;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/datasuorce")
public class DataSourceController {

    @Autowired
    private TDataSourceService tDataSourceService;

    @RequestMapping("/getAll")
    //csv上传
    public  Object getAll() throws IOException {

        List<TDataSource> list_datasource = tDataSourceService.getAll();
//        for(int i = 0; i<list_datasource.size(); i++){
//            Gson gson = new Gson();
//            Map<String, Object> map = new HashMap<String, Object>();
//            //map = gson.fromJson(list_datasource.get(i).toString(), map.getClass());
//
//            //String goodsid=(String) map.get("id");
//            System.out.println(list_datasource.get(i).toString());
//
//        }
        return list_datasource;
    }

//    @RequestMapping("/getAllByPage")
//    //csv上传
//    public  Object getAllByPage(int beginNumber, int limit) throws IOException {
//
//        List<TDataSource> list_datasource = tDataSourceService.getAllByPage(beginNumber,limit);
//        return list_datasource;
//    }
//
//    @RequestMapping("/getAllByPage")
//    //csv上传
//    public  Object getCount() throws IOException {
//
//        int count = tDataSourceService.getCount();
//        return count;
//    }

    @ResponseBody
    @RequestMapping(value="/getAllByBeginNumber")
    public Map<String,Object> getAllByBeginNumber(int pageSize,int pageNumber){
        // 查看全部数据执行后端分页查询
        Map<String,Object> queryMap = new HashMap<String,Object>();
        if (pageNumber <= 1){
            pageNumber = 1;
        }
        int beginNumber = (pageNumber - 1)* pageSize;
        queryMap.put("beginNumber", beginNumber);
        queryMap.put("limit", pageSize);
        List<TDataSource> list = tDataSourceService.getAllByPage(queryMap);
        int total = tDataSourceService.getCount();
        Map<String,Object> responseMap = new HashMap<String,Object>();
        //key需要与js中 dataField对应，bootStrap默认值为rows
        responseMap.put("rows", list);
        // 需要返回到前台，用于计算分页导航栏
        responseMap.put("total", total);
        System.out.println(responseMap);
        return responseMap;
    }

    @RequestMapping("/delTDatasourceById/{id}")
    //csv上传
    public  Object delTDatasourceById(@PathVariable("id") Integer id) throws IOException {

        boolean count = tDataSourceService.delTDatasourceById(id);

        return count;
    }

    @RequestMapping("/batchDelById")
    //csv上传
    public  Object batchDelById(@RequestBody List<Integer> ids) throws IOException {

        boolean count = tDataSourceService.batchDelById(ids);

        return count;
    }

    @RequestMapping("/updTDatasurceById")
    //csv上传
    public  Object updTDatasurceById(@RequestBody TDataSource td) throws IOException {

        td.setUpdatetime(new Date());
        boolean count = tDataSourceService.updTDatasurceById(td);

        return count;
    }

    @RequestMapping("/search")
    public  Object search(String type,String content) throws IOException {

        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("content",content);
        List<TDataSource> list_td = tDataSourceService.search(map);

        return list_td;
    }

}
