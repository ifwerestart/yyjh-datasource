package com.qsccc.yyjhservice.service.datasource;

import com.qsccc.yyjhservice.domain.datasource.TDataSource;

import java.util.List;

public interface TDataSourceService {
    boolean delTDatasourceById(Integer id);

    boolean addTDatasource(TDataSource record);

    TDataSource findTDatasourceById(Integer id);

    boolean updTDatasurceById(TDataSource record);

    List<TDataSource> getAll();
}
