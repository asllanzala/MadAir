package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

/**
 * Created by h127856 on 16/8/29.
 * 为了解决设置了透明状态栏 （getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)）后导致
 * adjustResize 失效，不会再键盘弹起后，把界面上推
 */
public class SoftInputAdjustTopView extends RelativeLayout {

    private int[] mInsets = new int[4];

    public SoftInputAdjustTopView(Context context) {
        super(context);
    }

    public SoftInputAdjustTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftInputAdjustTopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }

    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mInsets[0] = insets.getSystemWindowInsetLeft();
            mInsets[1] = insets.getSystemWindowInsetTop();
            mInsets[2] = insets.getSystemWindowInsetRight();
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
                    insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }
}
