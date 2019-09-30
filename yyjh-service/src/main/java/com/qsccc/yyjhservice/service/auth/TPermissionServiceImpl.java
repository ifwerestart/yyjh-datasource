package com.qsccc.yyjhservice.service.auth;

import com.qsccc.yyjhservice.dao.auth.TPermissionMapper;
import com.qsccc.yyjhservice.dao.auth.TRolePermissionMapper;
import com.qsccc.yyjhservice.domain.auth.TPermission;
import com.qsccc.yyjhservice.domain.auth.TRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class TPermissionServiceImpl implements TPermissionService {
    @Autowired
    private TRolePermissionMapper tRolePermissionMapper;
    @Autowired
    private TPermissionMapper tPermissionMapper;
    @Override
    public List<TPermission> getPermissionByRoleId(Integer roleid) {
        List<TRolePermission> list=tRolePermissionMapper.getTRolePermissionByRoleid(roleid);
        List<TPermission> permissionList=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            TPermission permission=tPermissionMapper.findTPermissionById(list.get(i).getPermissionId());
            permissionList.add(permission);
        }
        return permissionList;
    }

    @Override
    public TPermission findTPermissionByPerm(String permissoin) {
        return tPermissionMapper.findTPermissionByPerm(permissoin);
    }
}
