package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.qsccc.tools.MysqlOper;
import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import com.qsccc.yyjhservice.enumeration.DataSourceEnum;
import com.qsccc.yyjhservice.service.datasource.TDataSourceService;
import com.qsccc.yyjhservice.vo.ControllerResult;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static com.qsccc.tools.MysqlOper.copyDataFromOneTable2AnotherWithSelectAndCreateSql;

@RestController
@RequestMapping("/mysql")
public class MysqlController {
    @Autowired
    private TDataSourceService tDataSourceService;
    @Autowired
    private HttpSession httpSession;


    //根据配置连接数据库，如果数据库没有上传就上传，不然不能上传
    @RequestMapping(value = "/mysqlConfig")
    public Object connect(@RequestParam(value = "connect_param") String connect) throws Exception {
        ControllerResult result=new ControllerResult();

        ObjectMapper oMapper=new ObjectMapper();
        JsonNode rootNode=oMapper.readValue(connect, JsonNode.class);
        String  host=rootNode.get("host").asText();
        String  port=rootNode.get("port").asText();
        String database_name=rootNode.get("databaseName").asText();
        String user =rootNode.get("username").asText();
        String password = rootNode.get("password").asText();
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数
        //url: jdbc:mysql://127.0.0.1:3306/yyjh_datasource?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true
        String url = "jdbc:mysql://"+host+":"+port+"/"+database_name+"?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";

        TDataSource mysql_data=new TDataSource();
        mysql_data.setDatabaseName(database_name);
        mysql_data.setType("mysql");
        mysql_data.setCreatetime(new Date());
        mysql_data.setDriver(driver);
        mysql_data.setUrl(url);
        mysql_data.setPort(Integer.parseInt(port));
        mysql_data.setUsername(user);
        mysql_data.setPassword(password);
        String userId = httpSession.getAttribute("username").toString();
        mysql_data.setUserId(userId);

        try {
            // 加载驱动程序
            Class.forName(driver);
            // 连续数据库
            Connection conn = DriverManager.getConnection(url, user, password);

            if (!conn.isClosed() && conn!=null){
                result.setCode(DataSourceEnum.SUCCESS.getCode());
                result.setMsg(DataSourceEnum.SUCCESS.getMsg());

                //连接成功之后把数据上传到数据库的表里
                //如果表里已经存在url、port、database-name就不能上传
                List<TDataSource> all_datas=tDataSourceService.getAll();
                for (int i=0;i<all_datas.size();i++){
                    if(all_datas.get(i).getType().equals("mysql") && all_datas.get(i).getDatabaseName().equals(database_name) && all_datas.get(i).getPort()==Integer.parseInt(port) && all_datas.get(i).getUrl().equals(url)){
                        result.setCode(DataSourceEnum.FAIL.getCode());
                        result.setMsg(DataSourceEnum.FAIL.getMsg());
                        return result;
                    }
                }

                tDataSourceService.addTDatasource(mysql_data);
            }else{
                result.setCode(DataSourceEnum.EXCEPTION.getCode());
                result.setMsg(DataSourceEnum.EXCEPTION.getMsg());
            }

            conn.close();
        } catch(ClassNotFoundException e) {
            System.out.println("加载驱动异常!");
            e.printStackTrace();
        } catch(SQLException e) {
            result.setCode(DataSourceEnum.EXCEPTION.getCode());
            result.setMsg(DataSourceEnum.EXCEPTION.getMsg());
            System.out.println("获取连接异常!");
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }


   //根据id知道数据库名字信息，然后连接，显示该数据库下面的所有表
    @RequestMapping(value = "/mysqlFindTables/{id}")
    public  Object findTables(@PathVariable Integer id) throws Exception {
        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.EXCEPTION.getCode());
        result.setMsg(DataSourceEnum.EXCEPTION.getMsg());

        TDataSource tDataSource=tDataSourceService.findTDatasourceById(id);
        //System.out.println("id="+id);
        String database_name=tDataSource.getDatabaseName();
       // System.out.println("database_name="+database_name);
        String url=tDataSource.getUrl();
        String  user=tDataSource.getUsername();
        String password=tDataSource.getPassword();

        List<Map<String,Object>> config_tables=new ArrayList<>();
        Map<String,Object> config_datas=new HashMap<>();
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver);
            // 连续选择的数据库并且获取这个数据库下面的所有表
            Connection conn = MysqlOper.getConnection(url, user, password);
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, null, new String[] { "TABLE" });
            while (rs.next()) {
                Map<String, Object> table = new HashMap<String, Object>();
                boolean flag = true;//初始状态这个表是没有上传入库过的
                String table_name = rs.getString(3);

                table.put("table_name",table_name);

                String create_table_name=database_name+"_"+table_name;
                //把这个数据库下面的所有表跟yyjh_datasource数据库下面的表进行比对，看有没有入库过
                List<String> allTables=tDataSourceService.findAllTables("yyjh_datasource");
                boolean isExists=false;
                for(int i=0;i<allTables.size();i++){
                    String existTableName=allTables.get(i).toString();
                    if(create_table_name.equals(existTableName)){
                        //如果现在要入库的表名在数据库里已经存在了，就不能入库
                        isExists=true;
                        table.put("state","false");
                        break;
                    }
                }

                if(isExists==false){
                    table.put("state","true");
                }
                config_tables.add(table);
            }
            config_datas.put("tables",config_tables);
            config_datas.put("databaseName",database_name);
            result.setPayload(config_datas);
            result.setCode(DataSourceEnum.SUCCESS.getCode());
            result.setMsg(DataSourceEnum.SUCCESS.getMsg());


        } catch(SQLException e) {
            result.setCode(DataSourceEnum.EXCEPTION.getCode());
            result.setMsg(DataSourceEnum.EXCEPTION.getMsg());
            System.out.println("获取连接异常!");
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }


        //System.out.println(result);

        return  result;
    }


    //入库，把选择的数据库里的表复制到yyjh_datasource数据库里，如果选择的这个库跟表格已经在yyjh_datasource里存在了，就不能入库
    @RequestMapping("/mysqlEnter")
    public Object mysqlEnter(@RequestParam("enter_param") String enter_param) throws Exception{

        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.FAIL.getCode());
        result.setMsg(DataSourceEnum.FAIL.getMsg());

        ObjectMapper oMapper=new ObjectMapper();
        JsonNode rootNode=oMapper.readValue(enter_param, JsonNode.class);
        String  id1=rootNode.get("id").asText();
        int id=Integer.parseInt(id1);
        TDataSource tDataSource=tDataSourceService.findTDatasourceById(id);
        String database_name=tDataSource.getDatabaseName();
        String url=tDataSource.getUrl();
        String user=tDataSource.getUsername();
        String password=tDataSource.getPassword();

        JsonNode tables=rootNode.get("tables");
        if(tables.isArray()){
            for(JsonNode table:tables){
                String  table_name=table.get("table_name").asText();
                String targetTableName=database_name+"_"+table_name;
                boolean res=copyDataFromOneTable2AnotherWithSelectAndCreateSql(database_name,table_name,"yyjh_datasource",targetTableName,user,password,url);
                System.out.println("入库结果："+res);
            }

        }
        result.setCode(DataSourceEnum.SUCCESS.getCode());
        result.setMsg(DataSourceEnum.SUCCESS.getMsg());

        return  result;

    }


    //点击mysql的修改按钮之后，弹出模态框，信息从数据库里面获取出来，然后显示
    @RequestMapping("/mysqlFind/{id}")
    public Object find(@PathVariable("id") Integer id){
        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.SUCCESS.getCode());
        result.setMsg(DataSourceEnum.SUCCESS.getMsg());

        TDataSource tDataSource=tDataSourceService.findTDatasourceById(id);
        String database=tDataSource.getDatabaseName();
        Integer port=tDataSource.getPort();
        String url=tDataSource.getUrl();
        String username=tDataSource.getUsername();
        String password=tDataSource.getPassword();
        String tableName=tDataSource.getAlias();

        // url=jdbc:mysql://127.0.0.1:3306/dormitory?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true
        //截取url里面的ip地址
        //indexOf()返回指定子字符串第一次出现的字符串内的索引。
        int begin=url.indexOf("//");
        //indexOf(String str, int fromIndex)返回指定子串的第一次出现的字符串中的索引，从指定的索引开始。
        int end=url.indexOf(":",begin);
        String ip=url.substring(begin+2,end);

        Map<String,Object> mysql_config=new HashMap<>();
        mysql_config.put("ip",ip);
        mysql_config.put("username",username);
        mysql_config.put("password",password);
        mysql_config.put("port",port);
        mysql_config.put("database_name",database);
        mysql_config.put("table_name",tableName);
        result.setPayload(mysql_config);

        return mysql_config;

    }


    //检验mysql修改是否成功，看输入的database_name、port、ip地址、username、password能不能成功连接，不能连接修改失败
    @RequestMapping("/mysqlUpdate")
    public Object mysqlUpdate(@RequestParam(value = "connect_param") String connect) throws Exception{
        ControllerResult result=new ControllerResult();

        ObjectMapper oMapper=new ObjectMapper();
        JsonNode rootNode=oMapper.readValue(connect, JsonNode.class);
        String  port=rootNode.get("port").asText();
        String  host=rootNode.get("host").asText();
        String database_name=rootNode.get("databaseName").asText();
        String user =rootNode.get("username").asText();
        String password = rootNode.get("password").asText();
        String id1=rootNode.get("id").asText();
        int id=Integer.parseInt(id1);

        String url = "jdbc:mysql://"+host+":"+port+"/"+database_name+"?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        String userId = httpSession.getAttribute("username").toString();
        Connection conn=MysqlOper.getConnection(url,user,password);
        if(conn!=null){
            TDataSource mysql_data=new TDataSource();
            mysql_data.setDatabaseName(database_name);
            mysql_data.setUpdatetime(new Date());
            mysql_data.setUrl(url);
            mysql_data.setPort(Integer.parseInt(port));
            mysql_data.setUsername(user);
            mysql_data.setPassword(password);
            mysql_data.setId(id);
            mysql_data.setUserId(userId);

            //连接成功之后把数据上传到数据库的表里
            //如果表里已经存在url、port、database-name就不能上传
            List<TDataSource> all_datas=tDataSourceService.getAll();

            for (int i=0;i<all_datas.size();i++){
                if(all_datas.get(i).getType().equals("mysql")){
                    if(all_datas.get(i).getDatabaseName().equals(database_name) && all_datas.get(i).getPort()==Integer.parseInt(port) && all_datas.get(i).getUrl().equals(url)){

                        result.setCode(DataSourceEnum.FAIL.getCode());
                        result.setMsg(DataSourceEnum.FAIL.getMsg());
                        return result;
                    }
                }


            }

            boolean flag=tDataSourceService.updTDatasurceById(mysql_data);
            System.out.println(flag);
            result.setCode(DataSourceEnum.SUCCESS.getCode());
            result.setMsg(DataSourceEnum.SUCCESS.getMsg());

        }else{
            result.setCode(DataSourceEnum.EXCEPTION.getCode());
            result.setMsg(DataSourceEnum.EXCEPTION.getMsg());
        }

        return result;
    }



}
