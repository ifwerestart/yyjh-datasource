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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel")
public class ExcelSourceController {
    @Autowired
    private TDataSourceService tDataSourceService;
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

                String real_path=excel_path+File.separator+uuid+suffix;

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
    @PostMapping(value = "previewExcel")
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
}
