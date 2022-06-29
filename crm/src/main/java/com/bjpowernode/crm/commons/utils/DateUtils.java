package com.bjpowernode.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @BelongsProject: Code
 * @BelongsPackage: com.bjpowernode.crm.commons.utils
 * @Author: dushicheng
 * @CreateTime: 2022-06-14  15:16
 * @Description: TODO
 * @Version: 1.0
 */
public class DateUtils {
    /**
     * @description:对指定的date对象进行格式化
     * @author: dushicheng
     * @date: 2022/6/14 15:23
     * @param: [date]
     * @return: java.lang.String
    **/

    public static String formateDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String formateDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String formateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }



}
