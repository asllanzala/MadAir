package com.honeywell.hch.airtouch.ui.main.ui.common;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.baidu.android.pushservice.PushManager;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.ble.manager.BLEManager;
import com.honeywell.hch.airtouch.plateform.ble.service.BluetoothLeService;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;
import com.honeywell.hch.airtouch.plateform.storage.UpdateVersionPreference;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomIconViewItem;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.main.manager.common.MainActivityUIManager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirBleDataManager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.ui.MadAirFragment;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.view.HomeFragment;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.view.NoLocationHomeFragment;
import com.honeywell.hch.airtouch.ui.main.ui.devices.view.AllDeviceFragment;
import com.honeywell.hch.airtouch.ui.main.ui.devices.view.NoLocationAllDeviceFragment;
import com.honeywell.hch.airtouch.ui.main.ui.me.MeFragment;
import com.honeywell.hch.airtouch.ui.main.ui.me.feedback.FeedBackActivity;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessagesFragment;
import com.honeywell.hch.airtouch.ui.main.ui.title.view.DashboardTitlePageIndicator;
import com.honeywell.hch.airtouch.ui.notification.manager.baidupushnotification.BaiduPushMessageReceiver;
import com.honeywell.hch.airtouch.ui.notification.manager.config.BaiduPushConfig;
import com.honeywell.hch.airtouch.ui.service.TimerRefreshService;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;
import com.honeywell.hch.airtouch.ui.update.ui.UpdateVersionMinderActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import static com.honeywell.hch.airtouch.plateform.config.HPlusConstants.REFRESH_MADAIR_DATA;

//import com.honeywell.hch.airtouch.ui.main.ui.me.MadAirDetailInfoFragment;

/**
 * Created by h127856 on 7/18/16.
 */
public class MainActivity extends MainBaseActivity {


    private List<MadAirDeviceModel> mMadAirDevices = new ArrayList<>();
    private List<MadAirDeviceModel> mMadAirNeedAutoConnectDevices = new ArrayList<>();
    private static final int BLE_AUTO_CONNECT_POLLING_INTERVAL = 5000;


    private TimerRefreshService mServiceBinder;
    private HomeAndDeviceStatusReceiver mHomeAndDeviceStatusReceiver;


    private BaseRequestFragment mMessagesFragment;
    private BaseRequestFragment mMeFragment;


    /**
     * 没有家或是没有缓存数据并且加载中，加载失败等情况显示的dashboard以及devices界面
     */
    private FrameLayout mNoLocationDashboardFrameLayout;
    private FrameLayout mNoLocationDevicesFrameLayout;
    private BaseRequestFragment mLoadingDashboardFragment;
    private BaseRequestFragment mLoadingDevicesFragment;

    private boolean isBind = false;


