package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.honeywell.hch.airtouch.ui.R;

import java.util.ArrayList;

/**
 * Created by zhujunyu on 2016/11/22.
 */

public class PercentageBar extends View {

    //画线的画笔
    private Paint mLinePaint;
    //画虚线线的画笔
    private Paint mDottedLinePaint;
    //画柱状图的画笔
    private Paint mBarPaint;
    //写字的画笔
    private Paint mTextPaint;
    //底部坐标写字的画笔
    private Paint mTextPaintBottom;
    //开始X坐标
    private int startX;
    //开始Y坐标
    private int startY;
    //结束X坐标
    private int stopX;
    //结束Y坐标
    private int stopY;
    //测量值 宽度
    private int measuredWidth;
    //测量值 高度
    private int measuredHeight;
    //每条柱状图的宽度
    private int barWidth;
    //设置最大值，用于计算比例
    private float max;
    //设置每条柱状图的目标值，除以max即为比例
    private ArrayList<Float> respTarget;
    //设置一共有几条柱状图
    private int totalBarNum;
    //设置每条柱状图的当前比例
    private Float[] currentBarProgress;
    //每条柱状图的名字
    private ArrayList<String> respName;
    //每条竖线之间的间距
    private int deltaX;
    //每条柱状图之间的间距
    private int deltaY;
    //一共有几条竖线
    private int verticalLineNum;
    //底部的短bar的宽度
    private int mBottonBarWidth = 5;
    //纵坐标的文字与 横线之间的间隙
    private int space = 15;

    /**
     * 最大值得比例。如果是mg，默认为1，如果是g,该值为1000
     */
    private int mMaxScale = 1;

    public PercentageBar(Context context) {
        super(context);
        init(context);
    }

    public PercentageBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PercentageBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 测量方法，主要考虑宽和高设置为wrap_content的时候，我们的view的宽高设置为多少
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获得测量后的宽度
        measuredWidth = getMeasuredWidth();
        this.barWidth = measuredWidth * 6 / 275;
        //获得测量后的高度
        measuredHeight = getMeasuredHeight();
        //计算结束X的值(右边空余30)
        stopX = measuredWidth - 45;
        //计算结束Y的值 预留空间写文字
        stopY = measuredHeight - 50;

        deltaY = (stopX - startX - barWidth * totalBarNum) / (totalBarNum + 1);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画柱状图
         */
        for (int i = 0; i < totalBarNum; i++) {
            //添加50的偏移量，为了和横线左边保持一定的距离
            float left = startX + deltaY + i * (deltaY + barWidth);
            //按比例算出
            float top = (respTarget.get(i) / (max * mMaxScale)) * (stopY - startY);

            float right = startX + deltaY + i * (deltaY + barWidth) + barWidth;
            float bottom = stopY;
            canvas.drawRect(left, (stopY - top), right, bottom, mBarPaint);

            if (i == 0 || i == 7 || i == 14 || i == 21 || i == 28) {

                float left2 = (right + left - mBottonBarWidth) / 2;
                float right2 = (right + left - mBottonBarWidth) / 2 + mBottonBarWidth;
                canvas.drawRect(left2, stopY, right2, stopY + 10, mBarPaint);
                canvas.drawText(respName.get(i), left2, stopY + 50, mTextPaintBottom);
            }
        }


        //绘制虚线
        Path path = new Path();
        path.moveTo(startX, (stopY + startY) / 2);
        path.lineTo(stopX, (stopY + startY) / 2);
        canvas.drawPath(path, mDottedLinePaint);
        //绘制虚线
        path.moveTo(startX, startY);
        path.lineTo(stopX, startY);
        canvas.drawPath(path, mDottedLinePaint);
        //画底部横线
        canvas.drawLine(startX, stopY, stopX, stopY, mLinePaint);
        if (max == 0) {
            int topTextLeft = startX - getTextWidth(mTextPaint, "" + 12) / 2 - space;
            canvas.drawText("" + 12, topTextLeft, getBaseLine(mTextPaint, startY), mTextPaint);
        } else {
            int topTextLeft = startX - getTextWidth(mTextPaint, "" + (int) max) / 2 - space;
            canvas.drawText("" + (int) max, topTextLeft, getBaseLine(mTextPaint, startY), mTextPaint);
        }

