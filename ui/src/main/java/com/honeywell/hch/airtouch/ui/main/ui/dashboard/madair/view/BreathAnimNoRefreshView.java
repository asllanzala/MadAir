package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by zhujunyu on 2016/11/22.
 */

public class BreathAnimNoRefreshView extends ImageView {
    private Context mContext;
    private int mBreatheFreq;


    private ObjectAnimator objectAnimator;
    private ObjectAnimator objectAnimator2;
    private ObjectAnimator objectAnimator3;
    private AnimatorSet animatorSet;
    private float currentValue = 0;
    private float currentValue2 = 0.9f;
    private float currentValue3 = 0.9f;


    public BreathAnimNoRefreshView(Context context) {
        super(context);
    }

    public BreathAnimNoRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public BreathAnimNoRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }


    private void initView() {

//
//
//        objectAnimator = ObjectAnimator.ofFloat(this, "rotation", currentValue - 360, currentValue);
//        objectAnimator.setInterpolator(new LinearInterpolator());
//        objectAnimator.setRepeatCount(-1);
//        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                currentValue = (float) animation.getAnimatedValue();
//            }
//        });
//
//        objectAnimator2 = ObjectAnimator.ofFloat(this, "ScaleX", currentValue2, 1.0f, 0.8f, currentValue2);
//        objectAnimator2.setInterpolator(new LinearInterpolator());
//        objectAnimator2.setRepeatCount(-1);
//        objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
//        objectAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                currentValue2 = (float) animation.getAnimatedValue();
////                    Log.e("______",""+currentValue2);
//
//            }
//        });
//        objectAnimator3 = ObjectAnimator.ofFloat(this, "ScaleY", currentValue3, 1.0f, 0.8f, currentValue3);
//        objectAnimator3.setInterpolator(new LinearInterpolator());
//        objectAnimator3.setRepeatCount(-1);
//        objectAnimator3.setRepeatMode(ValueAnimator.RESTART);
//        objectAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                currentValue3 = (float) animation.getAnimatedValue();
//
//            }
//        });

//        animatorSet = new AnimatorSet();
//        animatorSet.play(objectAnimator).with(objectAnimator2).with(objectAnimator3);
//        animatorSet.start();
    }


    public void initBreatheFreq() {
        mBreatheFreq = 0;
    }

    public void setRotateSpeed(int breatheFreq) {
        if (breatheFreq <= 0) {
            return;
        }

        if (breatheFreq == mBreatheFreq) {
            return;
        }
        // 已经旋转，不重新调用start

        mBreatheFreq = breatheFreq;
        int duration = 60 / mBreatheFreq;
        int rateDuration = duration * 4 * 1000;
        int scaleDuration = duration * 1000;


        if (objectAnimator != null && objectAnimator2 != null && objectAnimator3 != null && animatorSet != null) {
            objectAnimator.cancel();
            objectAnimator2.cancel();
            objectAnimator3.cancel();
            animatorSet.cancel();
            animatorSet.end();
            objectAnimator = null;
            objectAnimator2 = null;
            objectAnimator3 =null;
            animatorSet =null;
            this.clearAnimation();
        }


//        objectAnimator.setFloatValues(currentValue - 360, currentValue);
//        objectAnimator.setDuration(rateDuration);
//
//        objectAnimator2.setFloatValues(currentValue2, 1.0f, 0.8f, currentValue2);
//        objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
//        objectAnimator2.setDuration(scaleDuration);
//
//
//        objectAnimator3.setFloatValues(currentValue3,1.0f, 0.8f,currentValue3);
//        objectAnimator3.setRepeatMode(ValueAnimator.RESTART);
//        objectAnimator3.setDuration(scaleDuration);

        objectAnimator = ObjectAnimator.ofFloat(this, "rotation", currentValue - 360, currentValue);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setDuration(rateDuration);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
            }
        });


        objectAnimator2 = ObjectAnimator.ofFloat(this, "ScaleX", currentValue2, 1.0f, 0.8f, currentValue2);
        objectAnimator2.setInterpolator(new LinearInterpolator());
        objectAnimator2.setRepeatCount(-1);
        objectAnimator2.setDuration(scaleDuration);
        objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue2 = (float) animation.getAnimatedValue();
//                    Log.e("______",""+currentValue2);

            }
        });

        objectAnimator3 = ObjectAnimator.ofFloat(this, "ScaleY", currentValue3, 1.0f, 0.8f, currentValue3);
        objectAnimator3.setInterpolator(new LinearInterpolator());
        objectAnimator3.setRepeatCount(-1);
        objectAnimator3.setDuration(scaleDuration);
        objectAnimator3.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue3 = (float) animation.getAnimatedValue();
            }
        });




        animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator).with(objectAnimator2).with(objectAnimator3);
        animatorSet.start();

    }
    public void stopBreathAnimation() {
        this.clearAnimation();
    }


}
