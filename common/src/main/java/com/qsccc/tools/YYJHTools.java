package com.qsccc.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class YYJHTools {

    public static final String DEFAULT_FORMAT_PARAM="yyyy-MM-dd' 'HH:mm:ss";
    /*
    根据格式化参数来格式化日期字符串
    @param  date
    @param param 可以为空
    @return Date

     */
    public  static Date parseStrToDate(String date,String param) throws ParseException {
        /*
        trim()的作用是去掉字符串两端的多余的空格，注意，是两端的空格，且无论两端的空格有多少个都会去掉，
        当然中间的那些空格不会被去掉，如：
        String s = "  a s f g      ";
        String s1 = s.trim();
        那么s1就是"a s f g"，
         */
        if(null==param || "".equals(param.trim()))
            param=YYJHTools.DEFAULT_FORMAT_PARAM;
        DateFormat df=new SimpleDateFormat(param);
        return df.parse(date);

    }

    /**
     * 根据格式化参数来格式化日期对象
     * @param  date
     * @param param
     * @return String
     * **/
    public  static String formatDateToStr(Date date,String param) throws  ParseException{
        if(null==param || "".equals(param.trim()))
            param=YYJHTools.DEFAULT_FORMAT_PARAM;
        DateFormat df=new SimpleDateFormat(param);
        return df.format(date);
    }

    public static URL formatURL(String  url)throws MalformedURLException{
        return new URL(url);
    }

    public static boolean isEmail(String email){
        return email.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    /*
    取出所有的空格，包括字符间的空格
     */
    public static String filterInnerBlank(String str){
        return str.replaceAll(" ","");
    }

    //生成UUID
    public static String  get32UUID(){
        String uuid= UUID.randomUUID().toString().trim().replaceAll("-","");
        return uuid;
    }

}
