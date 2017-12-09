package com.honeywell.hch.airtouch.ui.schedule.controller.presenter;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.schedule.controller.IArriveHomeView;

/**
 * Created by honeywell on 26/12/2016.
 */

public interface IArriveHomePresenter {

    void startGetDeviceDetailInfo();

    void setArrvieHomeTimeTask(int locationId, IRequestParams requestParams, IActivityReceive iReceiveResponse);

    void showTimeAfterGetRunstatus();

    UserLocationData getLocationDataByLocationId(int locationId);

    void initPresenter(UserLocationData userLocationData,IArriveHomeView iArriveHomeView);
}
