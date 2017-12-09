package com.honeywell.hch.airtouch.ui.main.ui.dashboard.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlBaseManager;
import com.honeywell.hch.airtouch.ui.main.manager.common.HomeControlManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.GroupManager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.view.IHomeFragmentView;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by h127856 on 7/21/16.
 */
public class HomeFramentPresenter implements IHomeFragmentPresenter {

    private final String TAG = "HomeFramentPresenter";

    private static final int AWAY_SCENARIO_MODEL = 0;
    private static final int AT_HOME_SCENARIO_MODEL = 1;
    private static final int SLEEP_SCENARIO_MODEL = 2;
    private static final int AWAKE_SCENARIO_MODEL = 3;

    private static final int AWAY_SCENARIO_VIEW_INDEX = 3;
    private static final int AT_HOME_SCENARIO_VIEW_INDEX = 0;
    private static final int SLEEP_SCENARIO_VIEW_INDEX = 1;
    private static final int AWAKE_SCENARIO_VIEW_INDEX = 2;

    @NonNull
    private IHomeFragmentView mHomeFragmentView;

    private UserLocationData mUserLocationData;


    private int mIndex = 0;

    private ControlBaseManager mGroupManager;


    public HomeFramentPresenter(IHomeFragmentView homeFragment, UserLocationData userLocationData, int index, ControlBaseManager controlBaseManager) {
        mHomeFragmentView = homeFragment;
        mUserLocationData = userLocationData;
        mIndex = index;

        mGroupManager = controlBaseManager;
        initManagerCallBack();
    }


    public void resetHomeFragmentPresenter(IHomeFragmentView homeFragment, UserLocationData userLocationData, int index) {
        mHomeFragmentView = homeFragment;
        mUserLocationData = userLocationData;
        mIndex = index;
    }


    /**
     * 当界面创建的时候调用
     */
    protected void initManagerCallBack() {
        setErrorCallback(mErrorCallBack);
        setSuccessCallback(mSuccessCallback);
    }

