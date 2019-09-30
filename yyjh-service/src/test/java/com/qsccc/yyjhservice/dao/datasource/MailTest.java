package com.qsccc.yyjhservice.dao.datasource;

import com.qsccc.yyjhservice.service.user.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {

    @Autowired
    private MailService mailService;

    private String to="2621175171@qq.com";

    @Test
    public  void sendSimpleMail() throws Exception{
        mailService.sendMail(to,"2621175171","454523");
    }
}
