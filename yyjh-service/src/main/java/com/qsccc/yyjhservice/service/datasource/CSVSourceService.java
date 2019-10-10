package com.qsccc.yyjhservice.service.datasource;

public interface CSVSourceService {
    boolean createTable(String sql);
    boolean addCSVData(String sql);
    boolean isTableExist(String tableName);
}
