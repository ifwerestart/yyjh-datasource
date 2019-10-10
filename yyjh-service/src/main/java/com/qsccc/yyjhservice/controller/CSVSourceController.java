package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qsccc.tools.YYJHTools;
import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import com.qsccc.yyjhservice.enumeration.DataSourceEnum;
import com.qsccc.yyjhservice.service.datasource.CSVSourceService;
import com.qsccc.yyjhservice.service.datasource.TDataSourceService;
import com.qsccc.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/csv")
public class CSVSourceController {

    @Value("${file.upload.path.datasource.csv}")
    private String csv_path;
    @Autowired
    private TDataSourceService tDataSourceService;
    @Autowired
    private CSVSourceService csvSourceService;
    @Autowired
    private HttpSession httpSession;

    //CSV上传
    @PostMapping(value = "/csvUpload")
    public Object csvUpload(@RequestParam("csv_upload") MultipartFile[] files)
            throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());
        if (null != files && files.length != 0) {
            //创建目录
            File directory = new File(csv_path);
            if (!directory.exists())
                directory.mkdirs();
            //遍历
            for (MultipartFile file : files) {
                //得到上传时的文件名
                String original = file.getOriginalFilename();

                String suffix = original.substring(original.lastIndexOf("."));
                if (!".csv".equals(suffix.trim()))
                    continue;
                //生成UUID
                String uuid = YYJHTools.get32UUID();
                String real_path = csv_path + "/" + uuid + suffix;
                File csv = new File(real_path);
                //存datasource库，存文件
                FileUtils.copyInputStreamToFile(file.getInputStream(), csv);
                TDataSource csv_data = new TDataSource();
                csv_data.setDatabaseName(uuid);
                csv_data.setType("csv");
                csv_data.setUrl(real_path);
                csv_data.setAlias(original);
                Date date = new Date();
                csv_data.setCreatetime(date);
                String username = httpSession.getAttribute("username").toString();
                csv_data.setUserId(username);
                boolean flag = tDataSourceService.addTDatasource(csv_data);
                if (flag) {
                    result.setCode(DataSourceEnum.SUCCESS.getCode());
                    result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                } else {
                    result.setCode(DataSourceEnum.FAIL.getCode());
                    result.setMsg(DataSourceEnum.FAIL.getMsg());
                }
            }

        }
        return result;
    }

    //CSV预览
    @RequestMapping(value = "/previewCSV",produces = "application/json;charset=utf-8")
    public Object previewCSV(
            @RequestParam("csv_upload") MultipartFile[] files,
            @RequestParam("csv_interpret") String interpret
    ) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());
        if (null != files && files.length != 0) {
            ObjectMapper om = new ObjectMapper();   //ObjectMapper提供一些功能将转换成Java对象匹配JSON(JSON 是String)结构,反之亦然
            ArrayNode datas = om.createArrayNode();
            //循环File
            for (MultipartFile file : files) {
                if (file.isEmpty())
                    continue;
                ObjectNode json_file = om.createObjectNode();
                ArrayNode file_datas = om.createArrayNode();
                //流操作
                InputStream in = file.getInputStream();   //读取文件
                Reader r_in = new InputStreamReader(in, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(r_in);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(interpret);
                    int index = 1;
                    String[] cols = line.split(interpret);
                    System.out.println(line);
                    ObjectNode json_cols = om.createObjectNode();
                    for (String col : cols) {//列
                        json_cols.put("col" + index, col);
                        index++;
                    }
                    file_datas.add(json_cols);//行
                    System.out.println(json_cols);

                }

                json_file.put("file_name", file.getOriginalFilename());
                json_file.put("file_datas", file_datas);
                datas.add(json_file);
            }
            //返回数据
            result.setCode(DataSourceEnum.SUCCESS.getCode());
            result.setMsg(DataSourceEnum.SUCCESS.getMsg());
            result.setPayload(datas);

        }
        return result;
    }




    //CSV入库
    @GetMapping(value = "/insertCSV/{id}",produces = "application/json;charset=utf-8")
    public Object insertCSV(@PathVariable Integer id) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());
        //获取文件路径
        TDataSource tDatasource =  tDataSourceService.findTDatasourceById(id);
        String pathname = tDatasource.getUrl();

        //获取存的数据库表名
        String databaseName= tDatasource.getDatabaseName();
        //查看数据库中是否存在该表名，存在则无法入库，返回 数据重复
        System.out.println(csvSourceService.isTableExist(databaseName));
        if(csvSourceService.isTableExist(databaseName)){
            result.setCode(DataSourceEnum.DUPLICATION.getCode());
            result.setMsg(DataSourceEnum.DUPLICATION.getMsg());
            return result;
        }

        //读取File文件
        File file = new File(pathname);
        InputStreamReader reader=new InputStreamReader(new FileInputStream(file),"UTF-8");
        BufferedReader bfreader=new BufferedReader(reader);
        String line;
        //data用于存放csv数据的每一行
        Map<String,Object> data = new LinkedHashMap<>();
        int index2 = 1;
        //length用于保存第一行的列数
        int length = 0;
        while((line=bfreader.readLine())!=null) {//包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
            int index = 1;
            String[] cols = line.split(",");
            for (String col : cols) {//列
                data.put("col" + index, col);
                index++;
            }
            //根据第一行的数据创建表,第一行多少列数据整个表就多少列数据
            if(index2==1){
                //保存第一行的列数
                length  = data.size();
                //拼接sql语句创建csv表
                String sql = "CREATE TABLE "+databaseName+"(";
                int temp = 1;
                for (String key : data.keySet()) {
                    if(temp==1){
                        sql += "`"+key+"` varchar(100) ";
                    }else{
                        sql += ",`"+key+"` varchar(100) ";
                    }
                    temp++;
                }
                sql += ")";
                csvSourceService.createTable(sql);
                index2++;
            }

            //通过拼接sql语句实现csv数据入库
            int count = 0;
            String sql2 = "insert into "+databaseName+" (";
            for (String key : data.keySet()) {
                count ++;
                if(count>length||count>index-1){
                    break;
                }
                if(count==index-1){
                    sql2 += "`"+key+"`)";
                }else{
                    sql2 += "`"+key+"`" +",";
                }

            }

            count = 0;
            sql2 += "VALUES (";
            for (String key : data.keySet()) {
                count ++;
                if(count>length||count>index-1){
                    break;
                }
                if(count==index-1){
                    sql2 += "'"+data.get(key)+"')";
                }else{
                    sql2 += "'"+data.get(key)+"'" +",";
                }

            }
            //数据入库
            boolean flag = csvSourceService.addCSVData(sql2);
            if(flag){
                result.setCode(DataSourceEnum.SUCCESS.getCode());
                result.setMsg(DataSourceEnum.SUCCESS.getMsg());
            }
        }

        return result;
    }



    //CSV文件修改
    @RequestMapping(value = "/updateCSV",produces = "application/json;charset=utf-8")
    public Object updateCSV(@RequestParam("csv_upload") MultipartFile file,@RequestParam("csv_id") Integer id) throws IOException {
        ControllerResult result = new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());



        //获取UUID
        TDataSource tDatasource =  tDataSourceService.findTDatasourceById(id);
        String uuid = tDatasource.getDatabaseName();

        //删除原文件
        String url = tDatasource.getUrl();
        File csv_file = new File(url);
        csv_file.delete();

        //创建目录
        File directory = new File(csv_path);
        if (!directory.exists())
            directory.mkdirs();

        //获取新文件名称
        String original = file.getOriginalFilename();

        String suffix = original.substring(original.lastIndexOf("."));

        String real_path = csv_path + "/" + uuid + suffix;
        File csv = new File(real_path);

        //存datasource库，改文件
        FileUtils.copyInputStreamToFile(file.getInputStream(), csv);
        TDataSource csv_data = new TDataSource();
        csv_data.setId(id);
        csv_data.setDatabaseName(uuid);
        csv_data.setType("csv");
        csv_data.setUrl(real_path);
        csv_data.setAlias(original);
        Date date = new Date();
        csv_data.setUpdatetime(date);
        boolean flag = tDataSourceService.updTDatasurceById(csv_data);
        if (flag) {
            result.setCode(DataSourceEnum.SUCCESS.getCode());
            result.setMsg(DataSourceEnum.SUCCESS.getMsg());
        } else {
            result.setCode(DataSourceEnum.FAIL.getCode());
            result.setMsg(DataSourceEnum.FAIL.getMsg());
        }


        return result;
    }
}
