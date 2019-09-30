package com.qsccc.yyjhservice.service.auth;

import com.qsccc.yyjhservice.domain.auth.TPermission;

import java.util.List;

public interface TPermissionService {
    List<TPermission> getPermissionByRoleId(Integer roleid);

    TPermission findTPermissionByPerm(String permissoin);
}
