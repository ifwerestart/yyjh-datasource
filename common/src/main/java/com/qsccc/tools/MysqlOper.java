package com.qsccc.tools;

import org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle;

import java.sql.*;

public class MysqlOper {
    /**
     * 获取一个连接
     * @return
     */

    public static Connection getConnection(String url,String user,String password){
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数

        //url: jdbc:mysql://127.0.0.1:3306/yyjh_datasource?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true
        //String url = "jdbc:mysql://"+host+":"+port+"/"+database_name+"?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";

        try {
            Class.forName(driver);
            // 连续数据库
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放资源
     * @param conn
     * @param stmt
     * @param rs
     */
    public static void release(Connection conn, Statement stmt, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs=null;
        }
        if(stmt!=null){
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            stmt = null;
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 利用创建表的语句和select语句解决数据迁移问题
     * @param dataBaseNameSource 源数据库名称
     * @param tableNameSource 源数据表名称
     * @param dataBaseNameTarget 目标数据库名称
     * @param tableNameTarget 目标数据表名称
     */
    public static boolean copyDataFromOneTable2AnotherWithSelectAndCreateSql(String dataBaseNameSource, String tableNameSource,
                                                                          String dataBaseNameTarget, String tableNameTarget, String user, String password,String url){
        Connection  conn = null;
        Statement statement= null;
        boolean res=false;
        try {
            conn = getConnection(url,user,password);
            statement = conn.createStatement();
            //创建数据库
            //statement.execute("create database "+dataBaseNameTarget);
            statement.execute("use "+dataBaseNameTarget);

            //创建数据表
            statement.execute("create table "+tableNameTarget+" LIKE "+dataBaseNameSource+"."+tableNameSource);

            //导入数据
            String copySql = "insert "+tableNameTarget +" select * from "+dataBaseNameSource+"."+tableNameSource;
            res=statement.execute(copySql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }



}
