package com.qsccc.yyjhservice.dao.datasource;

import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import org.springframework.stereotype.Repository;

@Repository
public interface TDataSourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDataSource record);

    int insertSelective(TDataSource record);

    TDataSource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDataSource record);

    int updateByPrimaryKey(TDataSource record);
}