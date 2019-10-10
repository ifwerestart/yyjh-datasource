package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsccc.tools.ExcelOper;
import com.qsccc.tools.YYJHTools;
import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import com.qsccc.yyjhservice.domain.user.TUser;
import com.qsccc.yyjhservice.enumeration.DataSourceEnum;
import com.qsccc.yyjhservice.service.datasource.TDataSourceService;
import com.qsccc.yyjhservice.service.user.TUserService;
import com.qsccc.yyjhservice.service.user.TUserServiceImpl;
import com.qsccc.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    private TUserService tUserService;

    @RequestMapping("/findTUserByLoginId/{loginId}")
    @ResponseBody
    public Object findTUserByLoginId(@PathVariable("loginId") String loginId) throws Exception{

        System.out.println(loginId);
        TUser tUser = tUserService.findTUserByLoginId(loginId);
        return tUser;
    }

    @RequestMapping("/addTUser/{loginId}/{password}/{nickname}/{userimg}/{email}/{tel}/{remark}")
    @ResponseBody
    public Object addTUser(@PathVariable("loginId") String loginId,@PathVariable("password") String password,@PathVariable("nickname") String nickname,@PathVariable("userimg") String userimg,@PathVariable("email") String email,@PathVariable("tel") String tel,@PathVariable("remark") String remark) throws Exception{
        Date date = new Date();
    //    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");

    //    System.out.println(loginId+" "+password+" ");

        TUser tUser = new TUser();
        tUser.setLoginid(loginId);
        tUser.setPassword(password);
        if(!nickname.equals("无")){
            tUser.setNickname(nickname);
        }


        userimg = org.apache.commons.lang.StringEscapeUtils.escapeJava(userimg);
        System.out.println(userimg);
        tUser.setUserimg(userimg);

        tUser.setEmail(email);
        tUser.setTel(tel);
        tUser.setCreateTime(date);
        if(!remark.equals("无")){
            tUser.setRemark(remark);
        }


        tUser.setState("1");

        int count = tUserService.addTUser(tUser);
        return count;
    }

}
