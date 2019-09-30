package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsccc.yyjhservice.domain.user.TUser;
import com.qsccc.yyjhservice.service.user.MailServiceImpl;
import com.qsccc.yyjhservice.service.user.TUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
public class SendEmailController{
    @Autowired
    private TUserServiceImpl tUserService;
    @Autowired
    private MailServiceImpl mailService;

    @RequestMapping("/sendEmail")
    public Object sendEmail(@RequestParam("json") String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> map=om.readValue(json,new TypeReference<Map<String,Object>>(){});
        String username=map.get("username").toString();
        String email=map.get("email").toString();
        String JYM=map.get("JYM").toString();
        TUser user=tUserService.selectByLoginid(username);
        Map<String,Object> result=new HashMap<>();
        if(email.equals(user.getEmail())){
            result.put("code",1);
            mailService.sendMail(email,username,JYM);
        }else {
            result.put("code",0);
        }
        return result;
    }

    @RequestMapping("/checkemail")
    public Object checkEmail(@RequestParam("json") String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> map=om.readValue(json,new TypeReference<Map<String,Object>>(){});
        String username=map.get("username").toString();
        String email=map.get("email").toString();
        TUser user=tUserService.selectByLoginid(username);
        Map<String,Object> result=new HashMap<>();
        if(email.equals(user.getEmail())){
            result.put("code",1);
        }else {
            result.put("code",0);
        }
        return result;
    }
}
