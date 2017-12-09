package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.honeywell.hch.airtouch.library.util.LogUtil;

/**
 * Created by Qian Jin on 12/1/16.
 */

public class MadAirScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;
    public Button mAddCityBtn;
    private int mScrllChangedY;
    private RelativeLayout mCityRl;

    public MadAirScrollView(Context context) {
        super(context);
    }

    public MadAirScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MadAirScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface ScrollViewListener {
        void onScrollChanged(MadAirScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        mScrllChangedY = y;
        super.onScrollChanged(x, y, oldX, oldY);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float motionEventX = ev.getX();
        float motionEventY = ev.getY();
        float mAddCityBtnLeft = mAddCityBtn.getX();
        float mAddCityBtnRight = mAddCityBtn.getX() + mAddCityBtn.getWidth();
        float mAddCityBtnTop = mAddCityBtn.getY();
        float mAddCityBtnButtom = mAddCityBtn.getY() + mAddCityBtn.getHeight();
        float heightDistance = mCityRl.getHeight() - mAddCityBtnButtom;
        LogUtil.log(LogUtil.LogLevel.DEBUG, "MadAirScrollView", "mScrllChangedY: " + mScrllChangedY);
        LogUtil.log(LogUtil.LogLevel.DEBUG, "MadAirScrollView", "heightDistance: " + heightDistance);
        if (mAddCityBtn.getVisibility() == VISIBLE && motionEventY < mAddCityBtnButtom && motionEventY > mAddCityBtnTop
                && motionEventX > mAddCityBtnLeft && motionEventX < mAddCityBtnRight && mScrllChangedY < heightDistance) {
            return false;
        } else {
            return super.dispatchTouchEvent(ev);
        }

    }

    public void setAddCityButton(Button addCityBtn) {
        this.mAddCityBtn = addCityBtn;
    }

    public void setmCityRl(RelativeLayout mCityRl) {
        this.mCityRl = mCityRl;
    }
}