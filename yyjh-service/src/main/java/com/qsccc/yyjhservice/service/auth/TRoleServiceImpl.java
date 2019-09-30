package com.qsccc.yyjhservice.service.auth;

import com.qsccc.yyjhservice.dao.auth.TRoleMapper;
import com.qsccc.yyjhservice.dao.auth.TUserRoleMapper;
import com.qsccc.yyjhservice.dao.user.TUserMapper;
import com.qsccc.yyjhservice.domain.auth.TRole;
import com.qsccc.yyjhservice.domain.auth.TUserRole;
import com.qsccc.yyjhservice.domain.user.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class TRoleServiceImpl implements TRoleService {

    @Autowired
    private TRoleMapper tRoleMapper;
    @Autowired
    private TUserMapper tUserMapper;
    @Autowired
    private TUserRoleMapper tUserRoleMapper;

    @Override
    public List<TRole> getAll() {
        return tRoleMapper.getAll();
    }

    @Override
    public TRole findTRole(String rolename) {
        return tRoleMapper.findTRole(rolename);
    }

    @Override
    public List<TRole> getRoleByUserName(String loginid) {
        TUser tuser=tUserMapper.selectByLoginid(loginid);
        List<TUserRole> list=tUserRoleMapper.selectTUserRoleByUserid(tuser.getId());
        List<TRole> lTRoles=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            Integer roleid=list.get(i).getRoleId();
            TRole tRole=tRoleMapper.findTRoleById(roleid);
            lTRoles.add(tRole);
        }
        return lTRoles;
    }
}
