package com.qsccc.yyjhservice.service.datasource;

import com.qsccc.yyjhservice.dao.datasource.TDataSourceMapper;
import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
//日志
@Slf4j
//事务
@Transactional
public class TDataSourceServiceImpl implements  TDataSourceService{

    @Autowired
    private TDataSourceMapper tDataSourceMapper;
    @Override
    public boolean delTDatasourceById(Integer id) {
        int count=tDataSourceMapper.delTDatasourceById(2);
        if(count>0)
            return true;
        return false;
    }

    @Override
    public boolean addTDatasource(TDataSource record) {
        int count=tDataSourceMapper.addTDatasource(record);
        if(count>0)
            return true;
        return false;
    }

    @Override
    public TDataSource findTDatasourceById(Integer id) {
        return tDataSourceMapper.findTDatasourceById(id);
    }

    @Override
    public boolean updTDatasurceById(TDataSource record) {
        int count=tDataSourceMapper.updTDatasurceById(record);
        if(count>0)
            return  true;
        return false;
    }

    @Override
    public List<TDataSource> getAll() {
        return tDataSourceMapper.getAll();
    }
}
