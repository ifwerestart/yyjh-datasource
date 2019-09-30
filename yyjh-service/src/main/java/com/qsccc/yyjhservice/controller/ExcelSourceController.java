package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qsccc.tools.ExcelOper;
import com.qsccc.tools.YYJHTools;
import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import com.qsccc.yyjhservice.enumeration.DataSourceEnum;
import com.qsccc.yyjhservice.service.datasource.TDataSourceService;
import com.qsccc.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/excel")
public class ExcelSourceController {
    @Autowired
    private TDataSourceService tDataSourceService;
   // @Autowired
    //private ExcelDatasService excelDatasService;

    @Value("${file.upload.path.datasource.excel}")
    private  String excel_path;

    //上传
    @PostMapping(value = "/excelUpload")
    public Object excelUpload(@RequestParam("excel_upload") MultipartFile[] files,
                              @RequestParam("excel_interpret") String excel_interpret) throws IOException {
        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());

        if(null!=files && files.length!=0){
            //创建目录

            File directory=new File(excel_path);
            if(!directory.exists()){
                directory.mkdirs();
            }
            //遍历
            for(MultipartFile file:files){

                //获取文件的原始文件名，getOriginalFilename : 获取上传文件的原名，不会是全部路径名
                String orignal=file.getOriginalFilename();

                //先获取到"."所在的最后位置，然后从这个位置开始到最后截取子字符串给变量，这样就能获取到文件的后缀名了
                String suffix=orignal.substring(orignal.lastIndexOf("."));

                if(! (".xls".equals(suffix.trim()) || ".xlsx".equals(suffix.trim()) )){  //如果文件名后缀不是xls或者xlsx类型的，就跳过下面的代码，继续执行for循环
                    continue;
                }
                //生成UUID，唯一的标识码，随机生成，保证不重复
                String uuid= YYJHTools.get32UUID();

                String real_path=excel_path+"/"+uuid+suffix;

                File excel=new File(real_path);
                //存库，存文件
                FileUtils.copyInputStreamToFile(file.getInputStream(),excel);

                TDataSource excel_data=new TDataSource();
                excel_data.setDatabaseName(uuid+suffix);
                excel_data.setType("excel");
                excel_data.setUrl(real_path);
                excel_data.setAlias(orignal);//存的是别名

                ObjectMapper oMapper=new ObjectMapper();
                JsonNode rootNode=oMapper.readTree(excel_interpret);

                //List<Map<String,Object>> all=oMapper.readValue(excel_interpret, new TypeReference<List<Map<String,Object>>>() {});

                if (rootNode.isArray()) {
                    for (JsonNode objNode : rootNode) {

                        String filename =objNode.get("filename").asText();
                        if(filename.equals(orignal)){
                            //System.out.println("这是objNode："+objNode);
                            //System.out.println("这是转成字符串："+oMapper.writeValueAsString(objNode));
                            excel_data.setEncode(oMapper.writeValueAsString(objNode));
                            break;
                        }

                    }
                }

                excel_data.setCreatetime(new Date());

                boolean flag=tDataSourceService.addTDatasource(excel_data);

                if(flag){
                    result.setCode(DataSourceEnum.SUCCESS.getCode());
                    result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                }else{
                    result.setCode(DataSourceEnum.FAIL.getCode());
                    result.setMsg(DataSourceEnum.FAIL.getMsg());
                }

            }

        }
        return result;
    }

    //预览
    @PostMapping(value = "/previewExcel")
    public Object previewExcel(@RequestParam("excel_upload") MultipartFile[] files) throws Exception {
        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());

        if(null!=files && files.length!=0){
            //解析返回list

            ExcelOper oper=new ExcelOper();
            List<Map<String,Object>> datas= oper.translateExcels(files);
            result.setCode(DataSourceEnum.SUCCESS.getCode());
            result.setMsg(DataSourceEnum.SUCCESS.getMsg());
            result.setPayload(datas);
        }

        return  result;

    }


    //入库,把选择的excel表复制到yyjh_datasource数据库里，如果选择的这个库跟表格已经在yyjh_datasource里存在了，就不能入库
    @RequestMapping(value = "/excelEnter/{id}")
    public Object enterExcel(@PathVariable Integer id) throws  Exception{

        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.FAIL.getCode());
        result.setMsg(DataSourceEnum.FAIL.getMsg());
        ExcelOper excelTest = new ExcelOper();

        TDataSource tDataSource=tDataSourceService.findTDatasourceById(id);
        String path = tDataSource.getUrl();
        String database_name =tDataSource.getDatabaseName();
        int position = database_name.indexOf(".");
        String tableNameSuffix = database_name.substring(0, position);
        String encode=tDataSource.getEncode();
        //System.out.println(encode);


        ObjectMapper om = new ObjectMapper();
        JsonNode rootNode=om.readTree(encode);
        //List<Map<String,Object>> enter_datas = om.readValue(json,new TypeReference<List<Map<String,Object>>>() { });

        String isfilter = rootNode.get("isfilter").asText();

        int start1 = 0, end1 = -1;

        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

        if (rootNode.has("interpret_filter")) {
            JsonNode objNode = rootNode.get("interpret_filter");

            String start_line = objNode.get("start_line").asText();
            String end_line = objNode.get("end_line").asText();
            if (start_line != "") {
                start1 = Integer.valueOf(start_line);
            }
            if (end_line != "") {
                end1 = Integer.valueOf(end_line);
            }

            String index_logic = objNode.get("index_logic").asText();
            JsonNode arrNode = objNode.get("index_string");
            List start_string = new ArrayList();
            List end_string = new ArrayList();
            List content_string = new ArrayList();


            if (arrNode.isArray()) { //表示判断这个json
                for (JsonNode obj : arrNode) {
                    start_string.add(obj.get("start_string").asText());
                    end_string.add(obj.get("end_string").asText());
                    content_string.add(obj.get("content_string").asText());
                }
            }

            datas = excelTest.findDate(path, start1, end1, start_string, end_string, content_string, index_logic);

        } //如果有过滤的信息
        else {
            datas = excelTest.findDate(path, start1, end1, null, null, null, "null");
        }


        for (int k = 0; k < datas.size(); k++) {
            String sheetname = datas.get(k).get("sheet_name").toString();
            String createTableName = tableNameSuffix + "_" + sheetname;

            //检验要创建的表名跟数据库中已经存在的表相同，就不能入库
            List<String> allTables=tDataSourceService.findAllTables("yyjh_datasource");
            boolean isExists=false;
            for(int i=0;i<allTables.size();i++){
                String existTableName=allTables.get(i).toString();

                if(createTableName.equals(existTableName)){
                    //如果现在要入库的表名在数据库里已经存在了，就不能入库
                    isExists=true;
                    break;
                }
            }
            if(isExists==false){
                int cols_number = Integer.parseInt(datas.get(k).get("col_length").toString());
                //构造生成表的字段值
                List cols = new ArrayList();
                for (int index = 1; index <= cols_number; index++)
                    cols.add("col" + index);

                //创建表
                boolean createResult = tDataSourceService.createAutoTable(createTableName, cols);

                List<Map<String,Object>> sheet_datas=( List<Map<String,Object>>)datas.get(k).get("sheet_datas");

                boolean insertAllResult = true;
                for (int j = 0; j < sheet_datas.size(); j++) {
                    //遍历map的所有value
                    List res = new ArrayList();
                    for (Object vals : sheet_datas.get(j).values()) {
                        res.add(vals.toString());
                    }
                    boolean addResult = tDataSourceService.addByFilter(createTableName, res);
                    if (addResult == false) {
                        insertAllResult = false;
                    }
                }

                if (createResult == true && insertAllResult == true) {
                    result.setCode(DataSourceEnum.SUCCESS.getCode());
                    result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                }

            }//如果表没有入库过，就创建表

            else{ //如果表已经入库过，就让这一行数据变灰色，不能点击入库
                result.setCode(DataSourceEnum.FAIL.getCode());
                result.setMsg(DataSourceEnum.FAIL.getMsg());
                return result;
            }




        }//遍历，看这个文件里有几个sheet



        return result;
    }

    //修改，选择新的excel文件之后，把alias的名字替换成当前选择的文件名，然后根据这一行的url，打开文件，替换文件里面的内容
    @RequestMapping(value = "/excelUpdate")
    public  Object excelUpdate(@RequestParam("excel_upload") MultipartFile[] files,
                               @RequestParam("excel_interpret") String excel_interpret,
                               @RequestParam("id") String json) throws Exception{

        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());

        ObjectMapper om=new ObjectMapper();
        JsonNode rootNode1=om.readValue(json,JsonNode.class);
        String id1=rootNode1.get("id").asText();
        int id=Integer.parseInt(id1);

        if(null!=files && files.length!=0){
            //创建目录

            File directory=new File(excel_path);
            if(!directory.exists()){
                directory.mkdirs();
            }
            //遍历
            for(MultipartFile file:files){

                //获取文件的原始文件名，getOriginalFilename : 获取上传文件的原名，不会是全部路径名
                String orignal=file.getOriginalFilename();
                //System.out.println("上传文件名："+orignal);

                //先获取到"."所在的最后位置，然后从这个位置开始到最后截取子字符串给变量，这样就能获取到文件的后缀名了
                String suffix=orignal.substring(orignal.lastIndexOf("."));

                if(! (".xls".equals(suffix.trim()) || ".xlsx".equals(suffix.trim()) )){  //如果文件名后缀不是xls或者xlsx类型的，就跳过下面的代码，继续执行for循环
                    continue;
                }

                TDataSource tDataSource=tDataSourceService.findTDatasourceById(id);
                String database_name=tDataSource.getDatabaseName();
                String url=tDataSource.getUrl();

                //获取database_name的前缀名字，是uuid随机码
                String database_name_prefix=database_name.substring(0,database_name.indexOf("."));
                //把前缀跟现在选择修改的文件的后缀连起来，修改成新的database_name
                String update_database_name=database_name_prefix+suffix;
                //System.out.println("修改之后的文件名："+update_database_name);

                String url_prefix=url.substring(0,url.lastIndexOf("."));
                //获取原本的文件名的后缀
                String original_suffix=database_name.substring(database_name.indexOf("."));
                String update_url=url_prefix+suffix;
                //System.out.println("修改之后的url："+update_url);

                TDataSource excel_data=new TDataSource();
                excel_data.setAlias(orignal);//存的是别名
                excel_data.setDatabaseName(update_database_name);
                excel_data.setUrl(update_url);

                ObjectMapper oMapper=new ObjectMapper();
                JsonNode rootNode=oMapper.readTree(excel_interpret);

                if (rootNode.isArray()) {
                    for (JsonNode objNode : rootNode) {
                        String filename =objNode.get("filename").asText();
                        if(filename.equals(orignal)){
                            excel_data.setEncode(oMapper.writeValueAsString(objNode));
                            break;
                        }

                    }
                }

                excel_data.setUpdatetime(new Date());
                excel_data.setId(id);

                //修改id对应的那一行数据里的alias、encode、updateTime
                boolean flag=tDataSourceService.updTDatasurceById(excel_data);

                if(flag){
                    result.setCode(DataSourceEnum.SUCCESS.getCode());
                    result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                }else{
                    result.setCode(DataSourceEnum.FAIL.getCode());
                    result.setMsg(DataSourceEnum.FAIL.getMsg());
                }

                //修改完t_datasource表里面的数据之后，开始更新url所对应的文件里的内容

                //先获取盘符
                File f = new File(this.getClass().getResource("/").getPath());
                int index1 = f.getPath().indexOf("\\");
                String dirve = f.getPath().substring(0,index1);
                //把盘符跟url拼接起来的是要找的那个文件路径
                String filepath = dirve+update_url;
                //要读取的文件的路径
                filepath = filepath.replaceAll("/","//");
                //System.out.println("filepath="+filepath);
                File excel_realpath=new File(filepath);
                //存库，存文件
                FileUtils.copyInputStreamToFile(file.getInputStream(),excel_realpath);

                //如果修改前后的文件后缀名不一样，修改之后还要把旧的文件删除
                if(!original_suffix.equals(suffix)){
                    String old_file_path=dirve+url;
                    old_file_path=old_file_path.replaceAll("/","//");
                    //System.out.println("旧文件路径："+old_file_path);
                    File old_file = new File(old_file_path);
                    if (old_file.isFile() && old_file.exists()) {
                        old_file.delete();
                    }
                }


                //修改之后还要把数据库中的旧表删除了





            }

        }

        return result;
    }


}
