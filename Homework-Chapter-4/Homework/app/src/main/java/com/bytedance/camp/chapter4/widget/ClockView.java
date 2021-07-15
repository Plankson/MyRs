package com.bytedance.camp.chapter4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.camp.chapter4.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClockView extends View {

    private static final int FULL_CIRCLE_DEGREE = 360;
    private static final int UNIT_DEGREE = 6;

    private static final float UNIT_LINE_WIDTH = 8; // 刻度线的宽度
    private static final int HIGHLIGHT_UNIT_ALPHA = 0xFF;
    private static final int NORMAL_UNIT_ALPHA = 0x80;
    private static final float HOUR_NEEDLE_LENGTH_RATIO = 0.4f; // 时针长度相对表盘半径的比例
    private static final float MINUTE_NEEDLE_LENGTH_RATIO = 0.6f; // 分针长度相对表盘半径的比例
    private static final float SECOND_NEEDLE_LENGTH_RATIO = 0.8f; // 秒针长度相对表盘半径的比例
    private static final float HOUR_NEEDLE_WIDTH = 12; // 时针的宽度
    private static final float MINUTE_NEEDLE_WIDTH = 8; // 分针的宽度
    private static final float SECOND_NEEDLE_WIDTH = 4; // 秒针的宽度

    private Calendar calendar = Calendar.getInstance();

    private static final float sm_r=20;

    private float radius = 0; // 表盘半径
    private float centerX = 0; // 表盘圆心X坐标
    private float centerY = 0; // 表盘圆心Y坐标

    private List<RectF> unitLinePositions = new ArrayList<>();
    private Paint unitPaint = new Paint();
    private Paint topPaint= new Paint();
    private Paint needlePaint = new Paint();
    private Paint numberPaint = new Paint();
    private Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            invalidate();
        }
    };

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        unitPaint.setAntiAlias(true);
        unitPaint.setColor(Color.WHITE);
        unitPaint.setStrokeWidth(UNIT_LINE_WIDTH);
        unitPaint.setStrokeCap(Paint.Cap.ROUND);
        unitPaint.setStyle(Paint.Style.STROKE);
        needlePaint.setColor(Color.WHITE);
        topPaint.setAntiAlias(true);
        // TODO 设置绘制时、分、秒针的画笔: needlePaint
        needlePaint.setAntiAlias(true);
        needlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        needlePaint.setStrokeCap(Paint.Cap.ROUND);
        needlePaint.setColor(Color.WHITE);

        // TODO 设置绘制时间数字的画笔: numberPaint
        numberPaint.setAntiAlias(true);
        numberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        numberPaint.setTextSize(50);
        numberPaint.setColor(Color.WHITE);
        numberPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        configWhenLayoutChanged();
    }

    private void configWhenLayoutChanged() {
        float newRadius = Math.min(getWidth(), getHeight()) / 2f;
        if (newRadius == radius) {
            return;
        }
        radius = newRadius;
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;

        // 当视图的宽高确定后就可以提前计算表盘的刻度线的起止坐标了
        for (int degree = 0; degree < FULL_CIRCLE_DEGREE; degree += UNIT_DEGREE) {
            double radians = Math.toRadians(degree);
            float startX = (float) (centerX + (radius * (1 - 0.05f)) * Math.cos(radians));
            float startY = (float) (centerY + (radius * (1 - 0.05f)) * Math.sin(radians));
            float stopX = (float) (centerX + radius * Math.cos(radians));
            float stopY = (float) (centerY + radius * Math.sin(radians));
            unitLinePositions.add(new RectF(startX, startY, stopX, stopY));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawUnit(canvas);
        drawTimeNeedles(canvas);
        drawTimeNumbers(canvas);
        handler.sendEmptyMessage(0);
    }

    // 绘制表盘上的刻度+中间的点
    private void drawUnit(Canvas canvas) {
        topPaint.setColor(Color.GRAY);
        topPaint.setStrokeWidth(5);
        topPaint.setStrokeCap(Paint.Cap.ROUND);
        topPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX,centerY,sm_r,topPaint);
        topPaint.setColor(Color.WHITE);
        topPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(centerX,centerY,sm_r+3,topPaint);

        for (int i = 0; i < unitLinePositions.size(); i++) {
            if (i % 5 == 0) {
                unitPaint.setAlpha(HIGHLIGHT_UNIT_ALPHA);
            } else {
                unitPaint.setAlpha(NORMAL_UNIT_ALPHA);
            }
            RectF linePosition = unitLinePositions.get(i);
            canvas.drawLine(linePosition.left, linePosition.top, linePosition.right, linePosition.bottom, unitPaint);
        }
    }

    private void drawTimeNeedles(Canvas canvas) {
        Time time = getCurrentTime();
        int hour = time.getHours();
        int minute = time.getMinutes();
        int second = time.getSeconds();
        int ms=time.getMs();
        // TODO 根据当前时间，绘制时针、分针、秒针
        float s=second*6f+ms*0.006f-90f;
        float m=minute*6f+second*0.1f-90f;
        float h=hour*30f+minute*0.5f+second*0.008333333f-90f;

        double the_s=Math.toRadians(s);
        double the_m=Math.toRadians(m);
        double the_h=Math.toRadians(h);
        //画时针

        needlePaint.setStrokeWidth(HOUR_NEEDLE_WIDTH);
        float hstopX = (float) (centerX +HOUR_NEEDLE_LENGTH_RATIO* radius * Math.cos(the_h));
        float hstopY = (float) (centerY +HOUR_NEEDLE_LENGTH_RATIO* radius * Math.sin(the_h));
        canvas.drawLine(centerX,centerY,hstopX,hstopY,needlePaint);
        needlePaint.setStrokeWidth(MINUTE_NEEDLE_WIDTH);
        float mstopX = (float) (centerX +MINUTE_NEEDLE_LENGTH_RATIO* radius * Math.cos(the_m));
        float mstopY = (float) (centerY +MINUTE_NEEDLE_LENGTH_RATIO* radius * Math.sin(the_m));
        canvas.drawLine(centerX,centerY,mstopX,mstopY,needlePaint);
        needlePaint.setStrokeWidth(SECOND_NEEDLE_WIDTH);
        float sstopX = (float) (centerX +SECOND_NEEDLE_LENGTH_RATIO* radius * Math.cos(the_s));
        float sstopY = (float) (centerY +SECOND_NEEDLE_LENGTH_RATIO* radius * Math.sin(the_s));
        canvas.drawLine(centerX,centerY,sstopX,sstopY,needlePaint);
        /**
         * 思路：
         * 1、以时针为例，计算从0点（12点）到当前时间，时针需要转动的角度
         * 2、根据转动角度、时针长度和圆心坐标计算出时针终点坐标（起始点为圆心）
         * 3、从圆心到终点画一条线，此为时针
         * 注1：计算时针转动角度时要把时和分都得考虑进去
         * 注2：计算坐标时需要用到正余弦计算，请用Math.sin()和Math.cos()方法
         * 注3：Math.sin()和Math.cos()方法计算时使用不是角度而是弧度，所以需要先把角度转换成弧度，
         *     可以使用Math.toRadians()方法转换，例如Math.toRadians(180) = 3.1415926...(PI)
         * 注4：Android视图坐标系的0度方向是从圆心指向表盘3点方向，指向表盘的0点时是-90度或270度方向，要注意角度的转换
         */
        // int hourDegree = 180;
        // float endX = (float) (centerX + radius * HOUR_NEEDLE_LENGTH_RATIO * Math.cos(Math.toRadians(hourDegree)))
    }

    private void drawTimeNumbers(Canvas canvas) {
        numberPaint.setTextSize(50);
        numberPaint.getFontMetrics(fontMetrics);
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        // 粗糙绘制
        for (int i = 0; i < 12; i++) {
            float hourDegree = i * 30 - 60;
            String number = String.format("%02d",i + 1);
            float textWidth = numberPaint.measureText(number);
            canvas.drawText(
                    number,
                    (float) (centerX + (radius * 0.8f + textWidth / 2f) * Math.cos(Math.toRadians(hourDegree))),
                    (float) (centerY+15 + (radius * 0.8 + textHeight / 2f) * Math.sin(Math.toRadians(hourDegree))),
                    numberPaint
            );
        }
        numberPaint.setTextSize(75);
        Calendar c=Calendar.getInstance();
        String timestage=String.format("%d年 %d 月 %d 日 %02d:%02d:%02d",c.get(Calendar.YEAR),
                c.get(Calendar.MONTH)+1,c.get(Calendar.DATE),c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
        canvas.drawText(timestage,
                (float) (centerX ),
                (float) (centerY-radius-50),
                numberPaint);

    }

    // 获取当前的时间：时、分、秒
    private Time getCurrentTime() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        return new Time(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND));
    }
}