        //纵坐标中间文字

        if (max == 0) {
            int midTextLeft = startX - getTextWidth(mTextPaint, "" + 6) / 2 - space;
            canvas.drawText("" + 6, midTextLeft, getBaseLine(mTextPaint, (stopY + startY) / 2), mTextPaint);
        } else {
            int midTextLeft = startX - getTextWidth(mTextPaint, "" + (int) max / 2) / 2 - space;
            canvas.drawText("" + (int) max / 2, midTextLeft, getBaseLine(mTextPaint, (stopY + startY) / 2), mTextPaint);
        }

        //纵坐标底部文字
        int bottomTextLeft = startX - getTextWidth(mTextPaint, "0") / 2 - space;
        canvas.drawText("" + 0, bottomTextLeft, getBaseLine(mTextPaint, stopY), mTextPaint);

    }


    private void init(Context context) {
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //初始化柱状图画笔
        mBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarPaint.setColor(context.getResources().getColor(R.color.mad_air_percentage_bar_bg));
        mBarPaint.setStyle(Paint.Style.FILL);
        //初始化线的画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(0xffcdcdcd);
        mLinePaint.setStrokeWidth(2);

        mDottedLinePaint = new Paint();
        mDottedLinePaint.reset();
        mDottedLinePaint.setStyle(Paint.Style.STROKE);
        mDottedLinePaint.setColor(Color.LTGRAY);
        mDottedLinePaint.setAntiAlias(true);
        mDottedLinePaint.setStrokeWidth(2);
        mDottedLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(40);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(0xffababab);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mTextPaintBottom = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaintBottom.setTextSize(40);
        mTextPaintBottom.setStrokeWidth(1);
        mTextPaintBottom.setColor(0xffababab);
        mTextPaintBottom.setTextAlign(Paint.Align.CENTER);

        //设开始X坐标 默认可以显示4位数的长度
//        startX = getTextWidth(mTextPaint, "1000") + space;
        startX = 100;
        //设开始Y坐标为20
        startY = 20;
    }


    /**
     * 设置每个柱状图的宽度
     *
     * @param width
     */
    public void setBarWidth(int width) {
        this.barWidth = width;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(30);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(0xffababab);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 获取文字的宽度
     */
    public int getTextWidth(Paint paint, String text) {
        float textLength = paint.measureText(text);
        return (int) textLength;
    }

    /**
     * 获取文字的底部线
     */
    public float getBaseLine(Paint textPaint, int targetY) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textBaseY = targetY + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
        return textBaseY;
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    public void setMax(float max) {
        this.max = max;
//        startX = getTextWidth(mTextPaint, "" + (int) this.max) + space;
        this.invalidate();
    }

    /**
     * 设置一共有几个柱状图
     *
     * @param totalNum
     */
    public void setTotalBarNum(int totalNum) {
        this.totalBarNum = totalNum;
        currentBarProgress = new Float[totalNum];
        for (int i = 0; i < totalNum; i++) {
            currentBarProgress[i] = 0.0f;
        }
    }

    /**
     * 分别设置每个柱状图的目标值
     *
     * @param respTarget
     */
    public void setRespectTargetNum(ArrayList<Float> respTarget) {
        this.respTarget = respTarget;
    }

    /**
     * 分别设置每个柱状图的名字
     *
     * @param respName
     */
    public void setRespectName(ArrayList<String> respName) {
        this.respName = respName;
    }


    /**
     * 设置有几条竖线
     *
     * @param num
     */
    public void setVerticalLineNum(int num) {
        this.verticalLineNum = num;
    }


    /**
     * 设置最大值得单位比例，如果是mg就为1，g就为1000
     * @param mMaxScale
     */
    public void setMaxScale(int mMaxScale) {
        this.mMaxScale = mMaxScale;
    }
}
