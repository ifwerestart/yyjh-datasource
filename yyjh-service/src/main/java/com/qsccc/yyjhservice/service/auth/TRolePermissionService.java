package com.qsccc.yyjhservice.service.auth;

import com.qsccc.yyjhservice.domain.auth.TRolePermission;

import java.util.List;

public interface TRolePermissionService {
    boolean delRolePermissionByRoleId(Integer roleId);

    boolean addTRolePermission(TRolePermission record);
}
