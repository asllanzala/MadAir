package com.honeywell.hch.airtouch.ui.common.ui.view;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;

/**
 * Created by Qian Jin on 4/15/16.
 */
public class DropDownAnimationManager {

    private Animation mTranslateInAnimation;
    private Animation mTranslateOutAnimation;
//    private static AnimationCallback mAnimationCallback;

    private RelativeLayout mDropDownMessageLayout;
    private TextView mDropDownMessageTextView;
    private final int EMPTY = 0;
    private final int TEXTSIZE = 16;
    private final int DROPLAYOUTHEIGHT =Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
            -StatusBarUtil.getStatusBarHeight(AppManager.getInstance().getApplication().getApplicationContext()) - DensityUtil.dip2px(53)  : -DensityUtil.dip2px(53);;
    private final int LAYOUTHEIGHT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
            StatusBarUtil.getStatusBarHeight(AppManager.getInstance().getApplication().getApplicationContext()) + DensityUtil.dip2px(52)  : DensityUtil.dip2px(52);
    private Context mContext;


    RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            LAYOUTHEIGHT);

    RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);


    public DropDownAnimationManager() {
    }

    public DropDownAnimationManager(ViewGroup mRelativeLayout, Context mContext) {
        initView(mContext);
        initRemindRelativeLayout(mRelativeLayout, mContext);
    }

//    public interface AnimationCallback {
//        void onShow(boolean visibility);
//    }
//
//    public static void setAnimationCallback(AnimationCallback animationCallback) {
//        mAnimationCallback = animationCallback;
//    }

    /**
     * Animation helper
     */
    private class translateInAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            mDropDownMessageLayout.clearAnimation();
            mDropDownMessageLayout.startAnimation(mTranslateOutAnimation);
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation arg0) {
            // TODO Auto-generated method stub
            StatusBarUtil.hideStatusBar((Activity) mContext);
        }
    }

    private class translateOutAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            StatusBarUtil.showStatusBar((Activity) mContext);
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation arg0) {
            // TODO Auto-generated method stub
        }
    }

    private void initView(Context mContext) {
        this.mContext = mContext;
        mTranslateInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.control_translate_in);
        mTranslateOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.control_translate_out);
        mTranslateInAnimation.setAnimationListener(new translateInAnimationListener());
        mTranslateOutAnimation.setAnimationListener(new translateOutAnimationListener());
    }

    protected void initRemindRelativeLayout(ViewGroup mRelativeLayout, Context mContext) {
        mDropDownMessageLayout = new RelativeLayout(mContext);
        mDropDownMessageTextView = new TextView(mContext);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mLayoutParams.setMargins(EMPTY, DROPLAYOUTHEIGHT, EMPTY, EMPTY);

        mDropDownMessageTextView.setTextColor(mContext.getResources().getColor(R.color.white));
        mDropDownMessageTextView.setTextSize(TEXTSIZE);
        mDropDownMessageTextView.setGravity(Gravity.CENTER);
        mDropDownMessageTextView.setLayoutParams(textViewParams);//

        mDropDownMessageLayout.setLayoutParams(mLayoutParams);
        mDropDownMessageLayout.addView(mDropDownMessageTextView);
        mRelativeLayout.addView(mDropDownMessageLayout);
    }

    public void showDragDownLayout(String message, boolean isErrMsg) {
        mDropDownMessageLayout.clearAnimation();
        if (!StringUtil.isEmpty(message)) {
            if (isErrMsg) {
                mDropDownMessageLayout.setBackgroundColor(mContext.getResources().getColor(R.color.top_back_abnormal));
            } else {
                mDropDownMessageLayout.setBackgroundColor(mContext.getResources().getColor(R.color.top_back_normal));
            }
            mDropDownMessageTextView.setText(message);

            mDropDownMessageLayout.startAnimation(mTranslateInAnimation);
        }
    }
}
