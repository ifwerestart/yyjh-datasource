package com.qsccc.yyjhservice.config.shiro;

import com.qsccc.yyjhservice.domain.auth.TPermission;
import com.qsccc.yyjhservice.domain.auth.TRole;
import com.qsccc.yyjhservice.domain.user.TUser;
import com.qsccc.yyjhservice.service.auth.TPermissionService;
import com.qsccc.yyjhservice.service.auth.TRoleService;
import com.qsccc.yyjhservice.service.user.TUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*
1.配置shiro、配置realm、其他shiro securityManager
2.自定义realm，实现两种认证方式（业务权限相关）
3、写接口，写权限（不写）
 */

//实现AuthorizingRealm接口用户用户认证
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private TUserService tUserService;
    @Autowired
    private TRoleService tRoleService;
    @Autowired
    private TPermissionService tPermissionService;

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {//进行权限验证
        //获取登录用户名
        String name = principals.getPrimaryPrincipal().toString();
        //查询用户
        TUser tUser = tUserService.selectByLoginid(name);
        //查询角色
        List<TRole> roles = tRoleService.getRoleByUserName(name);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (TRole role : roles) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRolename());
            List<TPermission> permissions = tPermissionService.getPermissionByRoleId(role.getId());
            for (TPermission permission : permissions) {
                //添加权限
                simpleAuthorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return simpleAuthorizationInfo;
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {//进行身份验证的(登录验证)
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (token.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String name = token.getPrincipal().toString();
        //查询用户
        TUser user = tUserService.selectByLoginid(name);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证token和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, user.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
    }
}