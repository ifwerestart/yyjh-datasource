package com.qsccc.yyjhservice.service.user;

import com.qsccc.yyjhservice.domain.user.TUser;

import java.util.List;

public interface TUserService {
    TUser selectByLoginid(String loginid);

    boolean updTUser(TUser user);

    List<TUser> getAll();

    TUser findTUserByLoginId(String loginId);

    int addTUser(TUser record);
}
