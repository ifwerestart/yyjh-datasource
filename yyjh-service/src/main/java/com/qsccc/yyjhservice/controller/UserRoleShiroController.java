package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsccc.yyjhservice.domain.auth.TPermission;
import com.qsccc.yyjhservice.domain.auth.TRole;
import com.qsccc.yyjhservice.domain.auth.TRolePermission;
import com.qsccc.yyjhservice.domain.auth.TUserRole;
import com.qsccc.yyjhservice.domain.user.TUser;
import com.qsccc.yyjhservice.service.auth.TPermissionService;
import com.qsccc.yyjhservice.service.auth.TRolePermissionServiceImpl;
import com.qsccc.yyjhservice.service.auth.TRoleServiceImpl;
import com.qsccc.yyjhservice.service.auth.TUserRoleServiceImpl;
import com.qsccc.yyjhservice.service.user.TUserServiceImpl;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/RoleShiro")
public class UserRoleShiroController {

    @Autowired
    private TRoleServiceImpl tRoleService;
    @Autowired
    private TUserServiceImpl tUserService;
    @Autowired
    private TPermissionService tPermissionService;
    @Autowired
    private TUserRoleServiceImpl tUserRoleService;
    @Autowired
    private TRolePermissionServiceImpl tRolePermissionService;

    @RequestMapping("/setUserRole")
    public Object setUserRole(@RequestParam("json") String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> map=om.readValue(json,new TypeReference<Map<String,Object>>(){});
        List<String> chk_roles=(List<String>) map.get("chk_roles");
        String username=map.get("username").toString();
        TUser tUser=tUserService.selectByLoginid(username);
        System.out.println(tUser);
        tUserRoleService.delTUserRoleByUserid(tUser.getId());
        int count=0;
        for (int i=0;i<chk_roles.size();i++){
            String rolename=chk_roles.get(i);
            TRole tRole=tRoleService.findTRole(rolename);
            System.out.println(tRole);
            TUserRole tUserRole=new TUserRole();
            tUserRole.setRoleId(tRole.getId());
            tUserRole.setUserId(tUser.getId());
            boolean flag=tUserRoleService.addTUserRole(tUserRole);
            if (flag){
                count++;
            }
        }
        Map<String,Object> result=new HashMap<>();
        if(count==chk_roles.size()){
            result.put("code",1);
        }else {
            result.put("code",0);
        }
        return result;
    }

    @RequestMapping("/setRolePermission")
    public Object setRolePermission(@RequestParam("json") String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> map=om.readValue(json,new TypeReference<Map<String,Object>>(){});
        List<String> chk_permission=(List<String>) map.get("chk_permission");
        String rolename=map.get("rolename").toString();
        TRole tRole=tRoleService.findTRole(rolename);
        tRolePermissionService.delRolePermissionByRoleId(tRole.getId());
        int count=0;
        for (int i=0;i<chk_permission.size();i++){
            String permission=chk_permission.get(i).toString();
            TPermission tPermission=tPermissionService.findTPermissionByPerm(permission);
            TRolePermission tRolePermission=new TRolePermission();
            tRolePermission.setRoleId(tRole.getId());
            tRolePermission.setPermissionId(tPermission.getId());
            boolean flag=tRolePermissionService.addTRolePermission(tRolePermission);
            if (flag){
                count++;
            }
        }
        Map<String,Object> result=new HashMap<>();
        if(count==chk_permission.size()){
            result.put("code",1);
        }else {
            result.put("code",0);
        }
        return result;
    }


}
