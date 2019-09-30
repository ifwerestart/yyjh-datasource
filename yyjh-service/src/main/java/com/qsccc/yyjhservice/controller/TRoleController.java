package com.qsccc.yyjhservice.controller;

import com.qsccc.yyjhservice.domain.auth.TRole;
import com.qsccc.yyjhservice.service.auth.TRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/TRole")
public class TRoleController {
    @Autowired
    private TRoleServiceImpl tRoleService;

    @RequestMapping("/selectRole")
    public Object selectRole(){
        List<TRole> list=tRoleService.getAll();
        return list;
    }

}
