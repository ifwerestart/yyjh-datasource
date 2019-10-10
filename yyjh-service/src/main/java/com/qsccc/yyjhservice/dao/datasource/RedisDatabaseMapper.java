package com.qsccc.yyjhservice.dao.datasource;


import com.qsccc.yyjhservice.domain.datasource.RedisDatabase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface RedisDatabaseMapper {
    int addKeyValue(@Param("tableName") String tableName, @Param("redisDatabase") RedisDatabase redisDatabase);

    int createTable(@Param("tableName") String tableName);

    int existTable(@Param("tableName") String tableName);

    int dropTable(@Param("tableName") String tableName);

    int updTableName(Map<String, String> map);


}
