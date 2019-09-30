package com.qsccc.yyjhservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsccc.tools.ExcelOper;
import com.qsccc.tools.YYJHTools;
import com.qsccc.yyjhservice.domain.datasource.TDataSource;
import com.qsccc.yyjhservice.enumeration.DataSourceEnum;
import com.qsccc.yyjhservice.service.datasource.TDataSourceService;
import com.qsccc.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/img")
public class ImgUploadController {

    @Value("${file.upload.path.datasource.img}")
    private  String img_path;



    //上传
    @PostMapping(value = "/imgUpload")
    public Object excelUpload(@RequestParam("image_upload") MultipartFile[] files) throws IOException {
        String real_path = "";
        System.out.println("11111111111111111111111");
        ControllerResult result=new ControllerResult();
        result.setCode(DataSourceEnum.NOFILE.getCode());
        result.setMsg(DataSourceEnum.NOFILE.getMsg());
       // System.out.println("11111111111111111111111"+files.length);
        if(null!=files && files.length!=0){
            //创建目录
         //   System.out.println("222222222222222222");
            File directory=new File(img_path);
            if(!directory.exists()){
                directory.mkdirs();
            }
            //遍历
            for(MultipartFile file:files){
            //    System.out.println("33333333333333333");
                //获取文件的原始文件名，getOriginalFilename : 获取上传文件的原名，不会是全部路径名
                String orignal=file.getOriginalFilename();

                //先获取到"."所在的最后位置，然后从这个位置开始到最后截取子字符串给变量，这样就能获取到文件的后缀名了
                String suffix=orignal.substring(orignal.lastIndexOf("."));

                if(! (".jpg".equals(suffix.trim()))){  //如果文件名后缀不是xls或者xlsx类型的，就跳过下面的代码，继续执行for循环
                    continue;
                }
                //生成UUID，唯一的标识码，随机生成，保证不重复
                String uuid= YYJHTools.get32UUID();

                String real_path1=img_path+File.separator+uuid+suffix;

                real_path= uuid+suffix;

                File img=new File(real_path1);
                //存库，存文件
                FileUtils.copyInputStreamToFile(file.getInputStream(),img);

            //    System.out.println(real_path);

            //    System.out.println("成功！");
                result.setCode(DataSourceEnum.SUCCESS.getCode());
                result.setMsg(DataSourceEnum.SUCCESS.getMsg());
                result.setPayload(real_path);
            }

        }
       // System.out.println("real_path:"+real_path);
        return result;
    }

}
