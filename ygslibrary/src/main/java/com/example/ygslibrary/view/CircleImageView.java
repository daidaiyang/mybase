package com.example.ygslibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;

import com.example.ygslibrary.R;


/**
 * Created by Administrator on 2017-11-14.
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView{

    private int mBorderThicknewss = 0;
    private Context context;
    //默认颜色
    private int defaultColor = 0xFFFFFFFF;
    //圆形外边框
    private int mBorderOutsideColor = 0;
    //圆形内边框
    private int mBorderInsideColor = 0;
    //控件默认的长和宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;


    public CircleImageView(Context context) {
        super(context);
        this.context= context;
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, Context context1) {
        super(context, attrs, defStyleAttr);
        this.context = context1;
    }



    private void setCustomAttributes(AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.roundedimageview);
        mBorderThicknewss = array.getDimensionPixelSize(R.styleable.roundedimageview_border_thickness,0);
        mBorderOutsideColor = array.getDimensionPixelSize(R.styleable.roundedimageview_border_outside_color,0);
        mBorderInsideColor = array.getDimensionPixelSize(R.styleable.roundedimageview_border_inside_color,0);
        array.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        if (getWidth()==0||getHeight()==0)
            return;
        this.measure(0,0);
        if (drawable.getClass() == NinePatchDrawable.class)
            return;

        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888,true);
        if (defaultWidth==0)
            defaultWidth = getWidth();
        if (defaultHeight == 0)
            defaultHeight = getHeight();

        int radius = 0;

        if (mBorderInsideColor!=defaultColor&&mBorderOutsideColor!=defaultColor){
            radius = (defaultWidth<defaultHeight?defaultWidth:defaultHeight)/2-2*mBorderThicknewss;
            //画外圆
            drawCircleBorder(canvas,radius+mBorderThicknewss+mBorderThicknewss/2,mBorderOutsideColor);
            //画外圆
            drawCircleBorder(canvas,radius+mBorderThicknewss/2,mBorderInsideColor);
        }else if (mBorderInsideColor != defaultColor
                && mBorderOutsideColor == defaultColor){//内边框
            radius = (defaultWidth < defaultHeight ? defaultWidth
                    : defaultHeight) / 2 - mBorderThicknewss;

            drawCircleBorder(canvas, radius + mBorderThicknewss / 2,
                    mBorderInsideColor);
        }else if (mBorderInsideColor == defaultColor
                && mBorderOutsideColor != defaultColor){//外边框
            radius = (defaultWidth < defaultHeight ? defaultWidth
                    : defaultHeight) / 2 - mBorderThicknewss;

            drawCircleBorder(canvas, radius + mBorderThicknewss / 2,
                    mBorderOutsideColor);
        }else{//无边框
            radius = (defaultWidth < defaultHeight ? defaultWidth
                    : defaultHeight) / 2;
        }
        Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);

        canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight
                / 2 - radius, null);
    }


    /**
     *
     * 获取裁剪后的圆形图片
     *
     */

    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {

        Bitmap scaledSrcBmp;

        int diameter = radius * 2;

        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片

        int bmpWidth = bmp.getWidth();

        int bmpHeight = bmp.getHeight();

        int squareWidth = 0, squareHeight = 0;

        int x = 0, y = 0;

        Bitmap squareBitmap;

        if (bmpHeight > bmpWidth) {// 高大于宽

            squareWidth = squareHeight = bmpWidth;

            x = 0;

            y = (bmpHeight - bmpWidth) / 2;

            // 截取正方形图片

            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);

        } else if (bmpHeight < bmpWidth) {// 宽大于高

            squareWidth = squareHeight = bmpHeight;

            x = (bmpWidth - bmpHeight) / 2;

            y = 0;

            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);

        } else {

            squareBitmap = bmp;

        }

        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {

            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
                    diameter, true);

        } else {

            scaledSrcBmp = squareBitmap;

        }

        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),

                scaledSrcBmp.getHeight(),

                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();

        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);

        paint.setFilterBitmap(true);

        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,

                scaledSrcBmp.getHeight() / 2,

                scaledSrcBmp.getWidth() / 2,

                paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

        bmp = null;

        squareBitmap = null;

        scaledSrcBmp = null;

        return output;

    }


    //边缘画圆
    private void drawCircleBorder(Canvas canvas, int radius, int color){
        Paint paint = new Paint();
        //去锯齿
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        //设置style为空心
        paint.setStyle(Paint.Style.STROKE);
        //设置paint的外框宽度
        paint.setStrokeWidth(mBorderThicknewss);
        canvas.drawCircle(defaultWidth/2,defaultHeight/2,radius,paint);
    }
}
