package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.qsccc.tools.YYJHTools;
import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import com.qsccc.yyjhservice.enumeration.DataSourceEnum;
import com.qsccc.yyjhservice.service.datasource.TDataSourceService;
import com.qsccc.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@RestController
@RequestMapping("/csv")
public class CSVSourceController {

    @Autowired
    private TDataSourceService tDataSourceService;

    @Value("${file.upload.path.datasource.csv}")
    private  String csv_path;

    @RequestMapping("/csvUpload")
    //csv上传
    public  Object csvUpload(@RequestParam("csv_upload")MultipartFile[] files) throws IOException {

        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());
        if(null!=files && files.length!=0){
            //创建目录
            File directory=new File(csv_path);
            if(!directory.exists()){
                directory.mkdirs();
            }
            //遍历
            for(MultipartFile file:files){

                //获取文件的原始文件名，getOriginalFilename : 获取上传文件的原名，不会是全部路径名
                String orignal=file.getOriginalFilename();

                //先获取到"."所在的最后位置，然后从这个位置开始到最后截取子字符串给变量，这样就能获取到文件的后缀名了
                String suffix=orignal.substring(orignal.lastIndexOf("."));

                if(!".csv".equals(suffix.trim())){  //如果文件名后缀不是csv类型的，就跳过下面的代码，继续执行for循环
                    continue;
                }
                //生成UUID，唯一的标识码，随机生成，保证不重复
                String uuid= YYJHTools.get32UUID();

                String real_path=csv_path+File.separator+uuid+suffix;

                File csv=new File(real_path);
                //存库，存文件
                FileUtils.copyInputStreamToFile(file.getInputStream(),csv);

                TDataSource csv_data=new TDataSource();
                csv_data.setDatabaseName(uuid+suffix);
                csv_data.setType("csv");
                csv_data.setUrl(real_path);
                csv_data.setAlias(orignal);//存的是别名
                csv_data.setCreatetime(new Date());
                boolean flag=tDataSourceService.addTDatasource(csv_data);

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


    //csv预览
    @RequestMapping(value="/previewCSV",produces = "application/json;charset=utf-8")
    public  Object previewCSV(@RequestParam("csv_upload") MultipartFile[] files,
                              @RequestParam("csv_interpret") String interpret) throws IOException {

        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());

        if(null!=files && files.length!=0){
            ObjectMapper om=new ObjectMapper();
            //payload对应的值是json数组
            ArrayNode datas=om.createArrayNode();

            for(MultipartFile file:files){

                if(file.isEmpty())
                    continue;

                //json_file对应的值是json对象，有两个key，是file_name 和 file_datas
                ObjectNode json_file=om.createObjectNode();
                //file_datas对应的值是json数组，数组里每一个json对象存的是列，总共多少个json对象就是有多少行
                ArrayNode file_datas=om.createArrayNode();

                //流操作
                InputStream in=file.getInputStream();
                //Reader r_in = new InputStreamReader(in, StandardCharsets.UTF_8);
                Reader r_in=new InputStreamReader(in,"utf-8");
                BufferedReader bufferedReader=new BufferedReader(r_in);

                String  line="";
                while((line=bufferedReader.readLine())!=null){
                    int index=1;
                    String[] cols = line.split(interpret);
                    //json_cols对应的是json对象，里面的key存的是列号，值是列里面的内容
                    ObjectNode json_cols=om.createObjectNode();

                    for(String  col:cols){//列
                        json_cols.put("col"+index,col);
                        index++;
                    }
                    file_datas.add(json_cols);//行
                }
                json_file.put("file_name",file.getOriginalFilename());
                json_file.put("file_datas",file_datas);
                datas.add(json_file);

            }
            //返回数据
            result.setCode(DataSourceEnum.SUCCESS.getCode());
            result.setMsg(DataSourceEnum.SUCCESS.getMsg());
            result.setPayload(datas);

        }
        return  result;

    }
}