    private class HomeAndDeviceStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!mAllDeviceTitlePageIndicator.isEditStatus()) {
                String action = intent.getAction();
                if (HPlusConstants.SHORTTIME_REFRESH_END_ACTION.equals(action)) {
                    //location 变化
                    initFramglentList();
                    //update unread message and error device
                    initErrorDeviceInAccount();
                    initUnreadMessageInAccount();
                    mMeFragment.setNetWorkViewGone();
                    mMessagesFragment.setNetWorkViewGone();
                } else if (HPlusConstants.LOGOUT_ACTION.equals(action)) {
                    logoutAction();
                } else if (HPlusConstants.ADD_DEVICE_OR_HOME_ACTION.equals(action)) {
                    int locationId = intent.getIntExtra(HPlusConstants.LOCAL_LOCATION_ID, 0);
                    List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                            UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
                    int index = 0;
                    boolean isHasThisLocation = false;
                    for (UserLocationData userLocationData : userLocationDataList) {
                        if (userLocationData.getLocationID() == locationId) {
                            isHasThisLocation = true;
                            break;
                        }
                        index++;
                    }
                    mCurrentHomeIndex = isHasThisLocation ? index : 0;

                    mCurrentAllDeviceIndex = mCurrentHomeIndex;
                    if (mCurrentAllDeviceIndex >= userLocationDataList.size()) {
                        mCurrentAllDeviceIndex = userLocationDataList.size() - 1;
                    }
                    if (mCurrentTab == ALL_DEVICE) {
                        mViewPager.setCurrentItem(mCurrentAllDeviceIndex);
                    } else {
                        mViewPager.setCurrentItem(mCurrentHomeIndex);
                    }
                } else if (HPlusConstants.SET_DEFALUT_HOME.equals(action)) {
                    refreshTitleLayout();
                } else if (HPlusConstants.ENTER_ENROLL_PROCESS.equals(action)) {
                    stopTimerRefreshThread();
                } else if (HPlusConstants.EXIT_ENROLL_PROCESS.equals(action)) {
                    startTimerRefreshThread();
                } else if (HPlusConstants.LONG_REFRESH_END_ACTION.equals(action)) {
                    if (mDashboardPageIndicator != null) {
                        mDashboardPageIndicator.initPresenter(mCurrentHomeIndex);
                    }
                    refreshMadAirWeather();
                } else if (HPlusConstants.UPDATE_ME_RED_DOT_ACTION.equals(action)) {
                    setMeRedDot();
                    setMeNewVersionLayout();
                } else if (REFRESH_MADAIR_DATA.equals(action)) {
                    allDeviceFragmentRefresh();
                    refreshMadAirFragment();
                } else if (HPlusConstants.GPS_RESULT.equals(action)) {
                    refreshMadAirGpsResult();
                } else if (BluetoothLeService.ACTION_PHONE_BLUETOOTH_ON.equals(action)) {
                    MadAirDeviceModelSharedPreference.saveStatus(MadAirDeviceStatus.DISCONNECT);
                } else if (BluetoothLeService.ACTION_PHONE_BLUETOOTH_OFF.equals(action)) {
                    MadAirDeviceModelSharedPreference.saveStatus(MadAirDeviceStatus.BLE_DISABLE);
                }
            }

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);

        mMainActivityUIManager = new MainActivityUIManager(UserAllDataContainer.shareInstance().getUserLocationDataList(), UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());

        mNoLocationDashboardFrameLayout = (FrameLayout) findViewById(R.id.loading_dashborad_contentFrame);
        mNoLocationDevicesFrameLayout = (FrameLayout) findViewById(R.id.loading_devices_contentFrame);
        setMeRedDot();   //设置小红点初始化，有可能下载了app，但是没有安装，更新
        initBottomNavigateView();
        mLoadingDevicesFragment = NoLocationAllDeviceFragment.newInstance(this);
        showNolocationFragment(mLoadingDevicesFragment, R.id.loading_devices_contentFrame);
        initMessageFragment();
        initMeFragment();
        initFramglentList();
        registerUserAliveChangedReceiver();
        initService();
        dealWithIntent(getIntent());
        dealWithPushNotify();
        startAutoConnectThread();
        UpdateManager.getInstance().checkUpgrade();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // start BLE service
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            this.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            MadAirBleDataManager.getInstance().registerBleReceiver();
        }

    }

    @Override
    public void dealWithNoNetWork() {
        setAllDevicesTitleStatus();
        mDashboardPageIndicator.initPresenter(mCurrentHomeIndex);

        setNoworkActionOfFragment(mMeFragment);
        setNoworkActionOfFragment(mMessagesFragment);
        setNoworkActionOfFragment(mLoadingDashboardFragment);
        setNoworkActionOfFragment(mLoadingDevicesFragment);

        homeFragmentDealNetwork();
    }

    @Override
    public void dealWithNetworkError() {
        setAllDevicesTitleStatus();
        mDashboardPageIndicator.initPresenter(mCurrentHomeIndex);

        setNetworkErrorActionOfFragment(mMeFragment);

        setNetworkErrorActionOfFragment(mMessagesFragment);
        setNetworkErrorActionOfFragment(mLoadingDashboardFragment);

        setNetworkErrorActionOfFragment(mLoadingDevicesFragment);


        homeFragmentDealNetwork();
    }

    @Override
    public void dealNetworkConnected() {
        setAllDevicesTitleStatus();

        setNetworkConnectActionOfFramgent(mMeFragment);
        setNetworkConnectActionOfFramgent(mMessagesFragment);
        setNetworkConnectActionOfFramgent(mLoadingDevicesFragment);

        refreshNoLocationHomeFragment();
        refreshNoLocationDevicesFragment();
        homeFragmentDealNetwork();

    }

    private void setNetworkErrorActionOfFragment(BaseRequestFragment fragment) {
        if (fragment != null) {
            fragment.setNetWorkErrorView();
        }
    }

    private void setNoworkActionOfFragment(BaseRequestFragment fragment) {
        if (fragment != null) {
            fragment.setNoNetWorkView();
        }
    }

    private void setNetworkConnectActionOfFramgent(BaseRequestFragment fragment) {
        if (fragment != null) {
            fragment.setNetWorkViewGone();
        }
    }

    private void refreshNoLocationHomeFragment() {
        if (mLoadingDashboardFragment != null) {
            ((NoLocationHomeFragment) mLoadingDashboardFragment).initNoLocationView();
        }
    }

    private void refreshNoLocationDevicesFragment() {
        if (mLoadingDevicesFragment != null) {
            ((NoLocationAllDeviceFragment) mLoadingDevicesFragment).initNoLocationView();
        }
    }

    private void homeFragmentDealNetwork() {
        triggerFragmentRefresh();
    }

    private void setAllDevicesTitleStatus() {
        if (mAllDeviceTitlePageIndicator != null && !mAllDeviceTitlePageIndicator.isEditStatus()) {
            mAllDeviceTitlePageIndicator.setTitleNormalStatus();
        } else if (mAllDeviceTitlePageIndicator != null) {
            mAllDeviceTitlePageIndicator.setEditStatusNetworkErrorView();
        }
    }

    private void dealWithPushNotify() {
        PushMessageModel pushMessageModel = UserAllDataContainer.shareInstance().getmPushMessageModel();
        if (pushMessageModel != null) {
            Class mClass = BaiduPushConfig.startEntrance(pushMessageModel);
            if (mClass != null) {
                if (mClass != MainActivity.class) {
                    Intent pushIntent = new Intent(mContext, mClass);
                    pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    pushIntent.putExtra(PushMessageModel.PUSHPARAMETER, (Serializable) pushMessageModel);
                    mContext.startActivity(pushIntent);
                    UserAllDataContainer.shareInstance().setmPushMessageModel(null);
                } else {
                    //广播推送
                    clickMessageButton();
                    UserAllDataContainer.shareInstance().setmPushMessageModel(null);
                }

            }
        }
    }

    private void initMessageFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mMessagesFragment = MessagesFragment.newInstance(this);
        transaction.add(R.id.message_contentFrame, mMessagesFragment);
        transaction.commit();
        mMessagesFrameLayout.setVisibility(View.GONE);
    }

    private void initMeFragment() {
        mMeFragment = MeFragment.newInstance(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.me_contentFrame, mMeFragment);
        transaction.commit();
        mMeFrameLayout.setVisibility(View.GONE);
    }

    /**
     *
     */
    private void showLoadingFrament(boolean isDashBoardTap) {
        if (isDashBoardTap) {
            if (mLoadingDashboardFragment == null) {
                mLoadingDashboardFragment = NoLocationHomeFragment.newInstance(this);
                showNolocationFragment(mLoadingDashboardFragment, R.id.loading_dashborad_contentFrame);
            }
            refreshNoLocationHomeFragment();
            mNoLocationDashboardFrameLayout.setVisibility(View.VISIBLE);
            mNoLocationDevicesFrameLayout.setVisibility(View.GONE);
        } else {
            if (mLoadingDevicesFragment == null) {
                mLoadingDevicesFragment = NoLocationAllDeviceFragment.newInstance(this);
                showNolocationFragment(mLoadingDevicesFragment, R.id.loading_devices_contentFrame);
            }
            refreshNoLocationDevicesFragment();
            mNoLocationDevicesFrameLayout.setVisibility(View.VISIBLE);
            mNoLocationDashboardFrameLayout.setVisibility(View.GONE);
        }

    }

    private void showNolocationFragment(BaseRequestFragment fragment, int fragmentId) {
        if (getSupportFragmentManager().findFragmentById(fragmentId) != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(fragmentId, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void hideTheLoadingFragment() {
        mNoLocationDevicesFrameLayout.setVisibility(View.GONE);
        mNoLocationDashboardFrameLayout.setVisibility(View.GONE);
    }

    private void removeLoadingFragment() {
        removeNoLocationFragment(mLoadingDashboardFragment, R.id.loading_dashborad_contentFrame);
        mLoadingDashboardFragment = null;
        removeNoLocationFragment(mLoadingDevicesFragment, R.id.loading_devices_contentFrame);
        mLoadingDevicesFragment = null;
        hideTheLoadingFragment();
    }

    private void removeNoLocationFragment(BaseRequestFragment loadingFragment, int frameLayoutId) {
        if (getSupportFragmentManager().findFragmentById(frameLayoutId) == loadingFragment && loadingFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(loadingFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void initErrorDeviceInAccount() {
        mBottomNavigationView.setAllDeviceNavigationRedDot(UserDataOperator.isHasErrorOrUnSupportDevice(UserAllDataContainer.shareInstance().getUserLocationDataList()));
    }

    private void initUnreadMessageInAccount() {
        mBottomNavigationView.setMessageNavigationRedDot(UserAllDataContainer.shareInstance().isHasUnReadMessage());
    }

    private void setMeRedDot() {
        mBottomNavigationView.setMeNavigationRedDot(UpdateVersionPreference.getMeRedDot());
    }

    private void setMeNewVersionLayout() {
        ((MeFragment) mMeFragment).setVersionRl();
        mBottomNavigationView.setMeNavigationRedDot(UpdateVersionPreference.getMeRedDot());
    }

    public void clearRedDot() {
        mBottomNavigationView.setMessageNavigationRedDot(false);
    }

    /**
     * 刷新或是初始化的时候
     */
    private void initFramglentList() {
        if (UserAllDataContainer.shareInstance().isLoadDataSuccess()) {
            int preLocationId = mCurrentLocationId;
            //有家的情况
            List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(), UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
            int homesize = userLocationDataList.size();
            int realLocationSize = UserAllDataContainer.shareInstance().getRealLocationSize();
            updateFragmentList(homesize, userLocationDataList, realLocationSize);

            triggerFragmentRefresh();
            if (homesize > 0) {
                updateCurrentIndex(userLocationDataList, preLocationId, realLocationSize);

                mCurrentLocationId = userLocationDataList.get(mCurrentHomeIndex).getLocationID();

                if (preLocationId != mCurrentLocationId) {
                    hideCityListAndSetDropIconStuas();
                }
                mViewPager.setCurrentItem(mCurrentHomeIndex);

            } else {
                hideCityListAndSetDropIconStuas();
            }
            setEveryTabVisbileLoadingSuccess();

        } else {
            setLayoutVisible();
        }

    }

    //更新index
    private void updateCurrentIndex(List<UserLocationData> userLocationDataList, int preLocationId, int realLocationSize) {
        int index = 0;
        int defaultLocationId = UserInfoSharePreference.getDefaultHomeId();

        for (UserLocationData userLocationData : userLocationDataList) {
//                    refreshHomeFragmentFragmentInViewPager(index, userLocationData);

            //第一次进来需要显示的是默认设置的default home界面
            if (mCurrentHomeIndex == INIT_DEFAULT_HOME_INDEX
                    && defaultLocationId == userLocationData.getLocationID()) {
                mCurrentHomeIndex = index;
                preLocationId = userLocationData.getLocationID();
            } else if (mCurrentHomeIndex != INIT_DEFAULT_HOME_INDEX &&
                    mCurrentLocationId == userLocationData.getLocationID()) {
                //刷新的时候，如果家有增删的时候，要保证显示是的当前滑动到的家，所以通过location id来确定
                mCurrentHomeIndex = index;

                refreshTitleLayout();
            }
            index++;
        }
        if (mCurrentHomeIndex >= userLocationDataList.size()) {
            mCurrentHomeIndex = userLocationDataList.size() - 1;
        }
        if (mCurrentHomeIndex == INIT_DEFAULT_HOME_INDEX) {
            mCurrentHomeIndex = 0;
            preLocationId = userLocationDataList.get(mCurrentHomeIndex).getLocationID();
        }
        if (realLocationSize - 1 < mCurrentHomeIndex) {
            mCurrentAllDeviceIndex = realLocationSize;
        } else {
            mCurrentAllDeviceIndex = mCurrentHomeIndex;
        }
    }

    private void updateFragmentList(int homesize, List<UserLocationData> userLocationDataList, int realLocationSize) {
        //有家的情况
        //初始化alldevice 数据
        List<UserLocationData> userLocationDataListForAllDevice = UserDataOperator.getAllDevicePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        List<VirtualUserLocationData> virtualUserLocationDataList = UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList();
        int virtuallocationSize = UserAllDataContainer.shareInstance().getVirtualLocationSize();
        int allDeviceSize = userLocationDataListForAllDevice.size();

        int currentTotalSizeAllDevice = getTotalViewPagerForAllDeviceSize();  //当前 alldeivce 家
        int currentTotalSize = getTotalViewPagerSize(); //当前home 家
        int currentRealHomeSize = mMainActivityUIManager.getCurrentRealHomeList(mHomeFragmentList);
        int currentVirtualHomeSize = mMainActivityUIManager.getCurrentVirtualHomeList(mHomeFragmentList);
        int currentVirtualAllDeviceSize = mMainActivityUIManager.getCurrentVirtualAllDeviceList(mAllDeviceFramentList);
        int currentRealAllDeviceSize = mMainActivityUIManager.getCurrentRealAllDeviceList(mAllDeviceFramentList);

        if (!AppManager.isEnterPriseVersion()) {
            if (currentRealHomeSize < realLocationSize) {
                for (int i = 0; i < realLocationSize - currentRealHomeSize; i++) {
                    mHomeFragmentList.add(0, HomeFragment.newInstance(userLocationDataList.get(i), 0, this));
                    mHomePageAdapter.notifyDataSetChanged();
                }
            } else {
                for (int i = 0; i < currentRealHomeSize - realLocationSize; i++) {
                    mHomeFragmentList.remove(0);
                    mHomePageAdapter.notifyDataSetChanged();
                }
            }
            if (currentVirtualHomeSize < virtuallocationSize) {
                for (int i = 0; i < virtuallocationSize - currentVirtualHomeSize; i++) {
                    mHomeFragmentList.add(getTotalViewPagerSize(), MadAirFragment.newInstance(this, virtualUserLocationDataList.get(currentVirtualHomeSize + i), MadAirBleDataManager.getInstance()));
                    mHomePageAdapter.notifyDataSetChanged();
                }
            } else {
                //需要找到对应的 被删除的 madairfragment
                for (BaseRequestFragment homeFragment : mHomeFragmentList) {
                    if (homeFragment instanceof MadAirFragment) {
                        boolean isNeedRemove = true;
                        for (VirtualUserLocationData virtualUserLocationData : virtualUserLocationDataList) {
                            if (virtualUserLocationData.getLocationID() == ((MadAirFragment) homeFragment).getmUserLocationData().getLocationID()) {
                                isNeedRemove = false;
                                break;
                            }
                        }
                        if (isNeedRemove) {
                            mHomeFragmentList.remove(homeFragment);
                            mHomePageAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
        //add realhome
        if (currentRealAllDeviceSize < realLocationSize) {
            for (int i = 0; i < realLocationSize - currentRealAllDeviceSize; i++) {
                addFragmentToViewPagerForAllDevice(0, userLocationDataListForAllDevice.get(i));
                mHomePageAdapter.notifyDataSetChanged();
            }
        }
        // add virtualhome
        if (currentVirtualAllDeviceSize == 0 && virtuallocationSize != 0) {
            addFragmentToViewPagerForAllDevice(mAllDeviceFramentList.size(), userLocationDataListForAllDevice.get(allDeviceSize - 1));
            mHomePageAdapter.notifyDataSetChanged();
        }
        if (currentTotalSizeAllDevice > allDeviceSize) {
            int removeCount = currentTotalSizeAllDevice - allDeviceSize;
            if (virtuallocationSize == 0 && currentVirtualAllDeviceSize != 0) {
                removeFragmentFromViewPagerForAllDevice(currentTotalSizeAllDevice - 1);
                mHomePageAdapter.notifyDataSetChanged();
                removeCount--;
            }
            for (int i = 0; i < removeCount; i++) {
                removeFragmentFromViewPagerForAllDevice(i);
                mHomePageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void refreshTitleLayout() {
        initHomeListView(true);
    }


    private void addFragmentToViewPagerForAllDevice(int index, UserLocationData userLocationData) {
        mAllDeviceFramentList.add(index, AllDeviceFragment.newInstance(userLocationData, this));

    }

    private void removeFragmentFromViewPagerForAllDevice(int index) {
        if (index < mAllDeviceFramentList.size()) {
            mAllDeviceFramentList.remove(index);
        }
    }

    private void refreshHomeFragmentFragmentInViewPager(int index, UserLocationData userLocationData) {
        if (!AppManager.isEnterPriseVersion()) {
            if (index < mHomeFragmentList.size()) {
                BaseRequestFragment fragment = mHomeFragmentList.get(index);
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).refreshFragment(userLocationData, index);
                } else if (fragment instanceof MadAirFragment) {
                    ((MadAirFragment) fragment).refreshFragment(userLocationData, index);
                }
            }
        }
    }

    private void registerUserAliveChangedReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
        intentFilter.addAction(HPlusConstants.LOGOUT_ACTION);
        intentFilter.addAction(HPlusConstants.LONG_REFRESH_END_ACTION);
        intentFilter.addAction(HPlusConstants.ADD_DEVICE_OR_HOME_ACTION);
        intentFilter.addAction(HPlusConstants.SET_DEFALUT_HOME);
        intentFilter.addAction(HPlusConstants.ENTER_ENROLL_PROCESS);
        intentFilter.addAction(HPlusConstants.EXIT_ENROLL_PROCESS);
        intentFilter.addAction(HPlusConstants.UPDATE_ME_RED_DOT_ACTION);
        intentFilter.addAction(REFRESH_MADAIR_DATA);
        intentFilter.addAction(HPlusConstants.GPS_RESULT);
        intentFilter.addAction(BluetoothLeService.ACTION_PHONE_BLUETOOTH_ON);
        intentFilter.addAction(BluetoothLeService.ACTION_PHONE_BLUETOOTH_OFF);
        mHomeAndDeviceStatusReceiver = new HomeAndDeviceStatusReceiver();
        registerReceiver(mHomeAndDeviceStatusReceiver, intentFilter);
    }

    private void unHomeAndDeviceStatusReceiver() {
        if (mHomeAndDeviceStatusReceiver != null) {
            unregisterReceiver(mHomeAndDeviceStatusReceiver);
        }
    }

    /**
     * 点击每个tab时候界面的变化
     */
    @Override
    protected void setLayoutVisible() {
        setEveryTabVisbileLoadingSuccess();
        triggerFragmentRefresh();
    }

    private void triggerFragmentRefresh() {
        List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        if (userLocationDataList.size() > 0 && mViewPager != null && mViewPager.getVisibility() == View.VISIBLE) {
            int index = 0;

            if (!AppManager.isEnterPriseVersion()) {
                for (UserLocationData userLocationData : userLocationDataList) {
                    refreshHomeFragmentFragmentInViewPager(index, userLocationData);
                    index++;
                }
            }
            allDeviceFragmentRefresh();
        }
    }

    private void refreshMadAirFragment() {
        for (BaseRequestFragment homeFragment : mHomeFragmentList) {
            if (homeFragment instanceof MadAirFragment) {
                ((MadAirFragment) homeFragment).refreshData();
            }
        }
    }

    private void refreshMadAirGpsResult() {
        for (BaseRequestFragment homeFragment : mHomeFragmentList) {
            if (homeFragment instanceof MadAirFragment) {
                ((MadAirFragment) homeFragment).refreshGpsResult();
            }
        }
    }

    private void allDeviceFragmentRefresh() {
        int indexAlldevice = 0;
        List<UserLocationData> userLocationDataListForAllDevice = UserDataOperator.getAllDevicePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        for (UserLocationData userLocationData : userLocationDataListForAllDevice) {
            if (indexAlldevice < mAllDeviceFramentList.size()) {
                mAllDeviceFramentList.get(indexAlldevice).refreshAllDeviceFragment(userLocationData);
                indexAlldevice++;
            }
        }
    }

    private void setEveryTabVisbileLoadingSuccess() {
        setTitleLayoutVisibleInEveryTab();
    }

    private void setNoHomeWhenLoadingSuccess() {
        setOtherLayoutGoneExceptMeTab();
        StatusBarUtil.changeStatusBarTextColor(mParentView, View.SYSTEM_UI_FLAG_VISIBLE);
        if (mCurrentTab == DASHBOARD || mCurrentTab == ALL_DEVICE) {
            showLoadingFrament(mCurrentTab == DASHBOARD);
        } else {
            hideTheLoadingFragment();
        }
    }

    private void setOtherLayoutGoneExceptMeTab() {
        mViewPager.setVisibility(View.GONE);
        mDashboardPageIndicator.setVisibility(View.GONE);
        mAllDeviceTitlePageIndicator.setVisibility(View.GONE);
        mArrowLayout.setVisibility(View.GONE);
        mMessagesFrameLayout.setVisibility(View.GONE);
        mMeFrameLayout.setVisibility(View.GONE);
        mNoLocationDashboardFrameLayout.setVisibility(View.GONE);
        mNoLocationDevicesFrameLayout.setVisibility(View.GONE);
    }

    /**
     * 在每一个tab页面中页面的显示状态
     */
    private void setTitleLayoutVisibleInEveryTab() {
        int homeSize = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(), UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()).size();
        boolean isHasHome = homeSize > 0 ? true : false;
        boolean isHasLoadedViewPage = getTotalViewPagerSize() > 0 ? true : false;
        if (mCurrentTab == DASHBOARD && !AppManager.isEnterPriseVersion()) {
            if (isHasHome && isHasLoadedViewPage) {
                showDashboardViewPager(homeSize);
                removeLoadingFragment();
            } else if (!isHasHome) {
                setNoHomeWhenLoadingSuccess();
            }

        } else if (mCurrentTab == ALL_DEVICE) {
            if (isHasHome && isHasLoadedViewPage) {
                showAllDeviceViewPager();
                removeLoadingFragment();
            } else if (!isHasHome) {
                setNoHomeWhenLoadingSuccess();
            }

        } else {
            hideTheLoadingFragment();
            StatusBarUtil.changeStatusBarTextColor(mParentView, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setOtherLayoutGoneExceptMeTab();
            if (mCurrentTab == MESSAGE && !AppManager.isEnterPriseVersion()) {
                mMessagesFrameLayout.setVisibility(View.VISIBLE);
                mMeFrameLayout.setVisibility(View.GONE);
                ((MessagesFragment) mMessagesFragment).getMessagesFromServer();
            } else if (mCurrentTab == ME) {
                mMessagesFrameLayout.setVisibility(View.GONE);
                mMeFrameLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取AllDeviceFragment list
     *
     * @return
     */
    public List<AllDeviceFragment> getAllDeviceFramentList() {
        return mAllDeviceFramentList;
    }


    private void initBottomNavigateView() {
        List<BottomIconViewItem> bottomIconViewItemList = new ArrayList<>();
        addBottomNavigateView(bottomIconViewItemList);

        if (!AppManager.isEnterPriseVersion()) {
            bottomIconViewItemList.add(mBottomNavigationView.addBottomViewItem(R.drawable.msg_active, R.drawable.msg_inactive, getString(R.string.messages_btn_text), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickMessageButton();

                }
            }));
        }


        bottomIconViewItemList.add(mBottomNavigationView.addBottomViewItem(R.drawable.me_active, R.drawable.me_inactive, getString(R.string.me_btn_text), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentTab = ME;
                StatusBarUtil.changeStatusBarTextColor(mParentView, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                mBottomNavigationView.setItemClick(getString(R.string.me_btn_text));
                hideTheLoadingFragment();
                setOtherLayoutGoneExceptMeTab();
                mMessagesFrameLayout.setVisibility(View.GONE);
                mMeFrameLayout.setVisibility(View.VISIBLE);
            }
        }));

        mBottomNavigationView.addBtnToNavigationView(bottomIconViewItemList);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onNewIntent: ");
        dealWithIntent(intent);
        //message  center logic
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //如果远程控制的推送 不进入message center
            PushMessageModel pushMessageModel = (PushMessageModel) bundle.getSerializable(PushMessageModel.PUSHPARAMETER);
            if (pushMessageModel != null) {
                Class mClass = BaiduPushConfig.startEntrance(pushMessageModel);
                if (mClass != null && mClass == MainActivity.class) {
                    clickMessageButton();  //广播类型的推送
                    return;
                }
            }

            if (bundle.getBoolean(AuthorizeBaseActivity.INTENTPARAMETEROBJECT)) {
                MessageBox.createSimpleDialog(this, null, getString(R.string.authorization_deleted), getString(R.string.ok), null);
            }

        }
    }

    public void clickMessageButton() {
        mCurrentTab = MESSAGE;

        mBottomNavigationView.setItemClick(getString(R.string.messages_btn_text));

        setLayoutVisible();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unHomeAndDeviceStatusReceiver();
        if (isBind) {

            unbindService(serviceConnection);
            isBind = false;
        }
        UpdateManager.getInstance().setNeedCheck(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            BLEManager.getInstance().unregisterBleServiceBroadcast();
            MadAirBleDataManager.getInstance().unregisterBleReceiver();
            unbindService(mServiceConnection);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {

            if (mAllDeviceTitlePageIndicator.isEditStatus()) {
                setAllDeviceEditStatusFromIndiacator(false);
                mAllDeviceTitlePageIndicator.setTitleNormalStatus();
            } else if (mMessagesFragment != null && ((MessagesFragment) mMessagesFragment).onkeyDownBack()) {
                return false;
            } else {
                MessageBox.createTwoButtonDialog(MainActivity.this, null, getString(R.string
                                .quit), getString(R.string.no), null,
                        getString(R.string.yes), quitAction);
            }

        }

        return false;
    }


    private MessageBox.MyOnClick quitAction = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {

            BLEManager.getInstance().disconnectAllDevices();

            //clear notification
            NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancelAll();
            //save ument statistics
            UmengUtil.onKillProcess(mContext);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    };


    private void hideCityListAndSetDropIconStuas() {
        hideCityListView();
        mDashboardPageIndicator.setDropDownImageView(true);
        mAllDeviceTitlePageIndicator.setDropDownImageView(true);
    }


    public void setMessageEditStatusFromIndiacator(boolean isEditStatus) {
        if (mMessagesFrameLayout != null && mMessagesFrameLayout.getVisibility() == View.VISIBLE) {
            if (isEditStatus) {
                mNavagateLayout.setVisibility(View.GONE);
            } else {
                mNavagateLayout.setVisibility(View.VISIBLE);
            }
        }

    }


    public void flingTheDeviceTitle() {
        if (mAllDeviceTitlePageIndicator != null) {
            mAllDeviceTitlePageIndicator.dealWithNetWorkResponse();
        }
    }


    private void logoutAction() {
        //清空该应用通知栏
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        UserInfoSharePreference.clearUserSharePreferencce(MainActivity.this);
        BLEManager.getInstance().disconnectAllDevices();
        MadAirDeviceModelSharedPreference.clearData();
        UserAllDataContainer.shareInstance().clear();
        if (isBind) {
            unbindService(serviceConnection);
            isBind = false;
        }
        PushManager.stopWork(MainActivity.this);
        BaiduPushMessageReceiver.registerWithNotificationHubs("");
        UserAllDataContainer.shareInstance().setmPushMessageModel(null);
        CloseActivityUtil.exitEnrollClient(this);
        Intent intent2 = new Intent();
        intent2.putExtra(StartActivity.FROM_ANOTHER_ACTIVITY, true);
        intent2.setClass(MainActivity.this, StartActivity.class);
        startActivity(intent2);
        backIntent();
    }


    /**
     * Service to GetRunStatus 30s each
     */
    private void initService() {
        if (!isBind) {
            try {
                Intent intent = new Intent(this, Class.forName(TimerRefreshService.class
                        .getName()));
                isBind = bindService(intent, serviceConnection,
                        TimerRefreshService.BIND_AUTO_CREATE);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.log(LogUtil.LogLevel.VERBOSE, TAG, "in onServiceDisconnected");
            mServiceBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.log(LogUtil.LogLevel.VERBOSE, TAG, "in onServiceConnected");
            mServiceBinder = ((TimerRefreshService.MyBinder) service).getService();
        }
    };

    public void stopTimerRefreshThread() {
        if (mServiceBinder != null) {
            mServiceBinder.stopRefreshThread();
        }
    }

    public void startTimerRefreshThread() {
        if (mServiceBinder != null) {
            mServiceBinder.startRefreshThread();
        }
    }


    @Override
    public void onPageSelected(int position) {
        mCurrentHomeIndex = position;
        List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        if (mCurrentHomeIndex < userLocationDataList.size()) {
            mCurrentLocationId = userLocationDataList.get(mCurrentHomeIndex).getLocationID();
        }

        if (position < userLocationDataList.size()) {
            mCurrentAllDeviceIndex = position;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DashBoadConstant.GROUP_CONTROL_REQUEST_CODE:
                if (mCurrentHomeIndex < mAllDeviceFramentList.size() && mCurrentHomeIndex >= 0) {
                    mAllDeviceFramentList.get(mCurrentHomeIndex).dealGroupControlForResult();
                }
                break;
            case DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE:
                if (data != null) {
                    if (mCurrentHomeIndex < mAllDeviceFramentList.size() && mCurrentHomeIndex >= 0) {
                        mAllDeviceFramentList.get(mCurrentHomeIndex).dealDeviceControlForResult(data);
                    }
                }
                break;
            case FeedBackActivity.SUBMIT_FEEDBACK_REQUEST_CODE:
                if (resultCode == FeedBackActivity.SUBMIT_FEEDBACK) {


                    if (mMeFragment.getNetWorkErrorLayout() != null && mMeFragment.getNetWorkErrorLayout().getVisibility() == View.VISIBLE) {
                        return;
                    }

                    ((MeFragment) mMeFragment).dealWithAcitivityResult();

                }
                break;
            default:
                break;
        }
    }

//----------------------------------------update-------------------------------------------------------


    private void dealWithIntent(Intent intent2) {
        boolean isUpdate = intent2.getBooleanExtra(PushMessageModel.PUSHPARAMETERUPDATE, false);
        if (isUpdate) {
            Intent intent = new Intent(mContext, UpdateVersionMinderActivity.class);
            intent.putExtra(UpdateManager.UPDATE_TYPE, HPlusConstants.NOTIFICATION_UPDATE);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }


    public FrameLayout getMessagesFragment() {
        return mMessagesFrameLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onResume----");
        CloseActivityUtil.exitBeforeLoginClient(mContext);
        CloseActivityUtil.exitEnrollClient(mContext);
        CloseActivityUtil.printEnrollActivity(this);
        //enroll finish 启动刷新广播
        Intent intent = new Intent(HPlusConstants.EXIT_ENROLL_PROCESS);
        sendBroadcast(intent);
    }

    public DashboardTitlePageIndicator getmDashboardPageIndicator() {
        return mDashboardPageIndicator;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BLEManager.getInstance().setBleServiceFromServiceConnected(service);
            if (!BLEManager.getInstance().initBluetoothLeService()) {
                LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "Unable to initialize Bluetooth");
            }
        }


        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            BLEManager.getInstance().unregisterBleServiceBroadcast();
        }
    };

    private void startAutoConnectThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    try {
                        Thread.sleep(BLE_AUTO_CONNECT_POLLING_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (hasAutoConnectDevices()) {
                        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "======auto connect");

                        for (MadAirDeviceModel device : mMadAirNeedAutoConnectDevices)
                            BLEManager.getInstance().connectBle(device.getMacAddress());
                    }
                }
            }
        }).start();

    }

    private boolean hasAutoConnectDevices() {

        mMadAirNeedAutoConnectDevices.clear();

        mMadAirDevices = MadAirDeviceModelSharedPreference.getDeviceList();

        if (mMadAirDevices == null || mMadAirDevices.size() == 0)
            return false;

        for (MadAirDeviceModel device : mMadAirDevices) {
            if (device.getDeviceStatus() == MadAirDeviceStatus.DISCONNECT && !device.getDeviceName().equals(""))
                mMadAirNeedAutoConnectDevices.add(device);
        }

        return mMadAirNeedAutoConnectDevices.size() > 0;
    }

}

