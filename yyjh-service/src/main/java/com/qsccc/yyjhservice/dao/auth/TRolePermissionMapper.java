package com.qsccc.yyjhservice.dao.auth;

import com.qsccc.yyjhservice.domain.auth.TRolePermission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TRolePermissionMapper {
    int delTRolePermissionById(Integer id);

    int addTRolePermission(TRolePermission record);

    TRolePermission findTRolePermissionById(Integer id);

    int updTRolePermissionById(TRolePermission record);

    List<TRolePermission> getAll();

    List<TRolePermission> getTRolePermissionByRoleid(Integer roleId);

    boolean delRolePermissionByRoleId(Integer roleId);
}