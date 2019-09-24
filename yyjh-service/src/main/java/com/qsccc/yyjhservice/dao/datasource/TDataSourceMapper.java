package com.qsccc.yyjhservice.dao.datasource;

import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TDataSourceMapper {
    int delTDatasourceById(Integer id);

    int addTDatasource(TDataSource record);

    TDataSource findTDatasourceById(Integer id);

    int updTDatasurceById(TDataSource record);

    List<TDataSource> getAll();
}