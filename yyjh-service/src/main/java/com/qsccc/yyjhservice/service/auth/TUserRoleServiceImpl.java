package com.qsccc.yyjhservice.service.auth;

import com.qsccc.yyjhservice.dao.auth.TUserRoleMapper;
import com.qsccc.yyjhservice.domain.auth.TUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class TUserRoleServiceImpl implements TUserRoleService {
    @Autowired
    private TUserRoleMapper tUserRoleMapper;
    @Override
    public boolean addTUserRole(TUserRole tUserRole) {
        int count=tUserRoleMapper.addTUserRole(tUserRole);
        if (count>0)
            return true;
        return false;
    }

    @Override
    public boolean delTUserRoleByUserid(Integer userid) {
        int count=tUserRoleMapper.delTUserRoleByUserid(userid);
        if (count>0)
            return true;
        return false;
    }

    @Override
    public List<TUserRole> selectTUserRoleByUserid(Integer userid) {
        return tUserRoleMapper.selectTUserRoleByUserid(userid);
    }
}
