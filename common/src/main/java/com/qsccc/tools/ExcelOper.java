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
                    //getLastCellNum()获取列数，比最后一列列标大1,编号从0开始
                    for (int index = 0; index < row.getLastCellNum(); index++) {
                        //获取列对象
                        Cell cell = row.getCell(index);
                        Object value = getCellValue(cell);
                        //System.out.println("row="+i+" cell=" +index+" value="+value);
                        //if(value.toString().startsWith(start_string) && value.toString().endsWith(end_string) && value.toString().contains(content_string) ){
                        cols.put("col" + (index + 1), value);
                       // }
                        //else{
                         //   data.put("col" + (index + 1),"");
                        //}

                    }
                    sheet_datas.add(cols);
                    /*
                    boolean flag=true;
                    for (Object value : cols.values()) {

                        if(value.equals("")){
                            flag=false;
                        }else{
                            flag=true;
                            break;
                        }
                    }
                    if(flag==true){
                        datas.add(cols);
                    }
                    */


                }
                sheet_alldatas.put("sheet_datas",sheet_datas);
                file_datas.add(sheet_alldatas);
            }
            shuju.put("file_datas",file_datas);
            objs.add(shuju);
        }


        return objs;
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
