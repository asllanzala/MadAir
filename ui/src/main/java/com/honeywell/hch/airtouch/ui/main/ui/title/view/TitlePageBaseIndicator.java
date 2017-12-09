/*
 * Copyright (C) 2011 Jake Wharton
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Francisco Figueiredo Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.honeywell.hch.airtouch.ui.main.ui.title.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainBaseActivity;
import com.honeywell.hch.airtouch.ui.main.ui.common.view.CustomViewPager;
import com.honeywell.hch.airtouch.ui.main.ui.common.view.ViewPager;

/**
 *
 */
public class TitlePageBaseIndicator extends RelativeLayout implements PageIndicator {

    /**
     * 避免滑动界面的时候，动画会重新启动，导致给用户卡顿的感觉
     */
    private static boolean isHasRotating = false;

    private ViewPager.OnPageChangeListener mListener;
    protected int mCurrentPage = 0;
    private int mScrollState;
    protected Context mContext;
    protected Activity mParentActivity;

    protected CustomViewPager mHomeViewPager;
    protected int lastValue = -1;
    protected boolean isSmallToBig = false;
    protected boolean isBigToSmall = false;
    protected ImageView mDropDownImageView;
    protected boolean isDropDownOrNot = true;
    protected LinearLayout mHomeNameLayout;

    //第一次进入应用，网络数据加载成功后显示
    protected LinearLayout mLoadingNetworkSuccssLayout;

    //第一次进入应用，cache数据加载成功后显示
    protected LinearLayout mLoadingCacheDataSuccessLayout;

    protected ImageView mCacheLoadingImageView;

    protected TextView mLoadingMsgTextView;



    public TitlePageBaseIndicator(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public TitlePageBaseIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }


    public void setActivity(Activity activity){
        mParentActivity = activity;
    }

    /**
     * 子类需要override这个方法
     */
    protected void initView() {
        mLoadingNetworkSuccssLayout = (LinearLayout)findViewById(R.id.loading_success_id);
        mLoadingCacheDataSuccessLayout = (LinearLayout)findViewById(R.id.loading_cache_id);
        mLoadingCacheDataSuccessLayout.setClickable(false);
        mCacheLoadingImageView = (ImageView)findViewById(R.id.loading_img);
        mLoadingMsgTextView = (TextView)findViewById(R.id.cache_loading_msg_id);
    }

    protected void setCacheLoadingViewVisibile(boolean isLoadingFailed){


        mLoadingNetworkSuccssLayout.setVisibility(View.GONE);
        mLoadingCacheDataSuccessLayout.setVisibility(View.VISIBLE);
        if (isLoadingFailed){
            mCacheLoadingImageView.clearAnimation();
            mCacheLoadingImageView.setImageResource(R.drawable.alert_yellow);
            mLoadingMsgTextView.setText(AppManager.getInstance().getApplication().getString(R.string.cache_loading_failed));
        }else{
            if (mCacheLoadingImageView != null && mCacheLoadingImageView.getAnimation() == null){
                isHasRotating = true;
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                        mContext, R.anim.loading_animation);
                LinearInterpolator lir = new LinearInterpolator();
                hyperspaceJumpAnimation.setInterpolator(lir);
                mCacheLoadingImageView.startAnimation(hyperspaceJumpAnimation);
            }
            mLoadingMsgTextView.setText(AppManager.getInstance().getApplication().getString(R.string.footview_loading));

        }

    }

    protected void setCacheLoadingViewGone(){
        mLoadingNetworkSuccssLayout.setVisibility(View.VISIBLE);
        mLoadingCacheDataSuccessLayout.setVisibility(View.GONE);
        mCacheLoadingImageView.clearAnimation();

    }


    public void setCacheLoadingView(boolean isCacheSuccessButRefreshFailed) {
        setCacheLoadingViewVisibile(isCacheSuccessButRefreshFailed);


    }

    @Override
    public void setViewPager(CustomViewPager view, Activity mainActivity) {
        mParentActivity = mainActivity;
        isHasRotating = false;
        if (mHomeViewPager != null) {
            mHomeViewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mHomeViewPager = view;
        mHomeViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;

        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset != 0) {
            mCurrentPage = position;
            boolean isLeft = true;
            if (lastValue >= positionOffsetPixels) {
                //右滑
                isLeft = false;
            } else if (lastValue < positionOffsetPixels) {
                //左滑
                isLeft = true;
            }
            setIndiactorView(positionOffset, isLeft);

            if (mListener != null) {
                mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }
        lastValue = positionOffsetPixels;

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
        isBigToSmall = false;
        isSmallToBig = false;
        initIndicatorView(mCurrentPage);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }


    public void initActviityHomeListView(boolean isFromRefresh) {

        ((MainBaseActivity) mParentActivity).initHomeListView(false);
    }

    public void initHomeLayout() {
        mHomeNameLayout = (LinearLayout) findViewById(R.id.home_name_layout);
        mHomeNameLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDropDownOrNot) {
                    homeLayoutClickEvent();
                } else {
                    ((MainBaseActivity) mParentActivity).hideCityListView();
                }
                setDropDownImageView(!isDropDownOrNot);
            }
        });
    }

    private void setIndiactorView(float positionOffset, boolean isLeft) {
        float radiusOffsetHead = 0.5f;
        if (positionOffset <= radiusOffsetHead && !isSmallToBig) {

            initIndicatorView(mCurrentPage);
            isSmallToBig = true;
            isBigToSmall = false;

        } else if (positionOffset > radiusOffsetHead && !isBigToSmall) {
            int currnt = isLeft ? mCurrentPage + 1 : mCurrentPage - 1;
            initIndicatorView(currnt);
            isBigToSmall = true;
            isSmallToBig = false;
        }
    }

    private void homeLayoutClickEvent() {
        initActviityHomeListView(false);
    }

    /**
     * 需要子类override，这个方法是用于滑动到一半之后进行值得设置
     *
     * @param index
     */
    protected void initIndicatorView(int index) {

    }


    /**
     * 设置dropDown的图标状态
     *
     * @param isDown
     */
    protected void setDropDownImageView(boolean isDown) {
    }

}
