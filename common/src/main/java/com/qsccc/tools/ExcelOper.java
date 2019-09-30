package com.qsccc.tools;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

public class ExcelOper {
    public List<Map<String,Object>>  translateExcels(MultipartFile[] files)  throws Exception{
        List<Map<String,Object>> objs=new ArrayList<>();

        for(MultipartFile f:files){
            Map<String,Object> shuju=new HashMap<>();

            Workbook wb = null;//Excel文档对象
            Sheet sheet = null;//工作表
            // 选择2003/2007，构造Excel文档对象
            String fileName = f.getOriginalFilename();
            InputStream f_in = f.getInputStream();

            shuju.put("file_name",fileName);

            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (suffix.equalsIgnoreCase("xls")) {
                wb = new HSSFWorkbook(new NPOIFSFileSystem(f_in).getRoot(), true);
            } else if (suffix.equalsIgnoreCase("xlsx")) {
                try {
                    wb = new XSSFWorkbook(f_in);
                } catch (Exception ex) {
                    wb = new HSSFWorkbook(f_in);
                }
            }

            //获取Excel中的工作表 getSheet(String name)根据sheet名称来获取

            //获取excel文件里的sheet有几个表
            int count=wb.getNumberOfSheets();
            List<Map<String,Object>> file_datas=new ArrayList<>();
            for(int j=0;j<count;j++){
                sheet = wb.getSheetAt(j);//根据下标获取sheet

                //循环取行对象 sheet.getLastRowNum()获取最后一行的行标
                Map<String,Object> sheet_alldatas=new HashMap<>();
                sheet_alldatas.put("sheet_name",sheet.getSheetName());
                List<Map<String,Object>> sheet_datas=new ArrayList<>();

               // getLastRowNum()获取的是最后一行的编号（编号从0开始）。
                for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                    //每行当做一个Map来存储
                    Row row = sheet.getRow(i);
                    if(row==null){
                        continue;
                    }
                    Map<String, Object> cols = new LinkedHashMap<String, Object>();
                    //循环取列对象
                    //getLastCellNum()获取列数，比最后一列列标大1,
                    for (int index = 0; index < row.getLastCellNum(); index++) {
                        //获取列对象
                        Cell cell = row.getCell(index);
                        Object value = getCellValue(cell);

                        cols.put("col" + (index + 1), value);


                    }
                    sheet_datas.add(cols);



                }
                sheet_alldatas.put("sheet_datas",sheet_datas);
                file_datas.add(sheet_alldatas);
            }
            shuju.put("file_datas",file_datas);
            objs.add(shuju);
        }