    protected GroupManager.SuccessCallback mSuccessCallback = new GroupManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected GroupManager.ErrorCallback mErrorCallBack = new GroupManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    protected void dealSuccessCallBack(ResponseResult responseResult) {
        dealSuccessCallBack(responseResult);
        switch (responseResult.getRequestId()) {
            case MULTI_COMM_TASK:
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "dealSuccessCallBack");
                break;
        }
    }

    /*
        group control update group name need overide
     */
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        int responseCode = responseResult.getResponseCode();
        switch (responseResult.getRequestId()) {
            case CONTROL_HOME_DEVICE:
                mHomeFragmentView.setControlFaile(responseCode);
                break;

        }
    }

    @Override
    public void initHomeFragment() {
        initWholeHomeFragment();
        if (UserAllDataContainer.shareInstance().isHasNetWorkError()
                || !NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            initNetworkErrorAndOff();
        }
    }


    @Override
    public void controlAtHomeModel() {
        controlHomeDevice(AT_HOME_SCENARIO_MODEL);
    }

    @Override
    public void controlSleepModel() {
        controlHomeDevice(SLEEP_SCENARIO_MODEL);
    }

    @Override
    public void controlAwakeModel() {
        controlHomeDevice(AWAKE_SCENARIO_MODEL);
    }

    @Override
    public void controlAwayModel() {
        controlHomeDevice(AWAY_SCENARIO_MODEL);
    }


    public int getLocationScenarioIndexView() {
        int verIndex = DashBoadConstant.DEAFAULT_SCENARIO_MODEL;
        if (mUserLocationData != null) {
            int scenarioModel = mUserLocationData.getOperationModel();
            verIndex = getScenarioIndexView(scenarioModel);
        }

        return verIndex;
    }

    public int getLocationId() {
        if (mUserLocationData != null) {
            return mUserLocationData.getLocationID();
        }
        return 0;
    }


    private void initWholeHomeFragment() {
        initHomeFragmentView();
    }

    private void initNetworkErrorAndOff() {
        String msg = "";
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            msg = AppManager.getInstance().getApplication().getString(R.string.network_off_msg);
        } else if (UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            msg = AppManager.getInstance().getApplication().getString(R.string.network_error_msg);
        }
        mHomeFragmentView.setNetWorkErrorTopViewBackground();
        mHomeFragmentView.setTopViewTip(msg);
        if (UserAllDataContainer.shareInstance().isLoadDataFailed()) {
            mHomeFragmentView.setNetWorkErrorNoData();
        }
    }


    private void initHomeFragmentView() {
        /**
         * 要区分
         * 1.是否有设备
         * 2，是否有家
         * 3.是否有网络（没有数据的时候）
         */
        if (mUserLocationData != null && mUserLocationData.getHomeDevicesList().size() > 0) {
            initTopViewHasDevice();
            initDeviceClassifyView();
            initScenarioControlView();
            initArriveHomeIcon();
            //TODO: 如果100G系列设备，隐藏homecontrol 功能，并显示提示语
            if (mUserLocationData.isAllAirTouch100GSeries() && isSelfHome()) {
                mHomeFragmentView.setNoControllableDevice();
            }
        } else if (mUserLocationData != null) {
            initNoDeviceView();
        }
    }

    private void initTopViewHasDevice() {
        mHomeFragmentView.setDefaultHomeIcon(isDefaultHome(), isSelfHome());
        mHomeFragmentView.setTopViewBackground(UserDataOperator.isHasUnnormalStatusExceptUnSupport(mUserLocationData), isHasUnSupportDevice());
        mHomeFragmentView.setTopViewTip(getTopBottomMsgStr());
    }

    private void initNoDeviceView() {
        mHomeFragmentView.setDefaultHomeIcon(isDefaultHome(), isSelfHome());

        mHomeFragmentView.setNoDeviceView();
    }


    /**
     * 更新第二层（空气PM2.5,Water的水质）显示，
     */
    private void initDeviceClassifyView() {
        mHomeFragmentView.initDeviceClassifyView(getDeviceCatogery(), isOnlyHasUnSupportDevice());

        if (mUserLocationData.getDefaultPMDevice() != null) {
            mHomeFragmentView.setAirtouchPMValue(getWorstAirtouchDevicePMValue(), getAirtouchDevicePMValueColor(), isPmValueHight());
        }
        if (mUserLocationData.getDefaultWaterDevice() != null) {
            mHomeFragmentView.setWaterQuality(getWorstWaterQuality(), getWorstWaterQualityColor(), isWaterQualityBad());
        }
        if (mUserLocationData.getDefaultTVOCDevice() != null) {
            int tvocColor = mUserLocationData.getDefaultTVOCDevice().getTvocFeature().getTVOCColor();
            mHomeFragmentView.setAirtouchTVOCValue(mUserLocationData.getDefaultTVOCDevice().getTvocFeature().getTVOC(),
                    tvocColor, mUserLocationData.getDefaultTVOCDevice().getTvocFeature().getTvocLevel() > DashBoadConstant.TVOC_GOOD_LEVEL);
        }
    }

    private void initScenarioControlView() {
        if (SharePreferenceUtil.getPrefInt(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH, String.valueOf(mUserLocationData.getLocationID()), DashBoadConstant.DEAFAULT_SCENARIO_MODEL)
                != DashBoadConstant.DEAFAULT_SCENARIO_MODEL) {
            int scenaroModel = SharePreferenceUtil.getPrefInt(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH, String.valueOf(mUserLocationData.getLocationID()), DashBoadConstant.DEAFAULT_SCENARIO_MODEL);
            mHomeFragmentView.setScenarioModelViewFlashing(getScenarioIndexView(scenaroModel));
        } else {
            mHomeFragmentView.initScenarioModelView(getScenarioIndexView(mUserLocationData.getOperationModel()));
        }

    }

    private void initArriveHomeIcon() {
        int visible = mUserLocationData.getDefaultPMDevice() == null && mUserLocationData.getDefaultTVOCDevice() == null ? View.GONE : View.VISIBLE;
        mHomeFragmentView.setArriveHomeIconVisible(visible);
    }

    private int getScenarioIndexView(int scenarioModel) {
        if (scenarioModel == AWAY_SCENARIO_MODEL) {
            return AWAY_SCENARIO_VIEW_INDEX;
        } else if (scenarioModel == AT_HOME_SCENARIO_MODEL) {
            return AT_HOME_SCENARIO_VIEW_INDEX;
        } else if (scenarioModel == SLEEP_SCENARIO_MODEL) {
            return SLEEP_SCENARIO_VIEW_INDEX;
        } else if (scenarioModel == AWAKE_SCENARIO_MODEL) {
            return AWAKE_SCENARIO_VIEW_INDEX;
        }
        return scenarioModel;
    }


    /**
     * 是否是默认家
     *
     * @return
     */
    private boolean isDefaultHome() {
        return UserDataOperator.isDefaultHome(mUserLocationData, mIndex);
    }

    /**
     * 是否是自己的家，如果不是就是授权过来的
     *
     * @return
     */
    private boolean isSelfHome() {
        return mUserLocationData.isIsLocationOwner();
    }


    /**
     * 获取设备类型，返回图标以及字符串
     *
     * @return
     */
    private Map<Integer, String> getDeviceCatogery() {
        Map<Integer, String> result = new LinkedHashMap<>();
        if (mUserLocationData.getDefaultPMDevice() != null
                && mUserLocationData.getDefaultTVOCDevice() != null && mUserLocationData.getDefaultWaterDevice() != null) {
            result.put(R.drawable.air, AppManager.getInstance().getApplication().getString(R.string.pm25_str));
            result.put(R.drawable.tvoc_icon, AppManager.getInstance().getApplication().getString(R.string.all_device_tvoc));
            result.put(R.drawable.water, AppManager.getInstance().getApplication().getString(R.string.dashboard_water_quality));
        } else if (mUserLocationData.getDefaultPMDevice() != null
                && mUserLocationData.getDefaultTVOCDevice() != null) {
            result.put(R.drawable.air, AppManager.getInstance().getApplication().getString(R.string.pm25_str));
            result.put(R.drawable.tvoc_icon, AppManager.getInstance().getApplication().getString(R.string.all_device_tvoc));
        } else if (mUserLocationData.getDefaultPMDevice() != null && mUserLocationData.getDefaultWaterDevice() != null) {
            result.put(R.drawable.air, AppManager.getInstance().getApplication().getString(R.string.pm25_str));
            result.put(R.drawable.water, AppManager.getInstance().getApplication().getString(R.string.dashboard_water_quality));

        } else if (mUserLocationData.getDefaultPMDevice() != null) {
            result.put(R.drawable.air, AppManager.getInstance().getApplication().getString(R.string.pm25_str));
        } else if (mUserLocationData.getDefaultWaterDevice() != null) {
            result.put(R.drawable.water, AppManager.getInstance().getApplication().getString(R.string.dashboard_water_quality));

        }
        return result;
    }


    private String getWorstAirtouchDevicePMValue() {
        return mUserLocationData.getDefaultPMDevice().getPmSensorFeature().getPm25Value();
    }

    private int getAirtouchDevicePMValueColor() {
        return mUserLocationData.getDefaultPMDevice().getPmSensorFeature().getPm25Color();
    }

    private boolean isPmValueHight() {
        return mUserLocationData.getDefaultPMDevice().getiDeviceStatusFeature().isDeviceStatusWorse();
    }


    private boolean isWaterQualityBad() {
        return mUserLocationData.getDefaultWaterDevice().getiDeviceStatusFeature().isDeviceStatusWorse();
    }

    /**
     * 是否有标红的设备指标
     *
     * @return
     */
    private boolean isHasUnnormalStatusInThisHome() {
        return UserDataOperator.isHasUnnormalStatusInHome(mUserLocationData);
    }

    /**
     * 家里只有全部是不支持的设备，才会在dashboard里显示
     *
     * @return
     */
    private boolean isHasUnSupportDevice() {
        return mUserLocationData.isHasUnsupportDeviceInHome();
    }

    /**
     * 只有不支持设备的时候
     *
     * @return
     */
    private boolean isOnlyHasUnSupportDevice() {
        return mUserLocationData.getHomeDevicesList().size() > 0 && mUserLocationData.getUnSupportDevice() != null;
    }

    private String getTopBottomMsgStr() {

        if (mUserLocationData.isHasErrorInThisHome()) {
            return AppManager.getInstance().getApplication().getString(R.string.has_error_msg);
        }

        if (mUserLocationData.isHasUnsupportDeviceInHome()) {
            return AppManager.getInstance().getApplication().getString(R.string.unsupport_device_top_msg_str);
        }


        if (UserDataOperator.isAirtouchWorse(mUserLocationData) && UserDataOperator.isWaterWorse(mUserLocationData)
                || UserDataOperator.isWaterWorse(mUserLocationData) && UserDataOperator.isAirtouchTVOCWorse(mUserLocationData)) {
            return AppManager.getInstance().getApplication().getString(R.string.all_device_worse);
        } else if (UserDataOperator.isAirtouchWorse(mUserLocationData) || UserDataOperator.isAirtouchTVOCWorse(mUserLocationData)
                || (UserDataOperator.isAirtouchWorse(mUserLocationData) && UserDataOperator.isAirtouchTVOCWorse(mUserLocationData))) {
            return AppManager.getInstance().getApplication().getString(R.string.only_airtouch_worse);
        } else if (UserDataOperator.isWaterWorse(mUserLocationData)) {
            return AppManager.getInstance().getApplication().getString(R.string.only_water_worse);
        } else {
            return AppManager.getInstance().getApplication().getString(R.string.no_device_worse);
        }
    }


    private String getWorstWaterQuality() {
        String worstQuality = HPlusConstants.DATA_LOADING_STATUS;

        if (mUserLocationData.getDefaultWaterDevice() != null &&
                ((WaterDeviceObject) mUserLocationData.getDefaultWaterDevice()).getAquaTouchRunstatus() != null) {
            worstQuality = mUserLocationData.getDefaultWaterDevice().getWaterQualityFeature().showQualityLevel();
        }
        return worstQuality;
    }

    private int getWorstWaterQualityColor() {
        if (((WaterDeviceObject) mUserLocationData.getDefaultWaterDevice()).getAquaTouchRunstatus() != null) {
            return mUserLocationData.getDefaultWaterDevice().getWaterQualityFeature().showQualityColor();
        }

        return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
    }

    public UserLocationData getmUserLocationData() {
        return mUserLocationData;
    }

    public void controlHomeDevice(int scenario) {
        mGroupManager.controlHomeDevice(mUserLocationData.getLocationID(), scenario);
    }


    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(GroupManager.SuccessCallback successCallback) {
        mGroupManager.setSuccessCallback(successCallback);
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(GroupManager.ErrorCallback errorCallback) {
        mGroupManager.setErrorCallback(errorCallback);
    }


    public ControlBaseManager  getGroupManager(){
        return mGroupManager;
    }

}
