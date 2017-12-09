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

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainBaseActivity;
import com.honeywell.hch.airtouch.ui.main.ui.devices.view.AllDeviceFragment;
import com.honeywell.hch.airtouch.ui.main.ui.title.presenter.AllDeviceIndicatorBasePresenter;
import com.honeywell.hch.airtouch.ui.main.ui.title.presenter.AllDeviceIndicatorPresenter;
import com.honeywell.hch.airtouch.ui.trydemo.presenter.TryDemoAllDeviceIndicatorPresenter;
import com.honeywell.hch.airtouch.ui.trydemo.ui.TryDemoMainActivity;

/**
 *
 */
public class AllDeviceTitlePageIndicator extends TitlePageBaseIndicator implements IAllDeviceIndactorView {


    private ImageView mDefaultOrAuthorizeImageView;
    private TextView mHomeNameTextView;

    private AllDeviceIndicatorBasePresenter mAllDeviceIndicatorPresenter;

    private ImageView mAddDeviceIconView;
    private ImageView mEditIconView;
    private TextView mTextView;
    private RelativeLayout mEditLayout;

    private boolean isEditStatus = false;

    private int mIndex;

    private HplusNetworkErrorLayout mNetworkErrorLayout;


    public AllDeviceTitlePageIndicator(Context context) {
        super(context);
    }

