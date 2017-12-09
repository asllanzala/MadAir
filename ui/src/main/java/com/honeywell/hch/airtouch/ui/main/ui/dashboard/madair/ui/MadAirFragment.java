package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.TypeTextView;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.IMadAirBLEDataManager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirChartDataManager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirDashboardUIManager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirSpeedUIManager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view.ArcProgressBar;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view.BreathAnimNoRefreshView;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view.MadAirScrollView;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view.PercentageBar;
import com.honeywell.hch.airtouch.ui.main.ui.me.SelectCityActivity;


/**
 * Created by Qian Jin on 11/7/16.
 */
public class MadAirFragment extends BaseRequestFragment {

    private View mTopView;
    private RelativeLayout mCityAreaLayout, mTopTitleBar, mHasNetwork, mNoNetwork;
    private TextView mPm25TextViewTitle, mPm25TextView, mPm25TopTextView, mNeedMaskTextView;
    private ImageView mDeviceStatusImageView;
    private TextView mDeviceStatusTextView, mDisconnectDescTextView;
    private LinearLayout mDeviceStatusBarLayout;
    private RelativeLayout mChartSection;
    private TextView mTextViewNum;
    private TextView mTextViewNumUnit;
    private TextView mTextViewUnit;
    private TextView mNoNetworkTextView;
    private LinearLayout mBatteryCircleSection;
    private MadAirScrollView mScrollView;
    private RelativeLayout mBreathSection;

    private MadAirDeviceModel mMadAirDevice;
    private IMadAirBLEDataManager mMadAirBleDataManager;
    private MadAirSpeedUIManager mMadAirSpeedUIManager;
    private MadAirChartDataManager mMadAirChartDataManager;
    private MadAirDashboardUIManager mMadAirDashboardUIManager;

    private VirtualUserLocationData mUserLocationData;
    private MadAirDeviceStatus mPreviousDeviceStatus = MadAirDeviceStatus.NOT_READY;
    private MadAirDeviceStatus mCurrentDeviceStatus = MadAirDeviceStatus.NOT_READY;

    private ImageView mSeparateLineOne;
    private ImageView mSeparateLineTwo;
    private BreathAnimNoRefreshView breathImageView;

    private Button mAddLocationBtn;
    private TypeTextView mTypeTextView;

    public static MadAirFragment newInstance(Activity activity, VirtualUserLocationData userLocationData, IMadAirBLEDataManager iMadAirBLEDataManager) {
        MadAirFragment fragment = new MadAirFragment();
        fragment.initActivity(activity);
        fragment.initUserLocationData(userLocationData, iMadAirBLEDataManager);
        return fragment;
    }

