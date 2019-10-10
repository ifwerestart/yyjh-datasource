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
import java.util.Map;

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
        int count=tDataSourceMapper.delTDatasourceById(id);
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

    @Override
    public List<TDataSource> getAllByPage(Map<String,Object> map) {
        return tDataSourceMapper.getAllByPage(map);
    }

    @Override
    public int getCount() {
        return tDataSourceMapper.getCount();
    }

    @Override
    public boolean batchDelById(List<Integer> id_list) {
        return tDataSourceMapper.batchDelById(id_list);
    }

    @Override
    public List<TDataSource> search(Map<String, String> map) {
        return tDataSourceMapper.search(map);
    }


    @Override
    public boolean createAutoTable(String name, List colums) {
        int count=tDataSourceMapper.createAutoTable(name,colums);
        if(count==0){
            return  true;
        }
        return false;
    }

    @Override
    public boolean addByFilter(String name, List cols) {
        int count=tDataSourceMapper.addByFilter(name,cols);
        if(count>0)
            return  true;
        return false;
    }

    public List<String> findAllTables(String database_name){
        return tDataSourceMapper.findAllTables(database_name);
    }

    @Override
    public boolean delTable(String table_name) {
        int count=tDataSourceMapper.delTable(table_name);
        if(count>0)
            return true;
        return false;
    }

    @Override
    public boolean findEqualTDatasource(TDataSource record) {
        TDataSource tDatasource = tDataSourceMapper.findEqualTDatasource(record);
        if(tDatasource!=null){
            return true;
        }else{
            return false;
        }

    }
}
