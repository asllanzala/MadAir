package com.honeywell.hch.airtouch.ui.main.ui.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.honeywell.hch.airtouch.library.util.LogUtil;


/**
 * Created by wuyuan on 15/5/12.
 */
public class CustomViewPager extends ViewPager
{

    private boolean isCanScroll = true;

    public CustomViewPager(Context context)
    {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll)
    {
        this.isCanScroll = isCanScroll;
    }


    @Override
    public void scrollTo(int x, int y)
    {
        if (isCanScroll)
        {
            super.scrollTo(x, y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
		/* return false;//super.onTouchEvent(arg0); */
        if (!isCanScroll)
            return false;
        return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!isCanScroll)
            return false;
        else
        {
            try {
                return super.onInterceptTouchEvent(arg0);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public void onDraw(Canvas canvas){
        try{
            super.onDraw(canvas);
        }
        catch (NullPointerException e){
            LogUtil.log(LogUtil.LogLevel.ERROR,"CustomViewPager","onDraw NullPointerException = " + e.toString());
        }
    }
}