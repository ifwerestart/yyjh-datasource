package com.qsccc.yyjhservice.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qsccc.tools.YYJHTools;
import com.qsccc.yyjhservice.domain.datasource.RedisDatabase;
import com.qsccc.yyjhservice.domain.datasource.TDataSource;

import com.qsccc.yyjhservice.enumeration.DataSourceEnum;

import com.qsccc.yyjhservice.service.datasource.RedisDatabaseService;
import com.qsccc.yyjhservice.service.datasource.TDataSourceService;
import com.qsccc.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/redis")
public class RedisSourceController {

    @Autowired
    private TDataSourceService tDataSourceService;
    @Autowired
    private RedisDatabaseService redisDatabaseService;
    @Autowired
    private HttpSession httpSession;


    //Redis连接测试
    @PostMapping(value = "/redisTest")
    public Object redisTest(@RequestBody String redis_datasource) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.EXCEPTION.getCode());
        result.setMsg(DataSourceEnum.EXCEPTION.getMsg());

        ObjectMapper om = new ObjectMapper();
        Map<String,Object> datasource = om.readValue(redis_datasource,new TypeReference<Map<String,Object>>(){});

        String host = datasource.get("url").toString();
        int port = Integer.parseInt(datasource.get("port").toString());
        String password = datasource.get("password").toString();
        String username = datasource.get("username").toString();