        return objs;
    }


    public List<Map<String, Object>> findDate(String path,int start_line,int end_line,List start_string,List end_string,List content_string,String logic) throws Exception {

        File f = new File(this.getClass().getResource("/").getPath());
        int index1 = f.getPath().indexOf("\\");
        String dirve = f.getPath().substring(0,index1);
        //System.out.println("盘符："+dirve);
        String filepath = dirve+path;
        //要读取的文件的路径
        filepath = filepath.replaceAll("/","//");
        //System.out.println("filepath="+filepath);

        File excel_path=new File(filepath);

        List<Map<String, Object>> file_datas = new ArrayList<Map<String, Object>>();

        Workbook wb = null;//Excel文档对象
        Sheet sheet = null;//工作表
        // 选择2003/2007，构造Excel文档对象
        String fileName = excel_path.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (suffix.equalsIgnoreCase("xls")) {
            wb = new HSSFWorkbook(new NPOIFSFileSystem(excel_path).getRoot(), true);
        } else if (suffix.equalsIgnoreCase("xlsx")) {
            try {
                wb = new XSSFWorkbook(excel_path);
            } catch (Exception ex) {
                wb = new HSSFWorkbook(new FileInputStream(excel_path));
            }
        }

        //获取Excel中的工作表 getSheet(String name)根据sheet名称来获取

        //获取excel文件里的sheet有几个表
        int count=wb.getNumberOfSheets();

        for(int j=0;j<count;j++){
            Map<String,Object> sheet_information=new HashMap<>();
            List<Map<String, Object>> sheet_datas = new ArrayList<Map<String, Object>>();
            sheet = wb.getSheetAt(j);//根据下标获取sheet

            //判断sheet表里是不是完全为空
            boolean isnull=false;
            for(int item=0;item<=sheet.getLastRowNum();item++){
                if(sheet.getRow(item)==null){
                    isnull=true;
                }else{
                    isnull=false;
                    break;
                }
            }
            //如果最后判断出这个sheet表是完全为空，就不用添加到file_dats列表里
            if(isnull==true){
                continue;
            }

            String sheet_name=sheet.getSheetName();
            sheet_information.put("sheet_name",sheet_name);

            //获取第一行有多少列，getLastCellNum()获取列数，比最后一列列标大1
            int colNumber=sheet.getRow(0).getLastCellNum();
            sheet_information.put("col_length",colNumber);

            //循环取行对象 sheet.getLastRowNum()获取最后一行的行标
            if(end_line==-1){ //表示最后一行的行号没有写，就默认是读所有的行
                end_line=sheet.getLastRowNum();
            }
            for (int i = start_line; i <= end_line; i++) {
                //每行当做一个Map来存储
                Row row = sheet.getRow(i);
                if(row==null){
                    System.out.println("这一行都为空");
                    continue;
                }

                Map<String, Object> data = new LinkedHashMap<String, Object>();
                //循环取列对象
                for (int index = 0; index < row.getLastCellNum(); index++) {
                    //获取列对象
                    Cell cell = row.getCell(index);
                    if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK){
                        Object value = getCellValue(cell);
                        //逻辑是and的时候，必须满足所有条件的时候，就可以添加进去，当一个条件不满足时就用“”代替
                        if(logic.equals("and")){
                            boolean judge=false;
                            for(int k=0;k<start_string.size();k++){
                                if((value!=null && value!="" )&& value.toString().startsWith(start_string.get(k).toString()) && value.toString().endsWith(end_string.get(k).toString()) && value.toString().contains(content_string.get(k).toString()) ){
                                    judge=true;
                                }
                                else{
                                    judge=false;
                                    data.put("col" + (index + 1),"");
                                    break;
                                }
                            }
                            if (judge==true)
                                data.put("col" + (index + 1), value);

                        }
                        //逻辑是or的时候，只要满足其中一组条件的时候，就可以添加进去，当任意一个条件都不满足时就用“”代替
                        else if(logic.equals("or")){
                            boolean judge=false;
                            for(int k=0;k<start_string.size();k++){
                                if((value!=null && value!="" )&& value.toString().startsWith(start_string.get(k).toString()) && value.toString().endsWith(end_string.get(k).toString()) && value.toString().contains(content_string.get(k).toString()) ){
                                    judge=true;
                                    data.put("col" + (index + 1), value);
                                    break;
                                }
                                else{
                                    judge=false;
                                }
                            }
                            if (judge==false)
                                data.put("col" + (index + 1),"");

                        }
                        //如果逻辑为空，也就是没有过滤信息的时候，全部插入进去
                        else{
                            data.put("col" + (index + 1), value);
                        }
                    }//如果当前列不为空

                } //遍历当前行的列

                //System.out.println("data="+data);

                //如果当前行的所有列的值都是空，就不添加进去
                boolean flag=true;
                if(data.isEmpty()){
                    flag=false;
                }

                for (Object value : data.values()) {

                    if(value.equals("")){
                        flag=false;
                    }else{
                        flag=true;
                        break;
                    }
                }
                if(flag==true){
                    sheet_datas.add(data);
                }


            } //遍历当前sheet表里的行
            sheet_information.put("sheet_datas",sheet_datas);
            file_datas.add(sheet_information);
        } //遍历文件里的sheet

        return file_datas;
    }

    //判断类型
    private Object getCellValue(Cell cell) {
        Object result = "";
        //判断列类型
        if (null != cell) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC://判断是否数值
                    result = getCellByDate(cell);
                    break;
                case Cell.CELL_TYPE_BOOLEAN://判断是布尔型
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_STRING://判断是字符串型
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA://判断是数字
                    result = cell.getNumericCellValue();
                    break;
                default:
                    result = "";
                    break;
            }
        }

        return result;
    }


    private Object getCellByDate(Cell cell) {
        Object obj = null;
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            obj = date;
        } else {
            DecimalFormat df = new DecimalFormat("0");
            // 如果是纯数字
            double value = cell.getNumericCellValue();
            int i = (int) value;
            // 判断是整数还是小数
            if (i == value) {
                obj = i;
            } else {
                obj = df.format(cell.getNumericCellValue());
            }
        }
        return obj;
    }


    public static void main(String[] args) {
        File file = new File("d:" + File.separator +"IntelliJ IDEA2018"+File.separator+"excel"+File.separator+ "test1.xlsx");

        ExcelOper et = new ExcelOper();
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        try {
            MultipartFile file1[]=new MultipartFile[2];
            //file1[0]=new MultipartFile("D:\\IntelliJ IDEA2018\\excel\\test1.xlsx");
            //file1[1]=new MultipartFile("D:\\IntelliJ IDEA2018\\excel\\test2.xls");

            datas = et.translateExcels(file1);//解析
            System.out.println(datas);
            //循环查看

            /*
            for (int i = 0; i < datas.size(); i++) {
                Map<String, Object> data = datas.get(i);
                Collection<Object> ds = data.values();
                //获取数据
                for (Object d : ds)
                    System.out.print(d + " ");
                System.out.println();
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
