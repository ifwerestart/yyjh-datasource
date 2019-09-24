package com.qsccc.tools;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

public class YYJHToolsTest {

    @Test
    public void parseStrToDate() {
        try{
            String date="2019-9-11 13:25:22";
            Date d=YYJHTools.parseStrToDate(date," ");
            System.out.println(d.getClass().getName());
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Test
    public void formatDateToStr() {
        try{
            Date date=new Date();
            String  d=YYJHTools.formatDateToStr(date,"yyyy-MM-dd");
            System.out.println(d.getClass().getName());
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Test
    public void formatURL() {
        try {
            URL url=YYJHTools.formatURL("http://127.0.1:3306");
            System.out.println(url.getPort());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void isEmail() {
        boolean flag=YYJHTools.isEmail("www.dfffffsa@qq-q.cc");
        System.out.println(flag);
    }

    @Test
    public void filterInnerBlank() {
        System.out.println(YYJHTools.filterInnerBlank("ffs fd  s  "));
    }
}