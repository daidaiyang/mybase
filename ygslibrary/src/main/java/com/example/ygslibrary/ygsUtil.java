package com.example.ygslibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ygsUtil {


    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    //仿快速点击
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 获取两位小数
     */
    public static String get2xiaoshu(double d) {
        DecimalFormat    df   = new DecimalFormat("#####0.00");
        String format = df.format(d);
        return format;
    }

    /**
     * 价格前面大后面小
     * @param price  价格+价格后面的字符串
     * @param num 小数点后几位小一号字体
     * @return
     */
    public static SpannableString price2Spanned(String price, int num) {
        SpannableString spannableString = new SpannableString(price);
        spannableString.setSpan(new AbsoluteSizeSpan(15, true), 0, price.indexOf("."), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(13, true), price.indexOf("."), price.length()-num, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }


    /**
     *      * 当前日期加上天数后的日期
     *      * @param num 为增加的天数
     *     * @return
     *
     */
    public static String plusDay2(Long s, int num, String type) {
        Date d = new Date(s);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currdate = format.format(d);
        System.out.println("现在的日期是：" + currdate);

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        d = ca.getTime();
        SimpleDateFormat formatget = new SimpleDateFormat(type);
        String enddate = formatget.format(d);
        String format1 = format.format(d);
        System.out.println("增加天数以后的日期：" + format1);
        return enddate;
    }

    /**
     *两个时间戳计算天数
     */

    public static int daydiff(Date fDate, Date oDate) {
        int days = (int) ((oDate.getTime() - fDate.getTime()) / (1000*3600*24));
        return days;

    }


    /**
     * 网络图片转换成数组并压缩，适用于微信分享
     * @param url 图片地址
     * @return
     */
    public static byte[] getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (bm!=null){
            return Bitmap3Bytes(bm);
        }else{
            return null;
        }
    }
    public static byte[] Bitmap3Bytes(Bitmap bmp) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0,i, j), null);
            bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 30,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
              /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
                return baos.toByteArray();*/
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 时间转字符串
     * @param date 时间
     * @param timeFormat 时间格式
     * @return
     */
    public static String getTime(Date date, String timeFormat) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(date);
    }
}
