package com.qsccc.yyjhservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.PublicKey;

@Controller
@RequestMapping("/router")
public class RouterController {

    @RequestMapping("/datasource")
    public  String datasource(){
        return "datasource/datasource";
    }
}
