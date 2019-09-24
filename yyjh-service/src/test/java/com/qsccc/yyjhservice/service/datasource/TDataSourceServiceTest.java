package com.qsccc.yyjhservice.service.datasource;

import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TDataSourceServiceTest {
    @Autowired
    private  TDataSourceService tDataSourceService;

    @Test
    public void delTDatasourceById() {
        boolean res=tDataSourceService.delTDatasourceById(2);
        System.out.println(res);

    }

    @Test
    public void addTDatasource() {
        TDataSource tDataSource=new TDataSource();
        tDataSource.setAlias("alias4");
        tDataSource.setAuth("update");
        Date date=new Date();
        tDataSource.setCreatetime(date);
        tDataSource.setPassword("123456");
        tDataSource.setPort(3308);
        //tDataSource.setType("jdbc");
        tDataSource.setUserId("4");
        tDataSource.setUsername("xiaozhan");
        boolean res=tDataSourceService.addTDatasource(tDataSource);
        System.out.println(res);
    }

    @Test
    public void findTDatasourceById() {
        TDataSource td=new TDataSource();
        td=tDataSourceService.findTDatasourceById(1);
        System.out.println(td);
    }

    @Test
    public void updTDatasurceById() {
        TDataSource tDataSource=new TDataSource();
        //tDataSource.setAlias("alias3");
        tDataSource.setAuth("delete");
        tDataSource.setUpdatetime(new Date());
        tDataSource.setId(3);
        boolean res=tDataSourceService.updTDatasurceById(tDataSource);
        System.out.println(res);
    }

    @Test
    public void getAll() {
        List<TDataSource> all=tDataSourceService.getAll();
        System.out.println(all.size());
    }

}