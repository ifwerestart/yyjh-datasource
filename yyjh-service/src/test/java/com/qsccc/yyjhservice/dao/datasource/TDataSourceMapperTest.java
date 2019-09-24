package com.qsccc.yyjhservice.dao.datasource;

import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TDataSourceMapperTest {
    @Autowired
    private TDataSourceMapper tDataSourceMapper;

    @Test
    public void delTDatasourceById() {
        int count=tDataSourceMapper.delTDatasourceById(2);
        if(count>0)
            System.out.println("success");
        else
            System.out.println("fail");
    }

    @Test
    public void addTDatasource() {
        TDataSource tDataSource=new TDataSource();
        tDataSource.setAlias("alias2");
        tDataSource.setAuth("delete");
        Date date=new Date();
        tDataSource.setCreatetime(date);
        tDataSource.setPassword("123123");
        tDataSource.setPort(3306);
        tDataSource.setType("jdbc");
        tDataSource.setUserId("2");
        tDataSource.setUsername("qwh");
        int count=tDataSourceMapper.addTDatasource(tDataSource);
        if(count>0)
            System.out.println("success");
        else
            System.out.println("fail");
    }

    @Test
    public void findTDatasourceById() {
        TDataSource td=new TDataSource();
        td=tDataSourceMapper.findTDatasourceById(1);
        System.out.println(td);

    }

    @Test
    public void updTDatasurceById() {
        TDataSource tDataSource=new TDataSource();
        tDataSource.setAlias("alias1");
        tDataSource.setAuth("update");
        tDataSource.setUpdatetime(new Date());
        tDataSource.setId(1);
        int count=tDataSourceMapper.updTDatasurceById(tDataSource);
        if(count>0)
            System.out.println("success");
        else
            System.out.println("fail");
    }

    @Test
    public void getAll() {
        List<TDataSource> all=tDataSourceMapper.getAll();
        System.out.println(all.size());
    }
}