package com.qsccc.yyjhservice.service.user;

import com.qsccc.yyjhservice.dao.user.TUserDateFormatMapper;
import com.qsccc.yyjhservice.domain.user.TUserDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class TUserDateFormatServiceImpl implements TUserDateFormatService{
    @Autowired
    private TUserDateFormatMapper tUserDateFormatMapper;

    @Override
    public boolean updTUserDateFormat(TUserDateFormat tUserDateFormat) {
        int count=tUserDateFormatMapper.updTUserDateFormat(tUserDateFormat);
        if (count>0)
            return true;
        return false;
    }

    @Override
    public TUserDateFormat selTUserDateFormat() {
        return tUserDateFormatMapper.selTUserDateFormat();
    }
}
