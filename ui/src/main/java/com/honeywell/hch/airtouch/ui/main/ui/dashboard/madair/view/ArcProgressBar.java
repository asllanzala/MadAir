package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by zhujunyu on 2016/11/28.
 */

public class ArcProgressBar extends View {
    //直径
    private int diameter = 30;

    private static final int HINT_STRING_MARGIN = 10;
    //圆心
    private float centerX;
    private float centerY;

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;

    private float startAngle = 0;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    //    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 20;
    private float bgArcWidth = dipToPx(6);
    private float progressWidth = dipToPx(8);
    private float textSize = dipToPx(34);
    private float hintSize = dipToPx(13);
    private int aniSpeed = 1000;
    private float longdegree = dipToPx(13);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);
    private String hintColor = "#676767";
    private String longDegreeColor = "#111111";
    private String shortDegreeColor = "#111111";
    private int bgArcColor;
    private int currentProcessColor;
    private boolean isShowCurrentSpeed = true;
    private String hintString = "";
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;

    // 如果没有数值，显示"---"
    private boolean mIsNoData;

    //圆心太靠下，所以加入一个向上的偏移量
    private int mCircleCenterDelta = DensityUtil.dip2px(10);

    // sweepAngle / maxValues 的值
    private float k;


    public ArcProgressBar(Context context) {
        super(context);
        initView();
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCofig(context, attrs);
        initView();
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
//        bgArcColor = a.getColor(R.styleable.ColorArcProgressBar_back_color, Color.BLACK);
        currentProcessColor = a.getColor(R.styleable.ColorArcProgressBar_process_color, Color.GREEN);
        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
//        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2));
//        progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10));
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);

        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);

        //增加一个空格，避免%和数字间距过短
        hintString = " " + a.getString(R.styleable.ColorArcProgressBar_string_unit);
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 100);
        setCurrentValues(String.valueOf(curValues));
        setMaxValues(maxValues);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) - mCircleCenterDelta;
        setMeasuredDimension(width, height);
    }

    private void initView() {

        diameter = 6 * getScreenWidth() / 28;
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE - mCircleCenterDelta;
        bgRect.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE) - mCircleCenterDelta;

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2 - mCircleCenterDelta;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
//        allArcPaint.setColor(bgArcColor);
        allArcPaint.setColor(getContext().getResources().getColor(R.color.mad_air_battery_progress_grey));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(currentProcessColor);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.BLACK);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setTextAlign(Paint.Align.CENTER);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                    degreePaint.setColor(Color.parseColor(longDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE, centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dipToPx(1.4f));
                    degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2, centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree, degreePaint);
                }

                canvas.rotate(9, centerX, centerY);
            }
        }

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);


        if (currentAngle > 72) {
            progressPaint.setColor(getContext().getResources().getColor(R.color.mad_air_battery_progress_green));
        } else {
            progressPaint.setColor(getContext().getResources().getColor(R.color.mad_air_battery_progress_red));
        }
        //当前进度

        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);
        Paint.FontMetricsInt fontMetrics = vTextPaint.getFontMetricsInt();
        int baseline = (int) ((bgRect.bottom + bgRect.top - fontMetrics.bottom - fontMetrics.top) / 2);

        if (isNeedContent) {
            // 如果没有数值则显示"---"
            if (mIsNoData) {

//                isNeedUnit = false;
                vTextPaint.setColor(getContext().getResources().getColor(R.color.ds_clean_now));
                canvas.drawText(getContext().getString(R.string.mad_air_dashboard_no_data), centerX, baseline, vTextPaint);

            } else {
                vTextPaint.setColor(Color.BLACK);
                canvas.drawText(String.format("%.0f", curValues), getValueTextDrawPosition(), baseline, vTextPaint);
                canvas.drawText(hintString, getValueTextDrawPosition() + getTextWidth() / 2 + HINT_STRING_MARGIN, baseline, hintPaint);
            }
        }

//        if (isNeedUnit) {
//            //文字设置在内容的右下方
//            canvas.drawText(hintString, getValueTextDrawPosition() + getTextWidth() / 2 + 10, baseline, hintPaint);
//        }
//        invalidate();
    }


    public float getValueTextDrawPosition() {
      float hintCenter =  hintPaint.measureText(hintString)/2;
        return centerX - hintCenter;
    }

    /**
     * 获取文字的宽度
     */
    public int getTextWidth() {
        float textLength = vTextPaint.measureText(String.format("%.0f", curValues));
        return (int) textLength;
    }

    /**
     * 获取文字的高度
     */
    public int getTextHeight() {
        Rect rect = new Rect();
        vTextPaint.getTextBounds(String.format("%.0f", curValues), 0, String.format("%.0f", curValues).length(), rect);
        return rect.height();
    }

    /**
     * 设置最大值
     *
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle / maxValues;

    }

    /**
     * 设置当前值
     *
     * @param currentValue
     */
    public void setCurrentValues(String currentValue) {
        //不是数字字串
        if (!isNumberString(currentValue)) {
            mIsNoData = true;
            currentValue = "0";
        }else{
            mIsNoData = false;
        }
        float currentValues = Float.parseFloat(currentValue);
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     *
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     *
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置直径大小
     *
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
//        progressAnimator = ValueAnimator.ofFloat(last, current);
//        progressAnimator.setDuration(length);
//        progressAnimator.setTarget(currentAngle);
//        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                currentAngle = (float) animation.getAnimatedValue();
//                curValues = currentAngle / k;
//            }
//        });
//        progressAnimator.start();
////        currentAngle = curValues/100 * 360;
        currentAngle = curValues * k;
        invalidate();
    }


    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    private boolean isNumberString(String testString) {
        String numAllString = "0123456789";
        if (testString == null || testString.length() <= 0)
            return false;
        for (int i = 0; i < testString.length(); i++) {
            String charInString = testString.substring(i, i + 1);
            if (!numAllString.contains(charInString))
                return false;
        }
        return true;
    }

}
