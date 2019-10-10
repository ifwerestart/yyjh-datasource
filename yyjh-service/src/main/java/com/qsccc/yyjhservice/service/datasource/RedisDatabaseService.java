package com.qsccc.yyjhservice.service.datasource;

import com.qsccc.yyjhservice.domain.datasource.RedisDatabase;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface RedisDatabaseService {
    boolean addKeyValue(@Param("tableName") String tableName, @Param("redisDatabase") RedisDatabase redisDatabase);
    boolean createTable(@Param("tableName") String tableName);
    boolean existTable(@Param("tableName") String tableName);
    boolean dropTable(@Param("tableName") String tableName);
    boolean updTableName(Map<String, String> map);
}
