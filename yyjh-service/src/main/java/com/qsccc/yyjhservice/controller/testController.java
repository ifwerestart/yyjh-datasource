package com.qsccc.yyjhservice.controller;

import com.qsccc.yyjhservice.dao.auth.TUserRoleMapper;
import com.qsccc.yyjhservice.domain.auth.TUserRole;
import com.qsccc.yyjhservice.domain.user.TUser;
import com.qsccc.yyjhservice.service.auth.TRoleService;
import com.qsccc.yyjhservice.service.user.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Test")
public class testController {
    @Autowired
    private TUserRoleMapper tUserRoleMapper;
    @Autowired
    private TRoleService tRoleService;
    @Autowired
    private TUserService tUserService;
    @RequestMapping("/test")
    public Object test(){
        return tRoleService.getRoleByUserName("admin");
    }
}
