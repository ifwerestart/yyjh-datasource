package com.qsccc.yyjhservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qsccc.yyjhservice")
public class YyjhServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YyjhServiceApplication.class, args);
    }

}
