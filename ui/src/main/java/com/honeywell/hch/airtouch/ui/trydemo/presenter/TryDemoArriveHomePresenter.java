package com.honeywell.hch.airtouch.ui.trydemo.presenter;

import android.os.Handler;
import android.os.Message;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.BackHomeRequest;
import com.honeywell.hch.airtouch.ui.schedule.controller.IArriveHomeView;
import com.honeywell.hch.airtouch.ui.schedule.controller.presenter.IArriveHomePresenter;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoConstant;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeListContructor;

/**
 * Created by honeywell on 26/12/2016.
 */

public class TryDemoArriveHomePresenter implements IArriveHomePresenter{


    private UserLocationData mUserLocationData;
    private IArriveHomeView iArriveHomeView;
    private IActivityReceive mActivityReceive;

    public TryDemoArriveHomePresenter(){
    }

    public void initPresenter(UserLocationData userLocationData,IArriveHomeView iArriveHomeView){
        mUserLocationData = userLocationData;
        this.iArriveHomeView = iArriveHomeView;
    }

    @Override
    public void startGetDeviceDetailInfo() {

    }

    @Override
    public void setArrvieHomeTimeTask(int locationId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        if(((BackHomeRequest)requestParams).getIsEnableCleanBeforeHome()){
            SharePreferenceUtil.setPrefString(TryDemoConstant.TRY_DEMO_SP,TryDemoConstant.ARRIVE_TIME_KEY,((BackHomeRequest)requestParams).getTimeToHome());
        }else{
            SharePreferenceUtil.setPrefString(TryDemoConstant.TRY_DEMO_SP,TryDemoConstant.ARRIVE_TIME_KEY,"");
        }
        mActivityReceive = iReceiveResponse;
        ResponseResult resultResponse = new ResponseResult();
        resultResponse.setRequestId(RequestID.ARRIVE_HOME_TIME);
        resultResponse.setResult(true);
        mActivityReceive.onReceive(resultResponse);

    }

    @Override
    public void showTimeAfterGetRunstatus() {
        String arriveTime =  SharePreferenceUtil.getPrefString(TryDemoConstant.TRY_DEMO_SP,TryDemoConstant.ARRIVE_TIME_KEY,"");
        if (StringUtil.isEmpty(arriveTime)){
            iArriveHomeView.noArriveHomeTimeLayout();
        }else{
            iArriveHomeView.hasArriveHomeTimeLayout();
        }
        iArriveHomeView.showTimeAfterGetRunstatus(arriveTime);
    }


    @Override
    public UserLocationData getLocationDataByLocationId(int locationId) {
        return UserDataOperator.getLocationWithId(locationId, TryDemoHomeListContructor.getInstance().getUserLocationDataList(), TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList());
    }

}
