package com.qsccc.yyjhservice.service.datasource;


import com.qsccc.yyjhservice.dao.datasource.CSVSourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Slf4j
@Transactional
public class CSVSourceServiceImpl implements CSVSourceService {
    @Autowired
    private CSVSourceMapper dataSourceMapper;

    @Override
    public boolean createTable(String sql) {
        int count = dataSourceMapper.createTable(sql);
        if(count==0){
            return  true;
        }else {
            return  false;
        }
    }

    @Override
    public boolean addCSVData(String sql) {
        int count = dataSourceMapper.addCSVData(sql);
        if(count>0){
            return  true;
        }else {
            return  false;
        }
    }

    @Override
    public boolean isTableExist(String tableName) {
        int count = dataSourceMapper.isTableExist(tableName);
        if(count > 0){
            return  true;
        }else {
            return  false;
        }
    }
}
