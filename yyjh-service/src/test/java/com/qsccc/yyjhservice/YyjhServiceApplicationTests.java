package com.qsccc.yyjhservice;

import com.qsccc.yyjhservice.config.jedis.JedisUtil;
import com.qsccc.yyjhservice.dao.user.TUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YyjhServiceApplicationTests {
    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private TUserMapper tUserMapper;
   // @Test
    public  void contextLoads(){
        System.out.println("这是jedis测试："+jedisUtil.get("test2"));

    }

    @Test
    public  void testMybatis(){
        System.out.println(tUserMapper.selectByPrimaryKey(1));
    }



}
