package com.honeywell.hch.airtouch.ui.schedule.controller.presenter;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserLocation;
import com.honeywell.hch.airtouch.plateform.http.task.GetDeviceDetailInfoTask;
import com.honeywell.hch.airtouch.plateform.http.task.SetArrvieHomeTimeTask;
import com.honeywell.hch.airtouch.ui.schedule.controller.IArriveHomeView;

import java.util.List;

/**
 * Created by honeywell on 26/12/2016.
 */

public class ArriveHomePresenter implements IArriveHomePresenter {

    private UserLocationData mUserLocationData;
    private IArriveHomeView iArriveHomeView;

    public ArriveHomePresenter(){

    }

    public void initPresenter(UserLocationData userLocationData,IArriveHomeView iArriveHomeView){
        mUserLocationData = userLocationData;
        this.iArriveHomeView = iArriveHomeView;
    }

    @Override
    public void showTimeAfterGetRunstatus() {
        String arriveTime = getArriveTime();
        if (StringUtil.isEmpty(arriveTime)){
            iArriveHomeView.noArriveHomeTimeLayout();
        }else{
            iArriveHomeView.hasArriveHomeTimeLayout();
        }
        iArriveHomeView.showTimeAfterGetRunstatus(arriveTime);
    }

    @Override
    public void startGetDeviceDetailInfo() {
        if (!GetDeviceDetailInfoTask.isTaskRunning()) {
            GetDeviceDetailInfoTask requestTask
                    = new GetDeviceDetailInfoTask();
            AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
        }
    }

    @Override
    public void setArrvieHomeTimeTask(int locationId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {

        SetArrvieHomeTimeTask arrvieHomeTimeTask = new SetArrvieHomeTimeTask(locationId, requestParams, iReceiveResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(arrvieHomeTimeTask);
    }


    @Override
    public UserLocationData getLocationDataByLocationId(int locationId) {
        if (locationId != 0) {
            return UserDataOperator.getLocationWithId(locationId, UserAllDataContainer.shareInstance().getUserLocationDataList(), UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        }
        return null;
    }

    private String getArriveTime(){
        String arriveTime = "";
        List<HomeDevice> mHomeDeviceList = mUserLocationData.getHomeDevicesList();
        if (mHomeDeviceList != null && mHomeDeviceList.size() > 0) {
            for (int i = 0; i < mHomeDeviceList.size(); i++) {
                HomeDevice homeDeviceItem = mHomeDeviceList.get(i);
                if (homeDeviceItem != null && homeDeviceItem instanceof AirTouchDeviceObject) {
                    AirtouchRunStatus runStatus = ((AirTouchDeviceObject) homeDeviceItem).getAirtouchDeviceRunStatus();
                    if (runStatus != null && runStatus.isCleanBeforeHomeEnable()) {
                        arriveTime = runStatus.getTimeToHome();
                        break;
                    }
                }
            }
        }
        return arriveTime;
    }
}
