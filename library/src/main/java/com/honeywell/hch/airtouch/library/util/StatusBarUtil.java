package com.honeywell.hch.airtouch.library.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;

/**
 * Created by h127856 on 16/8/23.
 */
public class StatusBarUtil {

    private static final int DEFAULT_STATUS_BAR_HEIGHT = 75;

    /**
     * View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 是把状态栏字体和图标变成黑色
     *  0 为设置为系统默认的（白色）SYSTEM_UI_FLAG_VISIBLE
     * @param parentView
     * @param visibility
     */
    public static void changeStatusBarTextColor(View parentView,int visibility){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            parentView.setSystemUiVisibility(visibility);
        }
    }

    public static void hideStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);;
        }
    }

    public static void showStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);;
        }
    }


    public static void initMarginTopWithStatusBarHeight(View view,Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null){
            view.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(view.getLayoutParams());
            rl.topMargin = getStatusBarHeight(context);
            view.setLayoutParams(rl);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    public static void initMarginTopWithStatusBarHeightFrameLayout(View view,Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            FrameLayout.LayoutParams rl = new FrameLayout.LayoutParams(view.getLayoutParams());
            rl.topMargin = getStatusBarHeight(context);
            view.setLayoutParams(rl);
        }
    }


    /**
     * get the height of  status bar
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return DEFAULT_STATUS_BAR_HEIGHT;
        }
    }


    public static int getStatusBarForAndroidM(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getStatusBarHeight(context);
        }
        return 0;
    }


    public static void initViewHeightWithStatusBarHeight(Context context,View view,int resId){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int statusHeight= getStatusBarHeight(context);
            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(resId);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,statusHeight + DensityUtil.dip2px(50));
            relativeLayout.setLayoutParams(layoutParams);
        }


    }


}
