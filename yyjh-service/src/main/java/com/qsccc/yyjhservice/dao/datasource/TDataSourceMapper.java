package com.qsccc.yyjhservice.dao.datasource;

import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TDataSourceMapper {
    int delTDatasourceById(Integer id);

    int addTDatasource(TDataSource record);

    TDataSource findTDatasourceById(Integer id);

    int updTDatasurceById(TDataSource record);

    List<TDataSource> getAll();

    List<TDataSource> getAllByPage(Map<String, Object> map);

    int getCount();

    boolean batchDelById(List<Integer> id_list);

    List<TDataSource> search(Map<String, String> map);

    int createAutoTable(@Param("tableName") String name, @Param("colums")List colums);

    int addByFilter(@Param("tableName") String name, @Param("colValues")List cols);

    List<String> findAllTables(String database_name);

    int delTable(String table_name);
}