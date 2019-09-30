package com.qsccc.yyjhservice.service.user;

import com.qsccc.yyjhservice.dao.user.TUserMapper;
import com.qsccc.yyjhservice.domain.user.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class TUserServiceImpl implements TUserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public TUser selectByLoginid(String loginid) {
        return tUserMapper.selectByLoginid(loginid);
    }

    @Override
    public boolean updTUser(TUser user) {
        int count=tUserMapper.updTUser(user);
        if (count>0)
            return true;
        return false;
    }

    @Override
    public List<TUser> getAll() {
        return tUserMapper.getAll();
    }


    @Override
    public TUser findTUserByLoginId(String loginId) {
        TUser tUser = tUserMapper.findTUserByLoginId(loginId);
        return tUser;
    }

    @Override
    public int addTUser(TUser record) {
        return tUserMapper.addTUser(record);
    }
}
