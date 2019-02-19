package com.example.ygslibrary.view.wheelview;

import android.content.Context;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Common {

    public static String defaultYear;
    public static String defaultMonth = "6月";
    public static String defaultDay = "19日";
    public static String defaultWeight = "55.0kg";
    public static String defaultheight = "160cm";
    public static String defaultZhiWu = "商务";
    public static final Date datefromComStr(String s){
        s = s.substring(0,s.length()-1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd", Locale.CHINA);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(s, pos);
        return strtodate;
    }

    public static final String dateToStr(Date time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        return sdf.format(time);
    }

    public static ArrayList<String> buildWeight(){
        int low = 35;
        int height = 300;
        ArrayList<String> weightList = new ArrayList<>();
        for (int i = low;i<=height;i++){
            weightList.add(i+".0kg");
            weightList.add(i+".5"+"kg");
        }
        return weightList;
    }

    public static ArrayList<String> buildHeight(){
        Integer low = 100;
        Integer height = 250;
        ArrayList<String> heightList = new ArrayList<>();
        for (int i = low;i<=height;i++){
            heightList.add(i+"cm");
        }
        return heightList;
    }

    public static ArrayList<String> buildZhiwu(){
        ArrayList<String> zhiwutList = new ArrayList<>();
        zhiwutList.add("客服");
        zhiwutList.add("结算");
        zhiwutList.add("运营");
        zhiwutList.add("商务");
        zhiwutList.add("技术");
        zhiwutList.add("项目");
        return zhiwutList;
    }



    public static ArrayList<String> buildYear(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy",Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        Integer end_time = Integer.parseInt(dateFormat.format(curDate));
        Integer start_time = end_time -130;
        defaultYear = start_time+105+"年";
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = start_time;i<=end_time;i++){
            yearList.add(i+"年");
        }
        return yearList;
    }

    public static ArrayList<String> buildMonthOfYear(){
        ArrayList<String> monthList = new ArrayList<>();
        for (int i=1;i<=12;i++){
            monthList.add(i+"月");
        }
        return monthList;
    }

    public static ArrayList<String> buildDayOfMonth(String year,String month){
        year = year.substring(0,year.length()-1);
        month = month.substring(0,month.length()-1);
        int selectYear = Integer.parseInt(year);
        int selectMonth = Integer.parseInt(month);
        ArrayList<String> DayList = new ArrayList<>();
        switch (selectMonth){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                for (int i=1;i<=31;i++){
                    DayList.add(i+"日");
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                for (int i=1;i<=30;i++){
                    DayList.add(i+"日");
                }
                break;
            case 2:
                if ((selectYear%4==0&&selectYear%100!=0)||selectYear%400==0){
                    for (int i=1;i<=29;i++){
                        DayList.add(i+"日");
                    }
                }else{
                    for (int i=1;i<=28;i++){
                        DayList.add(i+"日");
                    }
                }
                break;


        }
        return DayList;



    }





    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //判断两个日期是否在同一天
    public static boolean isInSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2
                .get(Calendar.DAY_OF_MONTH);
    }

    //判断两个日期是否位于同一小时
    public static boolean isInSameHour(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY);

    }
}
