package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by zhujunyu on 2016/11/22.
 */

public class BreathAnimView extends ImageView {

    private Context mContext;
    private int mBreatheFreq ;

    private Animation rotateAnimation;
    private Animation scaleAnimation;
//    private RotateAnimation

    private AnimationSet mAnimationSet;

    public BreathAnimView(Context context) {
        super(context);
    }

    public BreathAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public BreathAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }


    private void initView() {
        rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.breath_animation_rotate);
        scaleAnimation =  AnimationUtils.loadAnimation(mContext, R.anim.breath_animation_scale);

        mAnimationSet = new AnimationSet(false);
        mAnimationSet.addAnimation(rotateAnimation);
        mAnimationSet.addAnimation(scaleAnimation);
    }

    public void initBreatheFreq(){
        mBreatheFreq = 0;
    }

    public void setRotateSpeed(int breatheFreq) {
        if (breatheFreq <= 0) {
            return;
        }

        if(breatheFreq == mBreatheFreq){
            return;
        }

        mBreatheFreq = breatheFreq;



        this.stopBreathAnimation();
        int duration = 60 / mBreatheFreq;
        int rateDuration = duration * 4 * 1000;
        int scaleDuration = duration * 1000 /2;

        //如果animation停止的状态，就需要进行
//        rotateAnimation.getStartOffset()
        scaleAnimation.setDuration(scaleDuration);
//        scaleAnimation.setDuration(scaleDuration);
        rotateAnimation.setDuration(rateDuration);
        if (this.getAnimation() == null) {
            this.clearAnimation();
            startAnimation();
        }
    }

    public void stopBreathAnimation() {
        this.clearAnimation();
    }


    private void startAnimation() {
        this.startAnimation(mAnimationSet);
    }


    public enum ROATATESPEED {
        HIGH_SPEED, MID_SPEED, LOW_SPEED
    }
}