    public AllDeviceTitlePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_device_title, this);
        view.setVisibility(VISIBLE);

        StatusBarUtil.initMarginTopWithStatusBarHeight(findViewById(R.id.actionbar_tile_bg), mContext);

        mDefaultOrAuthorizeImageView = (ImageView) findViewById(R.id.select_title_default_home_iv);
        mHomeNameTextView = (TextView) findViewById(R.id.select_title_tv);

        initHomeLayout();


        mNetworkErrorLayout = (HplusNetworkErrorLayout) findViewById(R.id.network_error_layout);

        mEditLayout = (RelativeLayout) findViewById(R.id.right_edit);
        mAddDeviceIconView = (ImageView) findViewById(R.id.all_device_add_iv);
        mAddDeviceIconView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mParentActivity, EnrollScanActivity.class);
                mParentActivity.startActivity(intent);
                mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                EnrollBaseActivity.mAliveFlag  = 1;
            }
        });
        mEditIconView = (ImageView) findViewById(R.id.all_device_select_iv);
        mEditIconView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditStatus();
            }
        });
        mTextView = (TextView) findViewById(R.id.end_text_tip);
        mTextView.setText(getResources().getString(R.string.all_device_title_cancel));
        mTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitleNormalStatus();
                if (mParentActivity != null) {
                    ((MainBaseActivity) mParentActivity).setAllDeviceEditStatusFromIndiacator(false);
                }
            }
        });

        mDropDownImageView = (ImageView) findViewById(R.id.select_title_drop_iv);
        super.initView();
    }


    @Override
    public void setNoHomeView() {

    }


    @Override
    public void setHomeName(String homeName) {
        mHomeNameTextView.setText(homeName);
    }


    @Override
    public void setCityName(String cityName) {
    }

    @Override
    public void setDefaultHomeIcon(boolean isDefault, boolean isSelfHome,boolean isRealHome) {
        if(isRealHome){
            if (isDefault) {
                mDefaultOrAuthorizeImageView.setVisibility(VISIBLE);
                mDefaultOrAuthorizeImageView.setImageResource(R.drawable.default_home_blue);
            } else {
                if (isSelfHome) {
                    mDefaultOrAuthorizeImageView.setVisibility(VISIBLE);
                    mDefaultOrAuthorizeImageView.setImageResource(R.drawable.normal_home_ic);
                } else {
                    mDefaultOrAuthorizeImageView.setVisibility(VISIBLE);
                    mDefaultOrAuthorizeImageView.setImageResource(R.drawable.shared_to_me);
                }
            }
        } else {
            mDefaultOrAuthorizeImageView.setVisibility(VISIBLE);
            mDefaultOrAuthorizeImageView.setImageResource(R.drawable.homelist_madair_ic);
        }

        setAddDeviceIconVisible(isSelfHome);
    }


    @Override
    protected void initIndicatorView(int index) {
        getAllDeviceIndicatorPresenter(initAllDeviceIndicator(index)).setAllDeviceView();
        setTitleNormalStatus();
    }

    /**
     * 重新设置new index
     * @param index
     * @return
     */
    private int initAllDeviceIndicator(int index){
        int currentFragmentSize = getAllDeviceFragmentSize();
        if (index < 0) {
            mIndex = 0;
        } else if (index >= currentFragmentSize
                && currentFragmentSize - 1 >= 0) {
            mIndex = currentFragmentSize - 1;
        } else {
            mIndex = index;
        }
        return mIndex;
    }

    @Override
    public void setDropDownImageView(boolean isDown) {
        isDropDownOrNot = isDown;
        int src = isDropDownOrNot ? R.drawable.alldevice_drop_down_black : R.drawable.alldevice_pull_up_black;
        mDropDownImageView.setImageResource(src);
    }

    @Override
    public void setTitleNormalStatus() {

       if (mParentActivity instanceof MainActivity){
           showNoralStauts();
       }else if (mParentActivity instanceof TryDemoMainActivity){
           showTryDemoStatus();
       }
    }

    private void showNoralStauts(){
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            setNoWorkView();
            showNormalRightView();
        } else if (UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            setNetworkErrorView();
            showNormalRightView();
        } else if (isDeviceListLoading() || isDeviceListCacheSuccess()) {
            mNetworkErrorLayout.setVisibility(GONE);
            setCacheLoadingView(false);
        } else if (isDeviceCacheSucessButRefreshFailed() || isDeviceListLoadedFailed()) {
            mNetworkErrorLayout.setVisibility(GONE);
            setCacheLoadingView(true);
        } else {
            mNetworkErrorLayout.setVisibility(GONE);
            showNormalRightView();
        }
        if (mHomeNameTextView != null) {
            mHomeNameLayout.setVisibility(VISIBLE);
        }
    }

    private void showTryDemoStatus(){
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            setNoWorkView();
        }
        mLoadingNetworkSuccssLayout.setVisibility(View.GONE);
        mLoadingCacheDataSuccessLayout.setVisibility(View.GONE);
        mCacheLoadingImageView.clearAnimation();

        if (mHomeNameTextView != null) {
            mHomeNameLayout.setVisibility(VISIBLE);
        }

    }


    public void setEditStatus() {
        isEditStatus = true;
        ((MainBaseActivity) mParentActivity).setAllDeviceEditStatusFromIndiacator(true);
        mEditIconView.setVisibility(GONE);
        mTextView.setVisibility(VISIBLE);
        mAddDeviceIconView.setVisibility(GONE);
        mHomeNameLayout.setVisibility(GONE);

    }

    public void setEditStatusNetworkErrorView() {
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            mNetworkErrorLayout.setVisibility(VISIBLE);
            mNetworkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.NETWORK_OFF);
        } else if (UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            mNetworkErrorLayout.setVisibility(VISIBLE);
            mNetworkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.CONNECT_SERVER_ERROR);
        } else {
            mNetworkErrorLayout.setVisibility(GONE);
        }
    }

    public void initPresenter(int index) {
        int size = getAllDeviceIndicatorPresenter(index).getDataSourceSize();

        if (size > 0 && index < size) {
            initIndicatorView(index);
        }
    }

    public AllDeviceFragment getCurrentAllDeviceFragment(int current) {
        return ((MainBaseActivity) mParentActivity).getAllDeviceFramentList().get(current);
    }

    private int getAllDeviceFragmentSize(){
        return ((MainBaseActivity) mParentActivity).getAllDeviceFramentList().size();
    }


    public boolean isEditStatus() {
        return isEditStatus;
    }

    public void dealWithNetWorkResponse() {
        if (mNetworkErrorLayout != null) {
            if (mNetworkErrorLayout.getVisibility() == View.VISIBLE) {
                startFlick(mNetworkErrorLayout);
            } else {
                mNetworkErrorLayout.setVisibility(View.VISIBLE);
            }
        }
    }


    private void setAddDeviceIconVisible(boolean isSelfHome) {
        if (isSelfHome) {
            mAddDeviceIconView.setVisibility(VISIBLE);
        } else {
            mAddDeviceIconView.setVisibility(GONE);
        }
    }

    private void showNormalRightView() {
        setCacheLoadingViewGone();
        isEditStatus = false;
        setAddAndEditImageView();
    }

    private void setAddAndEditImageView() {
        if (mParentActivity != null) {
            setRightLayoutVisible();
            int visible = isLocationOwner(mIndex) ? VISIBLE : GONE;
            mAddDeviceIconView.setVisibility(visible);
        }
    }

    private void setNoWorkView() {
        mLoadingNetworkSuccssLayout.setVisibility(VISIBLE);
        mLoadingCacheDataSuccessLayout.setVisibility(GONE);
        mNetworkErrorLayout.setVisibility(VISIBLE);
        mNetworkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.NETWORK_OFF);

    }

    private void setNetworkErrorView() {
        mLoadingNetworkSuccssLayout.setVisibility(VISIBLE);
        mLoadingCacheDataSuccessLayout.setVisibility(GONE);
        mNetworkErrorLayout.setVisibility(VISIBLE);
        mNetworkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.CONNECT_SERVER_ERROR);
    }


    private AllDeviceIndicatorBasePresenter getAllDeviceIndicatorPresenter(int index) {
        if (mAllDeviceIndicatorPresenter == null) {
            if (mParentActivity instanceof MainActivity){
                mAllDeviceIndicatorPresenter = new AllDeviceIndicatorPresenter(this);
            }else if (mParentActivity instanceof TryDemoMainActivity){
                mAllDeviceIndicatorPresenter = new TryDemoAllDeviceIndicatorPresenter(this);
            }
        }
        mAllDeviceIndicatorPresenter.reInitPresenterIndex(index);
        return mAllDeviceIndicatorPresenter;
    }


    private void setRightLayoutVisible() {
        if (mIndex < getAllDeviceFragmentSize() && isLocationOwner(mIndex)) {
            if (getCurrentAllDeviceFragment(mIndex).isHaveLoadedDeviceListInThisLocation()) {
                setEditImageVisible(true);
                return;
            }
        }
        setEditImageVisible(false);
    }

    private void setEditImageVisible(boolean visible) {
        int visibleStaus = visible ? VISIBLE : GONE;

        mEditLayout.setVisibility(visibleStaus);
        if (visible) {
            mEditIconView.setVisibility(VISIBLE);
            mTextView.setVisibility(GONE);
        }
    }


    private boolean isLocationOwner(int index) {
        try {
            return UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                    UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()).get(index).isIsLocationOwner();
        } catch (Exception e) {

        }
        return false;
    }

    //
    private boolean isDeviceListLoadedSuccess() {
        try {
            return getCurrentAllDeviceFragment(mIndex).getUserLocationData().isNetworkDataLoadSuccess();
        } catch (Exception e) {

        }
        return false;
    }

    private boolean isDeviceListLoading() {
        try {
            return getCurrentAllDeviceFragment(mIndex).getUserLocationData().isDeviceListLoadingData();
        } catch (Exception e) {

        }
        return false;
    }

    private boolean isDeviceListLoadedFailed() {
        try {
            return getCurrentAllDeviceFragment(mIndex).getUserLocationData().isDeviceListLoadDataFailed();
        } catch (Exception e) {

        }
        return false;
    }

    private boolean isDeviceListCacheSuccess() {
        try {
            return getCurrentAllDeviceFragment(mIndex).getUserLocationData().isDeviceCacheDataLoadSuccess();
        } catch (Exception e) {

        }
        return false;
    }

    private boolean isDeviceCacheSucessButRefreshFailed() {
        try {
            return getCurrentAllDeviceFragment(mIndex).getUserLocationData().isDeviceCacheDataSuccessButFreshFaile();
        } catch (Exception e) {

        }
        return false;
    }


    //闪烁
    private void startFlick(View view) {
        if (null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0.2f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(alphaAnimation);
        view.setClickable(false);
    }

}
