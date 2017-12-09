package com.honeywell.hch.airtouch.ui.main.ui.title.presenter;

import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.main.ui.title.view.IAllDeviceIndactorView;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeListContructor;

/**
 * Created by h127856 on 7/26/16.
 */
public class AllDeviceIndicatorPresenter extends AllDeviceIndicatorBasePresenter {

    public AllDeviceIndicatorPresenter(IAllDeviceIndactorView indactorView, int index) {
        mAllDeviceIndicatorView = indactorView;
        mCurrentIndex = index;
    }

    public AllDeviceIndicatorPresenter(IAllDeviceIndactorView indactorView) {
        mAllDeviceIndicatorView = indactorView;
    }



    protected UserLocationData getUserLocationFromList() {
        try {
            return UserDataOperator.getAllDevicePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()).get(mCurrentIndex);
        } catch (Exception e) {

        }
        return null;
    }

    public int getDataSourceSize(){
        return UserDataOperator.getAllDevicePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()).size();
    }

}
