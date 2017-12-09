package com.honeywell.hch.airtouch.plateform.http.manager;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.model.MadAirModelManager;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.AirtouchCapability;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROCapability;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by allanhwmac on 7/27/15.
 * <p/>
 * UI和 后台数据的唯一接口
 */
public class UserAllDataContainer {

    //只有loading cache失败（状态不为LOADING__CACHE_DATA_SUCCESS）并且 loading网络数据失败的时候才设置为 LOADED_FAILED_STATUS
    public static final int LOADED_FAILED_STATUS = -2;

    /**
     * 只有在loading cache成功的状态情况下，刷新数据失败后，会进入这个状态。
     */
    public static final int CACHE_SUCCESS_BUT_REFRESH_FAILED_STATUS = -1;

    //进入界面，如果状态不为 LOADING__CACHE_DATA_SUCCESS，这个时候需要界面显示loading状态
    public static final int LOADING_STATUS = 0;

    //cache数据加载成功。
    public static final int LOADING_CACHE_DATA_SUCCESS = 1;
    //网络数据加载成功
    public static final int LOADED_SUCCESS_STATUS = 2;

    //
    private CopyOnWriteArrayList<UserLocationData> mUserLocationDataList = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<VirtualUserLocationData> mVirtualUserLocationDataList = new CopyOnWriteArrayList<>();  //virtualLocationData

//    private List<UserLocationData> mUserLocationDataForAllDeviceList = new ArrayList<>();  //virtualLocationData

    private static UserAllDataContainer mUserDataManagerInstance = new UserAllDataContainer();

    private WeatherRefreshManager mWeatherRefreshManger;

    private PushMessageModel mPushMessageModel;

    private Map<Integer, AirtouchCapability> mAirtouchCapabilityMap = new HashMap<>();

    private Map<Integer,SmartROCapability> mSmartROCapabilityMap = new HashMap<>();


    private ArrayList<MessageModel> loadMessageResponseList;

    /**
     * 这个变量是用于判断进入dashboard时，是否已经获了数据
     */
    private int mDashboardLoadingStatus = LOADING_STATUS;


    private boolean isHasUnReadMessage = false;

    private MadAirModelManager madAirModelManager = new MadAirModelManager();

    /**
     * 是否有网络超时或是连接服务器失败
     */
    private volatile boolean isHasNetWorkError = false;

    private UserAllDataContainer() {
    }

    public static UserAllDataContainer shareInstance() {
        return mUserDataManagerInstance;
    }

    /**
     * 退出时候，需要清空这些数据，否则换账号登录会有问题
     */
    public void clear() {
        mUserLocationDataList.clear();
        mVirtualUserLocationDataList.clear();
        mPushMessageModel = null;
        mAirtouchCapabilityMap.clear();
        mSmartROCapabilityMap.clear();
        loadMessageResponseList = null;
        mDashboardLoadingStatus = LOADING_STATUS;
        isHasUnReadMessage = false;
        isHasNetWorkError = false;
    }

    public List<UserLocationData> getUserLocationDataList() {
        return mUserLocationDataList;
    }

    public void setUserLocationDataList(CopyOnWriteArrayList<UserLocationData> userLocationDataList) {
        mUserLocationDataList.clear();
        this.mUserLocationDataList = userLocationDataList;
    }

    public List<VirtualUserLocationData> getmVirtualUserLocationDataList() {
        return mVirtualUserLocationDataList;
    }


    public void deleteUserLocation(UserLocationData userLocationData) {
        mUserLocationDataList.remove(userLocationData);
    }



    public WeatherRefreshManager getWeatherRefreshManager() {
        if (mWeatherRefreshManger == null) {
            mWeatherRefreshManger = new WeatherRefreshManager(AppManager.getInstance().getApplication().getApplicationContext());
        }
        return mWeatherRefreshManger;
    }




    public int getRealLocationSize() {
        return mUserLocationDataList.size();
    }

    public int getVirtualLocationSize() {
        return mVirtualUserLocationDataList.size();
    }


    public PushMessageModel getmPushMessageModel() {
        return mPushMessageModel;
    }

    public void setmPushMessageModel(PushMessageModel mPushMessageModel) {
        this.mPushMessageModel = mPushMessageModel;
    }



    public int getAllDeviceNumber() {
        int deviceNumber = 0;
        for (UserLocationData userLocationData : mUserLocationDataList) {
            deviceNumber += userLocationData.getHomeDevicesList().size();
        }
        return deviceNumber;
    }





    public Map<Integer, AirtouchCapability> getAirtouchCapabilityMap() {
        return mAirtouchCapabilityMap;
    }


    public Map<Integer, SmartROCapability> getSmartROCapabilityMap() {
        return mSmartROCapabilityMap;
    }

//    private List<String> getCityList(List<UserLocationData> userLocations) {
//        List<String> cityList = new ArrayList<>();
//        for (UserLocationData userLocationData : userLocations) {
//            cityList.add(userLocationData.getCity());
//        }
//        return cityList;
//    }




    public void setDashboardLoadFailed() {
        if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()) && !isHasNetWorkError()) {
            if (mDashboardLoadingStatus == LOADING_STATUS) {
                mDashboardLoadingStatus = LOADED_FAILED_STATUS;
            }
            if (mDashboardLoadingStatus == LOADING_CACHE_DATA_SUCCESS) {
                mDashboardLoadingStatus = CACHE_SUCCESS_BUT_REFRESH_FAILED_STATUS;
            }
        }
    }




    public void setDashboardLoadSuccess() {
        mDashboardLoadingStatus = LOADED_SUCCESS_STATUS;
    }

    public void setDashboardLoadingCacheSuccess() {
        mDashboardLoadingStatus = LOADING_CACHE_DATA_SUCCESS;
    }

    public boolean isLoadingData() {
        return mDashboardLoadingStatus == LOADING_STATUS;
    }

    public boolean isLoadDataFailed() {
        return mDashboardLoadingStatus == LOADED_FAILED_STATUS;
    }

    public boolean isLoadDataSuccess() {
        return mDashboardLoadingStatus == LOADED_SUCCESS_STATUS || mDashboardLoadingStatus == LOADING_CACHE_DATA_SUCCESS;
    }

    public boolean isLoadingCacheSuccess() {
        return mDashboardLoadingStatus == LOADING_CACHE_DATA_SUCCESS;
    }

    public boolean isLoadingNetworkSuccess() {
        return mDashboardLoadingStatus == LOADED_SUCCESS_STATUS;
    }

    public boolean isLoadCacheSuccessButRefreshFailed() {
        return mDashboardLoadingStatus == CACHE_SUCCESS_BUT_REFRESH_FAILED_STATUS;
    }

    public boolean isHasNetWorkError() {
        return isHasNetWorkError;
    }

    public void setHasNetWorkError(boolean hasNetWorkError) {
        isHasNetWorkError = hasNetWorkError;
    }

    public boolean isHasUnReadMessage() {
        return isHasUnReadMessage;
    }

    public void setHasUnReadMessage(boolean hasUnReadMessage) {
        isHasUnReadMessage = hasUnReadMessage;
    }

    public ArrayList<MessageModel> getLoadMessageResponseList() {
        return loadMessageResponseList;
    }

    public void setLoadMessageResponseList(ArrayList<MessageModel> loadMessageResponseList) {
        this.loadMessageResponseList = loadMessageResponseList;
    }

    public void updateMadAirData() {
        madAirModelManager.readMadAirData(mVirtualUserLocationDataList);
    }

    public void deleteMadAirDevice(String macAddress) {
        madAirModelManager.deleteMadAirDeviceForHomePage(macAddress);
    }



}
