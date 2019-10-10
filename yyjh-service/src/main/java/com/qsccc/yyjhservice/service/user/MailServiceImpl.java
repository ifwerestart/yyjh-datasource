package com.qsccc.yyjhservice.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;
        //读取application.properties的内容
    @Value("${mail.fromMail.addr}")
    private String form;


    @Override
    public void sendMail(String to,String username,String JYM) {
        String subject="发送验证码";
        String content="尊敬的"+username+"用户：   您好,欢迎您，   您正在通过邮件找回密码。如非本人操作，请忽视本邮件." +
                "     本次校验码为：	" +JYM+"	  本次验证码60秒内有效，请尽快填写校验码";
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(form);//发起者
        mailMessage.setTo(to);//接受者
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        try {
            mailSender.send(mailMessage);
            System.out.println("发送简单邮件");
        }catch (Exception e){
            System.out.println("发送简单邮件失败");
        }
    }
}
