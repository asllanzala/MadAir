package com.honeywell.hch.airtouch.ui.control.ui.device.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;

import java.util.ArrayList;

/**
 * Custom view for Air Touch series device control panel.
 * Created by Qian Jin on 9/24/15.
 */
public class AirTouchLedView extends RelativeLayout {
    private final String TAG = "AirTouchLedView";
    private final static int AIRTOUCH_S_MAX_POINT = 14;
    private final static int AIRTOUCH_P_MAX_POINT = 18;
    private final static int AIRTOUCH_F_MAX_POINT = 21;
    private int[] mLedCheckBoxIds = {R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5, R.id.cb6,
            R.id.cb7, R.id.cb8, R.id.cb9, R.id.cb10, R.id.cb11, R.id.cb12, R.id.cb13, R.id.cb14};

    private int[] mLedCheckBoxIdp = {R.id.cb22, R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5, R.id.cb6,
            R.id.cb7, R.id.cb8, R.id.cb9, R.id.cb10, R.id.cb11, R.id.cb12, R.id.cb13, R.id.cb14, R.id.cb15
            , R.id.cb16, R.id.cb23};

    private int[] mLedCheckBoxIdf = {R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5, R.id.cb6,
            R.id.cb7, R.id.cb8, R.id.cb9, R.id.cb10, R.id.cb11, R.id.cb12, R.id.cb13, R.id.cb14,
            R.id.cb15,R.id.cb16,R.id.cb17,R.id.cb18,R.id.cb19,R.id.cb20,R.id.cb21};

    private CheckBox minLedImage, maxLedImage;

    private ImageView mLedFan;

    private RelativeLayout mCheckboxLayout;
    private ArrayList<CheckBox> ledCheckBox = new ArrayList<>();
    private int mMaxLed;
    private Context mContext;

    private int[] mLedCheckBoxList;

    private LayoutParams[] mLedMargin;

    private int mFirstPointX = 0;
    private int mFirstPointY = 0;
    private int mEndPointX = 0;
    private int mEndPointY = 0;

    //private int mWidth, mHeight = 0;

    LayoutParams mParams;

    public AirTouchLedView(Context context) {
        super(context);
        mContext = context;

        initView(context);
    }

