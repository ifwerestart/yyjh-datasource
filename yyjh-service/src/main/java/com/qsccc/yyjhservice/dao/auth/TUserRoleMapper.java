package com.qsccc.yyjhservice.dao.auth;

import com.qsccc.yyjhservice.domain.auth.TUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TUserRoleMapper {
    int delTUserRoleById(Integer id);

    int addTUserRole(TUserRole record);

    TUserRole findTUserRoleById(Integer id);

    int updTUserRoleById(TUserRole record);

    List<TUserRole> getAll();
}