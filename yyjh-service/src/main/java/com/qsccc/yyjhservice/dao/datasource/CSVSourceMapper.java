package com.qsccc.yyjhservice.dao.datasource;

import com.qsccc.yyjhservice.domain.datasource.RedisDatabase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CSVSourceMapper {
    int addCSVData(String sql);

    int createTable(String sql);

    int isTableExist(String tableName);

//    int existTable(@Param("tableName") String tableName);

//    int dropTable(@Param("tableName") String tableName);
}