    public AirTouchLedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }


    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.device_control_panel_new, this);

        mCheckboxLayout = (RelativeLayout) findViewById(R.id.checkbox_layout);
        mParams = (LayoutParams) mCheckboxLayout.findViewById(R.id.led_fan).getLayoutParams();

        minLedImage = (CheckBox) mCheckboxLayout.findViewById(R.id.device_led_min_iv);
        maxLedImage = (CheckBox) mCheckboxLayout.findViewById(R.id.device_led_max_iv);

        mLedFan = (ImageView) mCheckboxLayout.findViewById(R.id.led_fan);
        mLedFan.setVisibility(GONE);
    }

    public ImageView getLedfan() {
        return mLedFan;
    }

    public void showLedfanPosition(int i) {
        mParams.leftMargin = mLedMargin[i].leftMargin - 29;
        mParams.topMargin = mLedMargin[i].topMargin - 30;
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "i: " + i + " mMaxLed:" + mMaxLed);
        if(mMaxLed == AIRTOUCH_P_MAX_POINT && i == AIRTOUCH_P_MAX_POINT - 1) {
            mParams.leftMargin += 15;
            mParams.topMargin += 15;
        }
        mLedFan.setLayoutParams(mParams);
        mLedFan.setVisibility(VISIBLE);

    }

    public void hideLedfanPosition() {
        mLedFan.setVisibility(GONE);
    }

    public void initLedPosition(int maxNumber, int radius, int yPiexl) {
        double angel = 0.0f;
        int offset = 0;

        mMaxLed = maxNumber;

        mLedMargin = new LayoutParams[mMaxLed];

        switch (maxNumber) {
            case AIRTOUCH_P_MAX_POINT:
                mLedCheckBoxList = mLedCheckBoxIdp;
                findViewById(R.id.cb17).setVisibility(View.GONE);
                findViewById(R.id.cb18).setVisibility(View.GONE);
                findViewById(R.id.cb19).setVisibility(View.GONE);
                findViewById(R.id.cb20).setVisibility(View.GONE);
                findViewById(R.id.cb21).setVisibility(View.GONE);
                angel = Math.PI / 11;
                offset = 3;

                minLedImage.setVisibility(View.INVISIBLE);
                maxLedImage.setVisibility(View.INVISIBLE);

                break;
            case AIRTOUCH_F_MAX_POINT:
                mLedCheckBoxList = mLedCheckBoxIdf;
                findViewById(R.id.cb22).setVisibility(View.GONE);
                findViewById(R.id.cb23).setVisibility(View.GONE);
                angel = Math.PI / 14;
                offset = 3;
                break;
            case AIRTOUCH_S_MAX_POINT:
            default:
                mLedCheckBoxList = mLedCheckBoxIds;
                findViewById(R.id.cb15).setVisibility(View.GONE);
                findViewById(R.id.cb16).setVisibility(View.GONE);
                findViewById(R.id.cb17).setVisibility(View.GONE);
                findViewById(R.id.cb18).setVisibility(View.GONE);
                findViewById(R.id.cb19).setVisibility(View.GONE);
                findViewById(R.id.cb20).setVisibility(View.GONE);
                findViewById(R.id.cb21).setVisibility(View.GONE);
                findViewById(R.id.cb22).setVisibility(View.GONE);
                findViewById(R.id.cb23).setVisibility(View.GONE);
                angel = Math.PI / (mMaxLed - 1);
                offset = 0;
                break;
        }

        for (int id : mLedCheckBoxList) {
            CheckBox led = (CheckBox) findViewById(id);
            ledCheckBox.add(led);
        }

        //the value of x is fixed basically
        int x = DensityUtil.getScreenWidth() / 2 - 5;
        //the value of y is top margin added radius
        int y = yPiexl;
        //the value of radius is adapted to resolution
        int r = radius;


        for (int i = 0; i < mMaxLed; i++) {
            LayoutParams params
                    = new LayoutParams(mCheckboxLayout.getLayoutParams());
            params.leftMargin = (int)(x + r * Math.cos(Math.PI - angel * (i - offset)));
            params.topMargin = (int)(y - r * Math.sin(angel * (i - offset)));
            //to optimized the last max point position when AIRTOUCH_P
            if(maxNumber == AIRTOUCH_P_MAX_POINT && i == mMaxLed - 1) {
                params.leftMargin -= 10;
                params.topMargin -= 10;
            }

            ledCheckBox.get(i).setLayoutParams(params);
            mLedMargin[i] = params;

            if (i == 0) {
                mFirstPointX = params.leftMargin;
                mFirstPointY = params.topMargin;
            }
            if (i == mMaxLed - 1) {
                mEndPointX = params.leftMargin;
                mEndPointY = params.topMargin;
            }
        }

        if(maxNumber == AIRTOUCH_S_MAX_POINT) {
            LayoutParams mParamsMin
                    = new LayoutParams(mCheckboxLayout.getLayoutParams());
            mParamsMin.leftMargin = mFirstPointX + DensityUtil.dip2px(3);
            mParamsMin.topMargin = mFirstPointY + DensityUtil.dip2px(20);
            minLedImage.setLayoutParams(mParamsMin);

            LayoutParams mParamsMax
                    = new LayoutParams(mCheckboxLayout.getLayoutParams());
            mParamsMax.leftMargin = mEndPointX - DensityUtil.dip2px(6);
            mParamsMax.topMargin = mEndPointY + DensityUtil.dip2px(16);
            maxLedImage.setLayoutParams(mParamsMax);
        }  else if(maxNumber == AIRTOUCH_F_MAX_POINT) {
            LayoutParams mParamsMin
                    = new LayoutParams(mCheckboxLayout.getLayoutParams());
            mParamsMin.leftMargin = mFirstPointX + DensityUtil.dip2px(12);
            mParamsMin.topMargin = mFirstPointY + DensityUtil.dip2px(20);
            minLedImage.setLayoutParams(mParamsMin);

            LayoutParams mParamsMax
                    = new LayoutParams(mCheckboxLayout.getLayoutParams());
            mParamsMax.leftMargin = mEndPointX - DensityUtil.dip2px(15);
            mParamsMax.topMargin = mEndPointY + DensityUtil.dip2px(16);
            maxLedImage.setLayoutParams(mParamsMax);
        }

    }


    public int getFirstPointX() {
        return mFirstPointX;
    }

    public void setFirstPointX(int mFirstPointX) {
        this.mFirstPointX = mFirstPointX;
    }

    public int getFirstPointY() {
        return mFirstPointY;
    }

    public void setFirstPointY(int mFirstPointY) {
        this.mFirstPointY = mFirstPointY;
    }

    public int getEndPointX() {
        return mEndPointX;
    }

    public void setEndPointX(int mEndPointX) {
        this.mEndPointX = mEndPointX;
    }

    public int getEndPointY() {
        return mEndPointY;
    }

    public void setEndPointY(int mEndPointY) {
        this.mEndPointY = mEndPointY;
    }

    public ArrayList<CheckBox> getLedCheckBox() {
        return ledCheckBox;
    }
}
