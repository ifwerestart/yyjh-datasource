package com.qsccc.yyjhservice.dao.user;

import com.qsccc.yyjhservice.domain.user.TUserDateFormat;
import org.springframework.stereotype.Repository;

@Repository
public interface TUserDateFormatMapper {
    int updTUserDateFormat(TUserDateFormat tUserDateFormat);

    TUserDateFormat selTUserDateFormat();
}
