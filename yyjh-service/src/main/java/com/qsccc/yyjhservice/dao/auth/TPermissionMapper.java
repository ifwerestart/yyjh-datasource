package com.qsccc.yyjhservice.dao.auth;

import com.qsccc.yyjhservice.domain.auth.TPermission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TPermissionMapper {
    int delTPermissionByID(Integer id);

    int addTPermission(TPermission record);

    TPermission findTPermissionById(Integer id);

    int updTPermissionById(TPermission record);

    List<TPermission> getAll();


}