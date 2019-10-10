package com.qsccc.yyjhservice.service.datasource;

import com.qsccc.yyjhservice.domain.datasource.TDataSource;

import java.util.List;
import java.util.Map;

public interface TDataSourceService {
    boolean delTDatasourceById(Integer id);

    boolean addTDatasource(TDataSource record);

    TDataSource findTDatasourceById(Integer id);

    boolean updTDatasurceById(TDataSource record);

    List<TDataSource> getAll();

    List<TDataSource> getAllByPage(Map<String, Object> map);

    int getCount();

    boolean batchDelById(List<Integer> id_list);

    List<TDataSource> search(Map<String, String> map);

    boolean createAutoTable(String name, List colums);

    boolean addByFilter(String name, List cols);

    List<String> findAllTables(String database_name);

    boolean delTable(String table_name);

    boolean findEqualTDatasource(TDataSource record);
}
