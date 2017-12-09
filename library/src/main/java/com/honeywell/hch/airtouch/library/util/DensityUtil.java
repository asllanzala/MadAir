package com.honeywell.hch.airtouch.library.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.honeywell.hch.airtouch.library.LibApplication;

/**
 * Created by nan.liu on 2/2/15.
 */
public class DensityUtil {
    /**
     * convert dp to px according to screen resolution
     */
    public static int dip2px(float dpValue) {
        final float scale = LibApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * convert px to dp according to screen resolution
     */
    public static int px2dip(float pxValue) {
        final float scale = LibApplication.getContext().getResources()
                .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) LibApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
        return width;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) LibApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int height = wm.getDefaultDisplay().getHeight();// 屏幕高度
        return height;
    }

    public static int px2sp(float pxValue) {
        final float fontScale = LibApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp to px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = LibApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 判断是否有底部虚拟导航栏
     * @param activity
     * @return
     */
    public static boolean navigationBarExist2(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

}
