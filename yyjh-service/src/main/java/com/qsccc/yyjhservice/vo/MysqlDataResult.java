package com.qsccc.yyjhservice.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MysqlDataResult {
    String  host;
    String port;
    String username;
    String  password;
    String databaseName;
    List<Map<String,Object>> tables;



}
