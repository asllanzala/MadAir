package com.honeywell.hch.airtouch.ui.trydemo.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.ble.service.BluetoothLeService;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserLocation;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomIconViewItem;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.main.manager.common.MainActivityUIManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceMadAirManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceUIBaseManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceUIManager;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainBaseActivity;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirBleDataManager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.ui.MadAirFragment;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.view.HomeFragment;
import com.honeywell.hch.airtouch.ui.main.ui.devices.view.AllDeviceFragment;
import com.honeywell.hch.airtouch.ui.main.ui.devices.view.NoLocationAllDeviceFragment;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessagesFragment;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoAllDeviceMadAirManager;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoAllDeviceUiManager;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoBleDataManager;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoDataConstructor;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeListContructor;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoIndicatorValueManager;

import java.util.ArrayList;
import java.util.List;

import static com.honeywell.hch.airtouch.plateform.config.HPlusConstants.REFRESH_MADAIR_DATA;

/**
 * Created by honeywell on 21/12/2016.
 */

public class TryDemoMainActivity extends MainBaseActivity implements IRefreshOpr {

    private TryDemoIndicatorValueManager mTryDemoIndicatorValueManager = null;
    private boolean isFromStartActivity = false;
    private TryDemoMainActivity.HomeAndDeviceStatusReceiver mHomeAndDeviceStatusReceiver;

    private RelativeLayout mTopMv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TryDemoHomeListContructor.getInstance().constructorDataList();
        mMainActivityUIManager = new MainActivityUIManager(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList());

        mDashboardPageIndicator.initPresenter(mCurrentHomeIndex);
        initBottomNavigateView();
        initFramglentList();
        registerWeatherReceiver();
        StatusBarUtil.hideStatusBar(this);

        mTryDemoIndicatorValueManager = new TryDemoIndicatorValueManager();
        mTryDemoIndicatorValueManager.addRefreshListener(this);

        startChangeIndicatorValue();

