package com.qsccc.yyjhservice.service.auth;

import com.qsccc.yyjhservice.dao.auth.TRolePermissionMapper;
import com.qsccc.yyjhservice.domain.auth.TRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class TRolePermissionServiceImpl implements  TRolePermissionService {
    @Autowired
    private TRolePermissionMapper tRolePermissionMapper;
    @Override
    public boolean delRolePermissionByRoleId(Integer roleId) {
        return tRolePermissionMapper.delRolePermissionByRoleId(roleId);
    }

    @Override
    public boolean addTRolePermission(TRolePermission record) {
        int count=tRolePermissionMapper.addTRolePermission(record);
        if (count>0)
            return true;
        return false;
    }
}