    private void initUserLocationData(VirtualUserLocationData userLocationData, IMadAirBLEDataManager iMadAirBLEDataManager) {
        mUserLocationData = userLocationData;
        mMadAirDevice = mUserLocationData.getMadAirDeviceModel();
        mMadAirBleDataManager = iMadAirBLEDataManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMadAirChartDataManager = new MadAirChartDataManager(mParentActivity);
        mMadAirDashboardUIManager = new MadAirDashboardUIManager(mParentActivity.getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        updateView();
        displayWeather();
        displayNetwork();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (breathImageView != null) {
            breathImageView.stopBreathAnimation();
        }
    }

    public void refreshMadAirWeather() {
        displayWeather();
        displayNetwork();
    }

    public void refreshData() {
        mMadAirDevice = mUserLocationData.getMadAirDeviceModel();

        updateView();
    }

    @Override
    public void refreshFragment(UserLocationData locationData, int index) {
        displayNetwork();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mTopView == null) {
            mTopView = inflater.inflate(R.layout.fragment_mad_air_dashboard, container, false);

            // scroll view
            mScrollView = (MadAirScrollView) mTopView.findViewById(R.id.dashboard_scroll);

            // city area
            mTopTitleBar = (RelativeLayout) mTopView.findViewById(R.id.title_bar_my_mask_layout);
            RelativeLayout titleBarTvLayout = (RelativeLayout) mTopView.findViewById(R.id.top_title_bar_my_mask_tv_layout);
            mCityAreaLayout = (RelativeLayout) mTopView.findViewById(R.id.city_area);
            mHasNetwork = (RelativeLayout) mTopView.findViewById(R.id.has_network_layout);
            mNoNetwork = (RelativeLayout) mTopView.findViewById(R.id.no_network_layout);
            mNoNetworkTextView = (TextView) mTopView.findViewById(R.id.no_network_tv);
            RelativeLayout cityAreaTvLayout = (RelativeLayout) mTopView.findViewById(R.id.city_area_tv_layout);
            mPm25TextView = (TextView) mTopView.findViewById(R.id.title_bar_pm25_value_tv);
            mPm25TextViewTitle = (TextView) mTopView.findViewById(R.id.title_bar_pm25_tv);
            mPm25TopTextView = (TextView) mTopView.findViewById(R.id.top_title_bar_pm25_tv);
            mNeedMaskTextView = (TextView) mTopView.findViewById(R.id.title_bar_need_mask_tv);
            mAddLocationBtn = (Button) mTopView.findViewById(R.id.mad_air_add_locate);
            mTypeTextView = (TypeTextView) mTopView.findViewById(R.id.title_bar_right_tv);
            mAddLocationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentStartActivity(SelectCityActivity.class);
                }
            });
            /*
             * 安卓系统版本大于等于5.0，全屏幕，mTopTitleBar > titleBarTvLayout
             * 安卓系统版本低于5.0，无法全屏幕，mTopTitleBar ＝ titleBarTvLayout
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setupSectionWithRelativeLayout(mTopTitleBar, 2.5f, 0, -1);
                setupSectionWithRelativeLayout(titleBarTvLayout, 1.5f, 1, -1);
                setupSectionWithRelativeLayout(cityAreaTvLayout, 9, 2.5f, -1);
            } else {
                setupSectionWithRelativeLayout(mTopTitleBar, 1.5f, 0, -1);
                setupSectionWithRelativeLayout(titleBarTvLayout, 1.5f, 0, -1);
                setupSectionWithRelativeLayout(cityAreaTvLayout, 9, 1.5f, -1);
            }
            setupSectionWithRelativeLayout(mCityAreaLayout, 11.5f, 0, -1);

            // device status bar
            mDeviceStatusBarLayout = (LinearLayout) mTopView.findViewById(R.id.device_status_bar);
            mDeviceStatusImageView = (ImageView) mTopView.findViewById(R.id.device_status_iv);
            mDisconnectDescTextView = (TextView) mTopView.findViewById(R.id.disconnect_desc_tv);
            mDeviceStatusTextView = (TextView) mTopView.findViewById(R.id.device_status_tv);

            mBreathSection = (RelativeLayout) mTopView.findViewById(R.id.breath_freq_section);
            RelativeLayout freqValueSection = (RelativeLayout) mTopView.findViewById(R.id.freq_value_section);
            LinearLayout speedControlSection = (LinearLayout) mTopView.findViewById(R.id.speed_control_section);

            // mBreathSection = freqValueSection + speedControlSection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setupSectionWithLinearLayout(mBreathSection, 16.5f, 0, R.id.device_status_bar);
                setupSectionWithRelativeLayout(freqValueSection, 9.5f, 0, R.id.device_status_bar);
                setupSectionWithRelativeLayout(speedControlSection, 7, 0, R.id.freq_value_section);
            } else {
                setupSectionWithLinearLayout(mBreathSection, 17.6f, 0, R.id.device_status_bar);
                setupSectionWithRelativeLayout(freqValueSection, 10.5f, 0, R.id.device_status_bar);
                setupSectionWithRelativeLayout(speedControlSection, 7, 0, R.id.freq_value_section);
            }
            breathImageView = (BreathAnimNoRefreshView) mTopView.findViewById(R.id.breath_freq_iv);
            if (mMadAirDevice != null) {
                breathImageView.setRotateSpeed(mMadAirDevice.getBreathFreq());
            }
            // chart section
            mChartSection = (RelativeLayout) mTopView.findViewById(R.id.chart_section3);

            // battery section
            mBatteryCircleSection = (LinearLayout) mTopView.findViewById(R.id.battery_circle_section2);
            setupSectionWithLinearLayout(mBatteryCircleSection, 7, 0, R.id.device_status_bar);

            mSeparateLineOne = (ImageView) mTopView.findViewById(R.id.line1);
            mSeparateLineTwo = (ImageView) mTopView.findViewById(R.id.line2);
            mScrollView.setAddCityButton(mAddLocationBtn);
            mScrollView.setmCityRl(mCityAreaLayout);
        }

        // delete later
//        mDeviceStatusImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMadAirDashboardUIManager.test(mMadAirDevice);
//            }
//        });

        return mTopView;
    }

    private void updateView() {
        if (!isAdded() || mMadAirDevice == null)
            return;

        mCurrentDeviceStatus = mMadAirDevice.getDeviceStatus();

        switch (mCurrentDeviceStatus) {
            case NOT_READY:
            case CHARGE:
            case SLEEP:
                break;

            case BLE_DISABLE:
                displayDisconnectArea();
                mMadAirDashboardUIManager.displayStatusBar(MadAirDeviceStatus.BLE_DISABLE, mDeviceStatusImageView,
                        mDeviceStatusTextView, mDisconnectDescTextView);
                break;

            case DISCONNECT:
                displayDisconnectArea();
                break;

            case CONNECT:
                displayConnectArea();
                break;

            case USING:
                displayBreathArea();
                break;
        }

        mPreviousDeviceStatus = mMadAirDevice.getDeviceStatus();
    }

    /*
     * city area : device status bar : info area : bottom = 11.5 : 1.5 : 7 : 2 (total 22)
     * system title bar : mad air title bar : text area = 1 : 1.5 : 9 (total 11.5)
     */
    private void setupSectionWithRelativeLayout(final View view, final float height, final float marginTop, final int belowId) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int total = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 22 : 23;
                RelativeLayout.LayoutParams layoutParams
                        = new RelativeLayout.LayoutParams(view.getLayoutParams());

                layoutParams.topMargin = (int) (DensityUtil.getScreenHeight() * marginTop / total);
                if (belowId > 0)
                    layoutParams.addRule(RelativeLayout.BELOW, belowId);
                if (height > 0)
                    layoutParams.height = (int) (DensityUtil.getScreenHeight() * height / total);

                view.setLayoutParams(layoutParams);
            }
        });
    }

    private void setupSectionWithLinearLayout(final View view, final float height, final float marginTop, final int belowId) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int total = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 22 : 23;
                LinearLayout.LayoutParams layoutParams
                        = new LinearLayout.LayoutParams(view.getLayoutParams());

                layoutParams.topMargin = (int) (DensityUtil.getScreenHeight() * marginTop / total);
                layoutParams.height = (int) (DensityUtil.getScreenHeight() * height / total);

                view.setLayoutParams(layoutParams);
            }
        });
    }

    private void displayWeather() {
        if (!isAdded() || mMadAirDevice == null)
            return;

        mHasNetwork.setVisibility(View.VISIBLE);
        mNoNetwork.setVisibility(View.INVISIBLE);

        int pm25 = mMadAirDashboardUIManager.getPm25(mUserLocationData);
        if (pm25 > 0) {
            mMadAirBleDataManager.saveTodayPm25(mMadAirDevice.getMacAddress(), pm25);
            mPm25TextView.setText(String.valueOf(pm25));
            mPm25TextView.setTextColor(mParentActivity.getResources().getColor(mMadAirDashboardUIManager.getPm25Color(pm25)));
            mNeedMaskTextView.setText(mMadAirDashboardUIManager.getNeedMask(pm25));
            mCityAreaLayout.setBackgroundResource(mMadAirDashboardUIManager.getBackground(pm25));

        } else {
            mPm25TextView.setText(mParentActivity.getString(R.string.mad_air_dashboard_no_data));
            mPm25TextView.setTextColor(mParentActivity.getResources().getColor(R.color.ds_clean_now));
            mCityAreaLayout.setBackgroundResource(mMadAirDashboardUIManager.getBackground(0));
        }
        mPm25TextViewTitle.setVisibility(View.VISIBLE);
    }

    private void displayDisconnectArea() {

        mChartSection.setVisibility(View.VISIBLE);
        mBreathSection.setVisibility(View.GONE);
        mBatteryCircleSection.setVisibility(View.GONE);
        mTopTitleBar.setVisibility(View.GONE);
        setSeparateLineVisible(View.GONE);

        breathImageView.stopBreathAnimation();

        // scroll view
        if (mCurrentDeviceStatus != mPreviousDeviceStatus)
            mMadAirDashboardUIManager.startScrollViewAnimation(mScrollView, -1);

        // device status bar
        resetDeviceStatusBarLayoutScale(MadAirDeviceStatus.DISCONNECT);
        resetChartSectionScale(true);
        mMadAirDashboardUIManager.displayStatusBar(MadAirDeviceStatus.DISCONNECT, mDeviceStatusImageView,
                mDeviceStatusTextView, mDisconnectDescTextView);

        displayChartSection();
    }

    private void displayConnectArea() {

        mChartSection.setVisibility(View.VISIBLE);
        mBreathSection.setVisibility(View.GONE);
        mBatteryCircleSection.setVisibility(View.VISIBLE);
        mSeparateLineOne.setVisibility(View.GONE);
        mSeparateLineTwo.setVisibility(View.VISIBLE);

        mTopTitleBar.setVisibility(View.GONE);

        breathImageView.stopBreathAnimation();
        breathImageView.initBreatheFreq();
        // scroll view
        if (mCurrentDeviceStatus != mPreviousDeviceStatus)
            mMadAirDashboardUIManager.startScrollViewAnimation(mScrollView, -1);
        displayBatteryFilterSection();
        // device status bar
        resetDeviceStatusBarLayoutScale(MadAirDeviceStatus.CONNECT);
        resetChartSectionScale(false);
        mMadAirDashboardUIManager.displayStatusBar(MadAirDeviceStatus.CONNECT, mDeviceStatusImageView,
                mDeviceStatusTextView, mDisconnectDescTextView);

        // battery section
        mBatteryCircleSection = (LinearLayout) mTopView.findViewById(R.id.battery_circle_section2);


        // chart section
        mChartSection = (RelativeLayout) mTopView.findViewById(R.id.chart_section3);
        displayChartSection();
    }

    private void displayBreathArea() {

        mChartSection.setVisibility(View.VISIBLE);
        mBreathSection.setVisibility(View.VISIBLE);
        mBatteryCircleSection.setVisibility(View.VISIBLE);

        setSeparateLineVisible(View.VISIBLE);

        // scroll view
        mMadAirDashboardUIManager.setScrollListener(mScrollView, mTopTitleBar);
        mMadAirDashboardUIManager.setScrollViewCallback(new MadAirDashboardUIManager.ScrollViewCallback() {
            @Override
            public void onShow() {
                if (mMadAirDevice.getDeviceStatus() == MadAirDeviceStatus.USING)
                    displayTopTitleBar();
            }
        });
        if (mCurrentDeviceStatus != mPreviousDeviceStatus)
            mMadAirDashboardUIManager.startScrollViewAnimation(mScrollView, 1);

        // device status bar
        resetDeviceStatusBarLayoutScale(MadAirDeviceStatus.USING);
        mMadAirDashboardUIManager.displayStatusBar(MadAirDeviceStatus.USING, mDeviceStatusImageView,
                mDeviceStatusTextView, mDisconnectDescTextView);

        resetChartSectionScale(false);

        // breath section
        mMadAirSpeedUIManager = new MadAirSpeedUIManager(mMadAirDevice, mMadAirBleDataManager, mTopView, mParentActivity);
        mMadAirSpeedUIManager.displaySpeed(mMadAirDevice.getMotorSpeed());

        // battery section
        displayBatteryFilterSection();
        if (mParentActivity.getString(R.string.mad_air_dashboard_no_data).equals(mMadAirDashboardUIManager.getBreathFreq())) {
            breathImageView.stopBreathAnimation();
        } else {
            int freq = mMadAirDevice.getBreathFreq();
            breathImageView.setRotateSpeed(freq);

        }
        // chart section
        mChartSection = (RelativeLayout) mTopView.findViewById(R.id.chart_section3);
        displayChartSection();
    }


    private void resetChartSectionScale(boolean isDisconnection) {
        float heightScale = isDisconnection ? 6.5f : 7;
        setupSectionWithLinearLayout(mChartSection, heightScale, 0, R.id.device_status_bar);
    }

    /**
     * 重新设置设备状态layout的比例
     */
    private void resetDeviceStatusBarLayoutScale(MadAirDeviceStatus madAirDeviceStatus) {
        float heightScale;
        switch (madAirDeviceStatus) {
            case DISCONNECT:
                heightScale = 2;
                break;
            case CONNECT:
                heightScale = 1.5f;
                break;
            case USING:
                heightScale = 2.5f;
                break;
            default:
                heightScale = 2;
                break;
        }
        setupSectionWithRelativeLayout(mDeviceStatusBarLayout, heightScale, 11.3f, -1);
    }

    private void setSeparateLineVisible(int visible) {
        mSeparateLineOne.setVisibility(visible);
        mSeparateLineTwo.setVisibility(visible);
    }

    private void displayTopTitleBar() {
        mTopTitleBar.setVisibility(View.VISIBLE);
        int pm25 = mMadAirDashboardUIManager.getPm25(mUserLocationData);
        if (pm25 > 0) {
            mPm25TopTextView.setText(String.valueOf(pm25));
            mPm25TopTextView.setTextColor(mParentActivity.getResources()
                    .getColor(mMadAirDashboardUIManager.getPm25Color(pm25)));
        } else {
            mPm25TopTextView.setText(mParentActivity.getString(R.string.mad_air_dashboard_no_data));
            mPm25TopTextView.setTextColor(mParentActivity.getResources().getColor(R.color.ds_clean_now));
        }
    }

    private void displayChartSection() {
        mTextViewNum = (TextView) mChartSection.findViewById(R.id.tv_num);
        mTextViewNumUnit = (TextView) mChartSection.findViewById(R.id.tv_unit_top);
        PercentageBar percentageBar = (PercentageBar) mChartSection.findViewById(R.id.bar_chart);
        mTextViewUnit = (TextView) mChartSection.findViewById(R.id.tv_unit);
        mMadAirChartDataManager.setPercentageBar(mMadAirDevice, percentageBar, mTextViewNum, mTextViewNumUnit, mTextViewUnit);
    }

    private void displayBatteryFilterSection() {
        TextView batteryPercentageTextView = (TextView) mBatteryCircleSection.findViewById(R.id.battery_percentage_tv);
        TextView batteryRemainingTextView = (TextView) mBatteryCircleSection.findViewById(R.id.battery_remaining_tv);
        TextView filterPercentageTextView = (TextView) mBatteryCircleSection.findViewById(R.id.filter_percentage_tv);
        TextView filterRemainingTextView = (TextView) mBatteryCircleSection.findViewById(R.id.filter_remaining_tv);
        TextView batteryUnitTextView = (TextView) mBatteryCircleSection.findViewById(R.id.battery_percentage_unit);
        TextView filterUnitTextView = (TextView) mBatteryCircleSection.findViewById(R.id.filter_percentage_unit);
        TextView breathFreqTextView = (TextView) mTopView.findViewById(R.id.breath_freq_tv);
        TextView currentBreathRateTextView = (TextView) mTopView.findViewById(R.id.current_breath_rate_tv);
        RelativeLayout shoppingLayout = (RelativeLayout) mBatteryCircleSection.findViewById(R.id.filter_shopping_layout);
        ArcProgressBar arcProgressBarBattery = (ArcProgressBar) mBatteryCircleSection.findViewById(R.id.bar_battery);
        ArcProgressBar arcProgressBarFilter = (ArcProgressBar) mBatteryCircleSection.findViewById(R.id.bar_filter);

        mMadAirDashboardUIManager.calculateBatteryFilterFreq(mMadAirDevice.getBreathFreq(), mMadAirDevice.getBatteryPercent(),
                mMadAirDevice.getBatteryRemain(), mMadAirDevice.getFilterUsageDuration(), mMadAirDevice.getAlarmInfo(), mDisconnectDescTextView);

        breathFreqTextView.setText(mMadAirDashboardUIManager.getBreathFreq());
        batteryPercentageTextView.setText(mMadAirDashboardUIManager.getBatteryPercent());
        batteryUnitTextView.setText(mMadAirDashboardUIManager.getBatteryUnit());
        batteryRemainingTextView.setText(mMadAirDashboardUIManager.getBatteryRemain());
        filterPercentageTextView.setText(mMadAirDashboardUIManager.getFilterPercent());
        filterUnitTextView.setText(mMadAirDashboardUIManager.getFilterUnit());
        filterRemainingTextView.setText(mMadAirDashboardUIManager.getFilterRemain());
        arcProgressBarBattery.setCurrentValues(mMadAirDashboardUIManager.getBatteryPercent());
        arcProgressBarFilter.setCurrentValues(mMadAirDashboardUIManager.getFilterPercent());
        currentBreathRateTextView.setText(mMadAirDashboardUIManager.getBreathDesc());
        shoppingLayout.setVisibility(mMadAirDashboardUIManager.getPurchaseVisible() ? View.VISIBLE : View.INVISIBLE);

        shoppingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(mUserLocationData
                        .getMadAirDeviceObject().getiFilterFeature().getFilterPurchaseUrls()[0]));
                startActivity(it);
            }
        });
    }

    public VirtualUserLocationData getmUserLocationData() {
        return mUserLocationData;
    }

    public void refreshGpsResult() {
        if (mTopView != null) {
            if (UserInfoSharePreference.getIsUsingGpsCityCode()) {
                if ("".equals(UserInfoSharePreference.getGpsCityCode())) {
                    mAddLocationBtn.setVisibility(View.GONE);
                    mNeedMaskTextView.setText(getString(R.string.location_locating));
                    mTypeTextView.setVisibility(View.VISIBLE);
                    mTypeTextView.startLoop();
                } else if (AppConfig.LOCATION_FAIL.equals(UserInfoSharePreference.getGpsCityCode())) {
                    mNeedMaskTextView.setText(getString(R.string.mad_air_locating_fail));
                    if (mNoNetwork.getVisibility() != View.VISIBLE) {
                        mAddLocationBtn.setVisibility(View.VISIBLE);
                    }
                    mTypeTextView.setVisibility(View.GONE);
                    mTypeTextView.stop();
                } else {
                    mAddLocationBtn.setVisibility(View.GONE);
                    mTypeTextView.setVisibility(View.GONE);
                    mTypeTextView.stop();
                    displayWeather();
                }
            } else {
                mAddLocationBtn.setVisibility(View.GONE);
                mTypeTextView.setVisibility(View.GONE);
                mTypeTextView.stop();
            }
        }
    }

    private void displayNetwork() {
        if (!isAdded() || mMadAirDevice == null)
            return;

        if (UserAllDataContainer.shareInstance().isHasNetWorkError()
                || !NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            displayNoNetwork();
        } else {
            mHasNetwork.setVisibility(View.VISIBLE);
            mNoNetwork.setVisibility(View.INVISIBLE);
            int pm25 = mMadAirDashboardUIManager.getPm25(mUserLocationData);
            mCityAreaLayout.setBackgroundResource(mMadAirDashboardUIManager.getBackground(pm25));
            refreshGpsResult();
        }
    }

    private void displayNoNetwork() {
        mHasNetwork.setVisibility(View.INVISIBLE);
        mNoNetwork.setVisibility(View.VISIBLE);
        mAddLocationBtn.setVisibility(View.GONE);
        mCityAreaLayout.setBackgroundResource(R.drawable.nerwork_error_bg);

        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()))
            mNoNetworkTextView.setText(mParentActivity.getString(R.string.network_off_msg));
        else if (UserAllDataContainer.shareInstance().isHasNetWorkError())
            mNoNetworkTextView.setText(mParentActivity.getString(R.string.network_error_msg));
    }

}