        isFromStartActivity = getIntent().getBooleanExtra(StartActivity.FROM_START_ACTIVITY, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mTopMv = new TopTipMessageView(this);
            mParentView.addView(mTopMv);
            mTopMv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                    return false;
                }
            });
        }

       List<String> cityList = new ArrayList<>();
        cityList.add(TryDemoHomeListContructor.getInstance().getUserLocationDataList().get(0).getCity());
        UserAllDataContainer.shareInstance().getWeatherRefreshManager().addCityListRefresh(cityList, false);
    }


    private class HomeAndDeviceStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (HPlusConstants.LONG_REFRESH_END_ACTION.equals(action)) {
                if (mDashboardPageIndicator != null) {
                    mDashboardPageIndicator.initPresenter(mCurrentHomeIndex);
                }
                refreshMadAirWeather();
            }
            if (HPlusConstants.TRY_DEMO_REFRESH_MADAIR_DATA.equals(action)){
                allDeviceFragmentRefresh();
                refreshMadAirFragmentTitle();
            }

        }
    }

    private void allDeviceFragmentRefresh() {
        int indexAlldevice = 0;
        List<UserLocationData> userLocationDataListForAllDevice = UserDataOperator.getAllDevicePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),
                TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList());
        for (UserLocationData userLocationData : userLocationDataListForAllDevice) {
            if (indexAlldevice < mAllDeviceFramentList.size()) {
                mAllDeviceFramentList.get(indexAlldevice).refreshAllDeviceFragment(userLocationData);
                indexAlldevice++;
            }
        }
    }

    private void refreshMadAirFragmentTitle() {
        if (mDashboardPageIndicator != null) {
            mDashboardPageIndicator.initPresenter(mCurrentHomeIndex);
        }
    }


    private void registerWeatherReceiver() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(HPlusConstants.LONG_REFRESH_END_ACTION);
        intentFilter.addAction(HPlusConstants.TRY_DEMO_REFRESH_MADAIR_DATA);
        mHomeAndDeviceStatusReceiver = new TryDemoMainActivity.HomeAndDeviceStatusReceiver();
        registerReceiver(mHomeAndDeviceStatusReceiver, intentFilter);
    }


    private  void initBottomNavigateView(){
        List<BottomIconViewItem> bottomIconViewItemList = new ArrayList<>();
        addBottomNavigateView(bottomIconViewItemList);
        mBottomNavigationView.addBtnToNavigationView(bottomIconViewItemList);
    }

    private void initFramglentList() {

        List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList());

        for (int i = 0; i < userLocationDataList.size(); i++) {
            if (userLocationDataList.get(i) instanceof RealUserLocationData){
                mHomeFragmentList.add(HomeFragment.newInstance(userLocationDataList.get(i), i, this));
            }else if (userLocationDataList.get(i) instanceof VirtualUserLocationData){
                mHomeFragmentList.add(MadAirFragment.newInstance(this,(VirtualUserLocationData) userLocationDataList.get(i),new TryDemoBleDataManager()));

            }
            mAllDeviceFramentList.add(i, AllDeviceFragment.newInstance(userLocationDataList.get(i), this));
            mHomePageAdapter.notifyDataSetChanged();
        }

        if (mHomeFragmentList.size() > 0){
            mCurrentHomeIndex = 0;
        }

        mViewPager.setCurrentItem(mCurrentHomeIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public  void finish(){

        //重置闪烁状态
        for (UserLocationData userLocation : TryDemoHomeListContructor.getInstance().getUserLocationDataList()){
            SharePreferenceUtil.setPrefInt(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH,
                    String.valueOf(userLocation.getLocationID()), DashBoadConstant.DEAFAULT_SCENARIO_MODEL);
        }

        //删除所有正在发送的handle消息
        for (BaseRequestFragment homeFragment : mHomeFragmentList){
            if (homeFragment instanceof HomeFragment && ((HomeFragment)homeFragment).getHomeControlManager() != null){
                ((HomeFragment)homeFragment).getHomeControlManager().removeAllMessage();
            }
        }

        TryDemoHomeListContructor.getInstance().exitTryDemoModel();
        mTryDemoIndicatorValueManager.exitTryDemoProcess();

        TryDemoDataConstructor.exitTryDemo();

        if (mHomeAndDeviceStatusReceiver != null){
            unregisterReceiver(mHomeAndDeviceStatusReceiver);
        }
        if (isFromStartActivity) {
            Intent i = new Intent();
            i.setClass(this, StartActivity.class);
            i.putExtra(StartActivity.FROM_ANOTHER_ACTIVITY,true);
            startActivity(i);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

        super.finish();
    }

    private void startChangeIndicatorValue(){
        mTryDemoIndicatorValueManager.startChangePMValue();
    }

    @Override
    public void doRefreshUIOpr() {
        //进行数据变化
        if (!AppManager.isEnterPriseVersion()) {
            ((HomeFragment) mHomeFragmentList.get(0)).refreshFragment(TryDemoHomeListContructor.getInstance().getUserLocationDataList().get(0), 0);
        }

        mAllDeviceFramentList.get(0).refreshAllDeviceFragment(TryDemoHomeListContructor.getInstance().getUserLocationDataList().get(0));

        initArrowViewAndErrorNumber(mCurrentHomeIndex,mHomeFragmentList.size());

        initHomeListView(true);
    }


    /**
     * 点击每个tab时候界面的变化
     */
    @Override
    protected void setLayoutVisible() {
        setTitleLayoutVisibleInEveryTab();
    }

    /**
     * 在每一个tab页面中页面的显示状态
     */
    private void setTitleLayoutVisibleInEveryTab() {
        int homeSize = UserDataOperator.getHomePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList()).size();
        boolean isHasHome = homeSize > 0 ? true : false;
        if (mCurrentTab == DASHBOARD && !AppManager.isEnterPriseVersion()) {
            if (isHasHome) {
                showDashboardViewPager(homeSize);
            }

        } else if (mCurrentTab == ALL_DEVICE) {
            if (isHasHome ) {
                showAllDeviceViewPager();
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentHomeIndex = position;
        List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),
                TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList());
        if (mCurrentHomeIndex < userLocationDataList.size()) {
            mCurrentLocationId = userLocationDataList.get(mCurrentHomeIndex).getLocationID();
        }

        if (position < userLocationDataList.size()) {
            mCurrentAllDeviceIndex = position;
        }

    }
}


