package com.qsccc.yyjhservice.service.datasource;

import com.qsccc.yyjhservice.dao.datasource.RedisDatabaseMapper;
import com.qsccc.yyjhservice.dao.datasource.TDataSourceMapper;
import com.qsccc.yyjhservice.domain.datasource.RedisDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Primary
@Slf4j
@Transactional
public class RedisDatabaseServiceImpl implements RedisDatabaseService{
    @Autowired
    private RedisDatabaseMapper redisDatabaseMapper;


    @Override
    public boolean addKeyValue(String tableName,RedisDatabase redisDatabase) {
        int count = redisDatabaseMapper.addKeyValue(tableName,redisDatabase);
        if(count>0){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean createTable(String tableName) {
        int count = redisDatabaseMapper.createTable(tableName);
        System.out.println(count);
        if(count==0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean existTable(String tableName) {
        int count = redisDatabaseMapper.existTable(tableName);
        System.out.println(count);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean dropTable(String tableName) {
        int count = redisDatabaseMapper.dropTable(tableName);
        System.out.println(count);
        if(count==0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updTableName( Map<String,String> map) {
        int count = redisDatabaseMapper.updTableName(map);
        System.out.println(count);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }
}
