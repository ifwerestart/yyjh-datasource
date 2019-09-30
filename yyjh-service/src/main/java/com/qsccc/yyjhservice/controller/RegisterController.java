package com.qsccc.yyjhservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @RequestMapping("/user_register")
    public  String datasource(){
        return "datasource/register";
    }

    @RequestMapping("/selectAllDatasource")
    public  String datasource1(){
        return "datasource/alldatasource";
    }
}
