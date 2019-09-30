package com.qsccc.yyjhservice.service.auth;

import com.qsccc.yyjhservice.domain.auth.TRole;

import java.util.List;

public interface TRoleService {
    List<TRole> getAll();

    TRole findTRole(String rolename);

    List<TRole> getRoleByUserName(String loginid);
}
