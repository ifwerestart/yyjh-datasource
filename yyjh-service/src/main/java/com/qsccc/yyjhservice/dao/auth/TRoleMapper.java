package com.qsccc.yyjhservice.dao.auth;

import com.qsccc.yyjhservice.domain.auth.TRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TRoleMapper {
    int delTRoleById(Integer id);

    int addTRole(TRole record);

    TRole findTRoleById(Integer id);

    int updTRoleById(TRole record);

    List<TRole> getAll();
}