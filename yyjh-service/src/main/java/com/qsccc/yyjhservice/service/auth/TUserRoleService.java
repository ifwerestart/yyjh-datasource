package com.qsccc.yyjhservice.service.auth;

import com.qsccc.yyjhservice.domain.auth.TUserRole;

import java.util.List;

public interface TUserRoleService {
    boolean addTUserRole(TUserRole tUserRole);

    boolean delTUserRoleByUserid(Integer userid);

    List<TUserRole> selectTUserRoleByUserid(Integer userid);
}
