package com.qsccc.yyjhservice.dao.user;

import com.qsccc.yyjhservice.domain.user.TUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TUserMapper {
    int deleteTUserById(Integer id);

    int addTUser(TUser record);

    TUser findTUserById(Integer id);

    int updTUserById(TUser record);

    List<TUser> getAll();

    //cp
    TUser selectByLoginid(String loginid);
    int updTUser(TUser user);


    //cjj
    TUser findTUserByLoginId(String loginId);
}