//        //判断是否有相同的数据源信息，有则无法配置
//        TDatasource tDatasource = new TDatasource();
//        tDatasource.setPassword(password);
//        tDatasource.setUrl(host);
//        tDatasource.setPort(port);
//        tDatasource.setUsername(username);
//
//        boolean count =  tDatasourceService.findEqualTDatasource(tDatasource);
//        System.out.println(count);
//        if(count == true){
//            result.setCode(DatasourceEnum.FAIL.getCode());
//            result.setMsg(DatasourceEnum.FAIL.getMsg());
//            return result;
//        }
        //连接本地的redis
        try {
            Jedis jedis = new Jedis(host,port);
            jedis.auth(password);
            System.out.println("连接本地的Redis服务器成功");
            //查看服务是否运行
            if(jedis.ping().equals("PONG")){
                System.out.println("服务正在运行：" + jedis.ping());

//                TDatasource redis_data = new TDatasource();
//                redis_data.setType("redis");
//                redis_data.setUrl(host);
//                redis_data.setPort(port);
//                redis_data.setPassword(password);
//                redis_data.setUsername(username);
//                Date date = new Date();
//                redis_data.setCreatetime(date);
//                boolean flag = tDatasourceService.addTDatasource(redis_data);
//                if (flag) {
                    result.setCode(DataSourceEnum.SUCCESS.getCode());
                    result.setMsg(DataSourceEnum.SUCCESS.getMsg());
//                } else {

//                }

                jedis.close();
            }
        } catch (Exception e) {
            result.setCode(DataSourceEnum.FAIL.getCode());
            result.setMsg(DataSourceEnum.FAIL.getMsg());

            System.out.println("Redis服务器连接失败" );
        }
        System.out.println(result.getCode());
        return result;
    }


    //Redis上传
    @PostMapping(value = "/redisUpload")
    public Object redisUpload(@RequestBody String redis_datasource) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.EXCEPTION.getCode());
        result.setMsg(DataSourceEnum.EXCEPTION.getMsg());

        ObjectMapper om = new ObjectMapper();
        Map<String,Object> datasource = om.readValue(redis_datasource,new TypeReference<Map<String,Object>>(){});

        String host = datasource.get("url").toString();
        int port = Integer.parseInt(datasource.get("port").toString());
        String password = datasource.get("password").toString();
        String username = datasource.get("username").toString();


        //连接本地的redis
        try {
            Jedis jedis = new Jedis(host,port);
            jedis.auth(password);
            System.out.println("连接本地的Redis服务器成功");
            //查看服务是否运行
            if(jedis.ping().equals("PONG")){
                System.out.println("服务正在运行：" + jedis.ping());
                jedis.close();
            }
        } catch (Exception e) {
            result.setCode(DataSourceEnum.FAIL.getCode());
            result.setMsg(DataSourceEnum.FAIL.getMsg());
            System.out.println("Redis服务器连接失败" );
            return result;
        }

        //判断是否有相同的数据源信息，有则无法配置
        TDataSource tDatasource = new TDataSource();
        tDatasource.setPassword(password);
        tDatasource.setUrl(host);
        tDatasource.setPort(port);
        tDatasource.setUsername(username);
        String userId = httpSession.getAttribute("username").toString();
        tDatasource.setUserId(userId);

        boolean count =  tDataSourceService.findEqualTDatasource(tDatasource);
        System.out.println(count);
        if(count == true){
            result.setCode(DataSourceEnum.DUPLICATION.getCode());
            result.setMsg(DataSourceEnum.DUPLICATION.getMsg());
            return result;
        }


                TDataSource redis_data = new TDataSource();
                redis_data.setType("redis");
                redis_data.setUrl(host);
                redis_data.setPort(port);
                redis_data.setPassword(password);
                redis_data.setUsername(username);
                Date date = new Date();
                redis_data.setCreatetime(date);
                redis_data.setUserId(userId);
                boolean flag = tDataSourceService.addTDatasource(redis_data);
                if (flag) {
                    result.setCode(DataSourceEnum.SUCCESS.getCode());
                    result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                } else {
                    result.setCode(DataSourceEnum.FAIL.getCode());
                    result.setMsg(DataSourceEnum.FAIL.getMsg());
                }



        System.out.println(result.getCode());
        return result;
    }


   // Redis预览(没有用到，直接忽略)
    @RequestMapping(value = "/previewRedis")
    public Object previewRedis(@RequestParam("redis_datasource") String redis_datasource) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.EXCEPTION.getCode());
        result.setMsg(DataSourceEnum.EXCEPTION.getMsg());

        ObjectMapper om = new ObjectMapper();
        Map<String,Object> datasource = om.readValue(redis_datasource,new TypeReference<Map<String,Object>>(){});

        String host = datasource.get("url").toString();
        int port = Integer.parseInt(datasource.get("port").toString());
        String password = datasource.get("password").toString();

        //连接本地的redis
        try {
            Jedis jedis = new Jedis(host,port);
            jedis.auth(password);
            System.out.println("连接本地的Redis服务器成功");
            //查看服务是否运行
            if(jedis.ping().equals("PONG")){
                //database存放不为空的数据库号
                ArrayList database = new ArrayList();
                System.out.println("服务正在运行：" + jedis.ping());
                //获取不为空的数据库号
                for(int i=0;i<16;i++){
                    jedis.select(i);
                    if(jedis.dbSize()!=0)
                        database.add(i);
                }
                //ArrayList转Array（databases存的数据还是数据库号）
                Integer[] databases = new Integer[database.size()];//当泛型为Integer时，需要
                databases = (Integer[])database.toArray(databases);      //以Integer类来作为数组基本元素,否则输出会报错。

                //redis_data存放每个库的数据
                List<HashMap<String,Object>> redis_data = new ArrayList<>();

                //获取每个不为空的库的所有key-value
                for(int i=0;i<databases.length;i++){
                    HashMap<String,Object> base = new HashMap<>();       //存每个库信息
                    HashMap<String,Object> base_data = new HashMap<>();    //存每个库数据信息
                    base.put("base_number",i);       //每个库号
                    //选择库
                    jedis.select(databases[i]);
                    //获取当前库号所有的key
                    Set<String> keys = jedis.keys("*");
                    //获取每个key的value
                    for (String key : keys) {
                        String type = jedis.type(key);
                        //判断value类型
                        if(type.equals("string")){
                            Object value = jedis.get(key);
                            base_data.put(key,value);
                        }else if(type.equals("list")){
                            Object value = jedis.lrange(key, 0, -1);
                            base_data.put(key,value);
                        }else if(type.equals("set")){
                            Object value = jedis.smembers("key");
                            base_data.put(key,value);
                        }else if(type.equals("zset")){
                            Object value = jedis.zrange("key",0,-1);
                            base_data.put(key,value);
                        }else if(type.equals("hash")){
                            Object value = jedis.hvals(key);
                            base_data.put(key,value);
                        }

                    }
                    base.put("base_data",base_data);
                    redis_data.add(base);

                }

                jedis.close();

                result.setCode(DataSourceEnum.SUCCESS.getCode());
                result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                result.setPayload(redis_data);

            }
        } catch (Exception e) {
            result.setCode(DataSourceEnum.FAIL.getCode());
            result.setMsg(DataSourceEnum.FAIL.getMsg());
            System.out.println("Redis服务器连接失败" );
        }

        return result;
    }


    // Redis点击"入库"按钮  获取非空数据库号和已经入库的数据库号
    @GetMapping(value = "/getBaseNumber/{id}")
    public Object getBaseNumber(@PathVariable("id") Integer id) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.EXCEPTION.getCode());
        result.setMsg(DataSourceEnum.EXCEPTION.getMsg());

        TDataSource tDatasource =  tDataSourceService.findTDatasourceById(id);
        String host = tDatasource.getUrl();
        int port = tDatasource.getPort();
        String password = tDatasource.getPassword();
        String username = tDatasource.getUsername();
        //连接本地的redis
        try {
            Jedis jedis = new Jedis(host,port);
            jedis.auth(password);
            System.out.println("连接本地的Redis服务器成功");
            //查看服务是否运行
            if(jedis.ping().equals("PONG")){
                //list存放两个list，一个是非空库号，一个是非空但是mysql中存在的库号
                Map<String,Integer[]> list = new HashMap<>();
                //database存放不为空的数据库号
                ArrayList database = new ArrayList();
                ArrayList exist_database = new ArrayList();
                System.out.println("服务正在运行：" + jedis.ping());
                //获取不为空的数据库号
                for(int i=0;i<16;i++){
                    jedis.select(i);
                    if(jedis.dbSize()!=0){
                        database.add(i);
                        //判断库号在mysql中是否存在
                        String tableName = username+"_redis_db"+i;
                        if(redisDatabaseService.existTable(tableName)){
                            exist_database.add(i);
                        }
                    }
                }

                //ArrayList转Array（databases存的数据还是数据库号）
                Integer[] databases = new Integer[database.size()];//当泛型为Integer时，需要
                databases = (Integer[])database.toArray(databases);      //以Integer类来作为数组基本元素,否则输出会报错。
                list.put("all_database",databases);

                Integer[] exist_databases = new Integer[exist_database.size()];
                exist_databases = (Integer[])exist_database.toArray(exist_databases);
                list.put("exist_database",exist_databases);

                jedis.close();

                result.setCode(DataSourceEnum.SUCCESS.getCode());
                result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                result.setPayload(list);

            }
        } catch (Exception e) {
            result.setCode(DataSourceEnum.FAIL.getCode());
            result.setMsg(DataSourceEnum.FAIL.getMsg());
            System.out.println("Redis服务器连接失败" );
        }

        return result;
    }



    // Redis点击"执行入库"按钮  实现入库
    @GetMapping(value = "/insertRedis/{id}/{alias}")
    public Object insertRedis(@PathVariable("id") Integer id,@PathVariable("alias") String alias) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.EXCEPTION.getCode());
        result.setMsg(DataSourceEnum.EXCEPTION.getMsg());

        TDataSource tDatasource =  tDataSourceService.findTDatasourceById(id);
        String host = tDatasource.getUrl();
        int port = tDatasource.getPort();
        String password = tDatasource.getPassword();
        String username = tDatasource.getUsername();

        String[] base_number = alias.split(","); // 用,分割

        //连接本地的redis
        try {
            Jedis jedis = new Jedis(host,port);
            jedis.auth(password);
            System.out.println("连接本地的Redis服务器成功");
            //查看服务是否运行
            if(jedis.ping().equals("PONG")){

                //获取每个不为空的库的所有key-value
                for(int i=0;i<base_number.length;i++){
                    //创建表
                    String tableName = username+"_redis_db"+Integer.parseInt(base_number[i]);
//                    if(redisDatabaseService.existTable(tableName)){
//                        redisDatabaseService.dropTable(tableName);
//                    }
//
                    redisDatabaseService.createTable(tableName);
                    //选择库
                    jedis.select(Integer.parseInt(base_number[i]));
                    //获取当前库号所有的key
                    Set<String> keys = jedis.keys("*");
                    //获取每个key的value
                    for (String key : keys) {
                        //判断value类型
                        String type = jedis.type(key);
                        RedisDatabase redisDatabase = new RedisDatabase();
                        redisDatabase.setKey(key);

                        //根据value类型取得value值
                        if(type.equals("string")){
                            Object value = jedis.get(key);
                            redisDatabase.setValue(value.toString());
                        }else if(type.equals("list")){
                            Object value = jedis.lrange(key, 0, -1);
                            redisDatabase.setValue(value.toString());

                        }else if(type.equals("set")){
                            Object value = jedis.smembers("key");
                            redisDatabase.setValue(value.toString());

                        }else if(type.equals("zset")){
                            Object value = jedis.zrange("key",0,-1);
                            redisDatabase.setValue(value.toString());

                        }else if(type.equals("hash")){
                            Object value = jedis.hvals(key);
                            redisDatabase.setValue(value.toString());

                        }


                        redisDatabaseService.addKeyValue(tableName,redisDatabase);
                    }
                }

                jedis.close();

                result.setCode(DataSourceEnum.SUCCESS.getCode());
                result.setMsg(DataSourceEnum.SUCCESS.getMsg());
             //   result.setPayload(databases);

            }
        } catch (Exception e) {
            result.setCode(DataSourceEnum.FAIL.getCode());
            result.setMsg(DataSourceEnum.FAIL.getMsg());
            System.out.println("Redis服务器连接失败" );
            e.printStackTrace();
        }

        return result;
    }




    //Redis数据源修改
    @PostMapping(value = "/redisUpdate")
    public Object redisUpdate(@RequestParam("redis_datasource") String redis_datasource) throws IOException {

        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.EXCEPTION.getCode());
        result.setMsg(DataSourceEnum.EXCEPTION.getMsg());

        ObjectMapper om = new ObjectMapper();
        Map<String,Object> datasource = om.readValue(redis_datasource,new TypeReference<Map<String,Object>>(){});

        //获取原数据用户名(用于修改表名称)
        Integer id = Integer.parseInt(datasource.get("id").toString());
        TDataSource tDataSource = tDataSourceService.findTDatasourceById(id);
        String pre_username = tDataSource.getUsername();

        String host = datasource.get("url").toString();
        String password = "";
        int port = Integer.parseInt(datasource.get("port").toString());
        if(datasource.containsKey("password") ){
            password = datasource.get("password").toString();
        }
        //获取新数据用户名
        String username = datasource.get("username").toString();
     //   int id = Integer.parseInt(datasource.get("id").toString());

        //判断是否有相同的数据源信息，有则无法配置
        TDataSource tDatasource = new TDataSource();
        if(datasource.containsKey("password")){
            tDatasource.setPassword(password);
        }
        tDatasource.setUrl(host);
        tDatasource.setPort(port);
        tDatasource.setUsername(username);

        boolean count =  tDataSourceService.findEqualTDatasource(tDatasource);
        System.out.println(count);
        if(count == true){
            result.setCode(DataSourceEnum.DUPLICATION.getCode());
            result.setMsg(DataSourceEnum.DUPLICATION.getMsg());
            return result;
        }
        //连接本地的redis
        try {
            Jedis jedis = new Jedis(host,port);
            jedis.auth(password);
            System.out.println("连接本地的Redis服务器成功");
            //查看服务是否运行
            if(jedis.ping().equals("PONG")){
                System.out.println("服务正在运行：" + jedis.ping());

                TDataSource redis_data = new TDataSource();
                redis_data.setType("redis");
                redis_data.setUrl(host);
                redis_data.setPort(port);
                if(datasource.containsKey("password")){
                    redis_data.setPassword(password);
                }

                redis_data.setUsername(username);
                redis_data.setId(id);
                Date date = new Date();
                redis_data.setUpdatetime(date);

                //更改数据库的表名
                for(int i=0;i<16;i++){
                    jedis.select(i);
                    if(jedis.dbSize()!=0){
                        //判断库号在mysql中是否存在
                        String pre_tableName = pre_username+"_redis_db"+i;
                        if(redisDatabaseService.existTable(pre_tableName)){
                            String tableName = username+"_redis_db"+i;

                            Map<String,String> map  = new LinkedHashMap<>();
                            map.put("tableName",tableName);
                            map.put("pre_tableName",pre_tableName);
                            redisDatabaseService.updTableName(map);
                        }
                    }
                }



                //更改t_datasource表的数据源信息
                boolean flag = tDataSourceService.updTDatasurceById(redis_data);

                if (flag) {
                    result.setCode(DataSourceEnum.SUCCESS.getCode());
                    result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                } else {
                    result.setCode(DataSourceEnum.FAIL.getCode());
                    result.setMsg(DataSourceEnum.FAIL.getMsg());
                }

                jedis.close();
            }
        } catch (Exception e) {
            result.setCode(DataSourceEnum.FAIL.getCode());
            result.setMsg(DataSourceEnum.FAIL.getMsg());
e.printStackTrace();
            System.out.println("Redis服务器连接失败" );
        }
        System.out.println(result.getCode());
        return result;
    }

}
