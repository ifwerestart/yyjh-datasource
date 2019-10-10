package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsccc.yyjhservice.domain.user.TUser;
import com.qsccc.yyjhservice.domain.user.TUserDateFormat;
import com.qsccc.yyjhservice.service.user.TUserDateFormatService;
import com.qsccc.yyjhservice.service.user.TUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/TUser")
public class TUserController {
    @Autowired
    private TUserService tUserService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private TUserDateFormatService tUserDateFormatService;

    @RequestMapping("/selectByLoginid")
    public Object selectByLoginid(@RequestParam("json") String json) throws IOException {
        System.out.println(json);
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> map=om.readValue(json,new TypeReference<Map<String,Object>>(){});
        String loginid=map.get("login_username").toString();
        String password=map.get("login_password").toString();
        TUser tuser=tUserService.selectByLoginid(loginid);
        Map<String,Object> result=new HashMap<>();
        if(password.equals(tuser.getPassword())){
            //添加用户认证信息
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginid, password);
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            System.out.println("loginShiro:" + usernamePasswordToken);

            //Session
            httpSession.setAttribute("username",loginid);


            result.put("code",1);
        }else{
            result.put("code",0);
        }
        return result;
    }

    @RequestMapping("/proveusername")
    public Object proveUsername(@RequestParam("json") String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> map=om.readValue(json,new TypeReference<Map<String,Object>>(){});
        String fogusername=map.get("fogusername").toString();
        TUser tuser=tUserService.selectByLoginid(fogusername);
        Map<String,Object> result=new HashMap<>();
        if(tuser!=null){
            result.put("code",1);
        }else{
            result.put("code",0);
        }
        return result;
    }

    @RequestMapping("/changepwd")
    public Object changePwd(@RequestParam("json") String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        TUser user=om.readValue(json,TUser.class);
        boolean flag=tUserService.updTUser(user);
        Map<String,Object> result=new HashMap<>();
        if(flag){
            result.put("code",1);
        }else{
            result.put("code",0);
        }
        return result;
    }

    @RequestMapping("/userinfo")
    public Object userInfo(){
        String username=httpSession.getAttribute("username").toString();
        TUser tUser=tUserService.selectByLoginid(username);
        tUser.setPassword("************");
        return tUser;
    }

    @RequestMapping("/selectTUser")
    public Object selectTUser(){
        List<TUser> list=tUserService.getAll();
        System.out.println(list);

        return list;
    }
    @RequestMapping("/setDateFormat")
    public Object setDateFormat(@RequestParam("json") String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> map=om.readValue(json,new TypeReference<Map<String,Object>>(){});
        String date=map.get("date").toString();
        //修改
        TUserDateFormat tUserDateFormat=new TUserDateFormat();
        tUserDateFormat.setDateformat(date);
        return tUserDateFormatService.updTUserDateFormat(tUserDateFormat);
    }

    @RequestMapping("/selDateFormat")
    public Object selDateFormat(){
        TUserDateFormat tUserDateFormat=tUserDateFormatService.selTUserDateFormat();
        System.out.println(tUserDateFormat);
        return tUserDateFormat;
    }


    @RequestMapping("/logout")
    public Object logout() {
        return "logout";
    }

    @RequestMapping("/error")
    public Object error(){
        return "error";
    }

    //权限
    @RequestMapping("/permissionmanage")
    @RequiresPermissions(value = {"oper_role_permission","oper_user_role"}, logical = Logical.OR)
    public Object permissionManage() {
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }

    @RequestMapping("/datasmanage")
    @RequiresPermissions(value = {"create","update","select","delete"}, logical = Logical.OR)
    public Object datasManage() {
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }

    @RequestMapping("/datemanage")
    @RequiresPermissions(value = {"date_oper"}, logical = Logical.OR)
    public Object dateManage(){
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }

    @RequestMapping("/addPermission")
    @RequiresPermissions(value = {"create"}, logical = Logical.OR)
    public Object addPermission(){
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }

    @RequestMapping("/updPermission")
    @RequiresPermissions(value = {"update"}, logical = Logical.OR)
    public Object updPermission(){
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }

    @RequestMapping("/delPermission")
    @RequiresPermissions(value = {"delete"}, logical = Logical.OR)
    public Object delPermission(){
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }

    @RequestMapping("/operUserRole")
    @RequiresPermissions(value = {"oper_user_role"}, logical = Logical.OR)
    public Object operUserRole(){
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }
    @RequestMapping("/operRolePerm")
    @RequiresPermissions(value = {"oper_role_permission"}, logical = Logical.OR)
    public Object operRolePerm(){
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }

    @RequestMapping("/operDate")
    @RequiresPermissions(value = {"date_oper"}, logical = Logical.OR)
    public Object operDate(){
        Map<String,Object> result=new HashMap<>();
        result.put("code",1);
        return result;
    }


}
