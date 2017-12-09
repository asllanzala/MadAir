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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainBaseActivity;
import com.honeywell.hch.airtouch.ui.main.ui.title.presenter.DashboardTitleIndicatorPresenter;
import com.honeywell.hch.airtouch.ui.main.ui.title.presenter.IHomeIndicatorPresenter;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeListContructor;
import com.honeywell.hch.airtouch.ui.trydemo.presenter.TryDemoDashboardTitleIndicatorPresenter;
import com.honeywell.hch.airtouch.ui.trydemo.ui.TryDemoMainActivity;
import com.honeywell.hch.airtouch.ui.weather.WeatherActivity;

/**
 *
 */
public class DashboardTitlePageIndicator extends TitlePageBaseIndicator implements IHomeIndactorView {


    private ImageView mDefaultOrAuthorizeImageView;
    private TextView mHomeNameTextView;
    private ImageView mWeatherIcon;
    private TextView mTemperatureTextView;
    private TextView mCityNameTextView;

    private LinearLayout mWeatherLayout;

    private IHomeIndicatorPresenter mHomeActivityIndicatorPresenter;

    private int mCurrentIndex;

    public DashboardTitlePageIndicator(Context context) {
        super(context);
    }

    public DashboardTitlePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_page_indicator_layout, this);

        StatusBarUtil.initMarginTopWithStatusBarHeight(findViewById(R.id.actionbar_tile_bg), mContext);
        mDefaultOrAuthorizeImageView = (ImageView) findViewById(R.id.default_auth_image_id);
        mHomeNameTextView = (TextView) findViewById(R.id.home_name_id);
        mWeatherIcon = (ImageView) findViewById(R.id.weather_icon_id);
        mTemperatureTextView = (TextView) findViewById(R.id.temperature_id);
        mCityNameTextView = (TextView) findViewById(R.id.city_name);
        mDropDownImageView = (ImageView) findViewById(R.id.drop_icon_id);
        mWeatherLayout = (LinearLayout) findViewById(R.id.title_right_layout);
        initHomeLayout();
        mWeatherLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: indiaaccount in try demo model should not display weather.
                if (AppConfig.shareInstance().isIndiaAccount()) {
                    return;
                }

                    UserLocationData userLocationData = null;
                if (mParentActivity instanceof MainActivity) {
                    userLocationData = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                            UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()).get(mCurrentIndex);
                } else if (mParentActivity instanceof TryDemoMainActivity) {
                    userLocationData = UserDataOperator.getHomePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),
                            TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList()).get(mCurrentIndex);
                }

                Intent intent = new Intent(mParentActivity, WeatherActivity.class);
                intent.putExtra(DashBoadConstant.ARG_CITY_NAME, userLocationData.getCity());
                intent.putExtra(DashBoadConstant.ARG_LOCATION_NAME, mCityNameTextView.getText());
                intent.putStringArrayListExtra(DashBoadConstant.ARG_CITY_BACKGROUD_LIST, userLocationData.getCityBackgroundDta().getCityBackgroundPathList());
                intent.putExtra(DashBoadConstant.ARG_LOCATION_ID, userLocationData.getLocationID());
                mParentActivity.startActivity(intent);
                mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        super.initView();
    }


    @Override
    public void setNoHomeView() {

    }


    @Override
    public void setGetWeatherFailed() {
        mWeatherIcon.setVisibility(GONE);
        mTemperatureTextView.setVisibility(GONE);
        mWeatherLayout.setClickable(false);
    }

    @Override
    public void setHomeName(String homeName) {
        mHomeNameTextView.setText(homeName);
    }

    @Override
    public void setWeatherIcon(int weatherIcon) {
        mWeatherIcon.setImageResource(weatherIcon);
        mWeatherIcon.setVisibility(VISIBLE);
        mWeatherLayout.setClickable(true);
    }

    @Override
    public void setWeatherTemperature(String temperature) {
        mTemperatureTextView.setText(temperature);
        mTemperatureTextView.setVisibility(VISIBLE);
    }


    @Override
    public void setCityName(String cityName) {
        if (AppConfig.LOCATION_FAIL.equals(cityName)) {
            mCityNameTextView.setVisibility(GONE);
        } else {
            mCityNameTextView.setVisibility(VISIBLE);
            mCityNameTextView.setText(cityName);
        }
    }

    @Override
    public void setDefaultHomeIcon(boolean isDefault, boolean isSelfHome, boolean isRealHome) {
        if (isRealHome) {
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

    }

    @Override
    protected void initIndicatorView(int index) {
        int newIndex = getHomeActivityIndicatorPresenter(index).initHomeIndicator(index);
        mCurrentIndex = newIndex;
        if (mParentActivity != null) {
            ((MainBaseActivity) mParentActivity).initArrowViewAndErrorNumber(newIndex, mHomeActivityIndicatorPresenter.getHomeSize());
        }
    }


    @Override
    public void setDropDownImageView(boolean isDown) {
        isDropDownOrNot = isDown;
        int src = isDropDownOrNot ? R.drawable.drop_down_arrow_small : R.drawable.up_arrow_small;
        mDropDownImageView.setImageResource(src);
    }

    @Override
    public void setCacheLoadingVisible(boolean isLoadingSuccessButRefreshFailed) {
        super.setCacheLoadingView(isLoadingSuccessButRefreshFailed);
        if (isLoadingSuccessButRefreshFailed) {
            mWeatherLayout.setClickable(false);
        } else {
            //when the weather is loading, user can't enter into weather layout.
            if (mLoadingCacheDataSuccessLayout.getVisibility() == VISIBLE) {
                mWeatherLayout.setClickable(false);
            } else {
                mWeatherLayout.setClickable(true);
            }

        }
    }

    @Override
    public void setCacheLoadingGone() {
        setCacheLoadingViewGone();
    }


    public int homeLayoutWidth() {
        return mHomeNameLayout.getWidth();
    }


    public void initPresenter(int index) {
        int size = getHomeActivityIndicatorPresenter(index).getHomeSize();
        if (size > 0 && index < size) {
            initIndicatorView(index);
        }
    }


    private IHomeIndicatorPresenter getHomeActivityIndicatorPresenter(int index) {
        if (mHomeActivityIndicatorPresenter == null) {
            if (mParentActivity instanceof MainActivity) {
                mHomeActivityIndicatorPresenter = new DashboardTitleIndicatorPresenter(this, index);
            } else {
                mHomeActivityIndicatorPresenter = new TryDemoDashboardTitleIndicatorPresenter(this, index);

            }
        }
        return mHomeActivityIndicatorPresenter;
    }


}
