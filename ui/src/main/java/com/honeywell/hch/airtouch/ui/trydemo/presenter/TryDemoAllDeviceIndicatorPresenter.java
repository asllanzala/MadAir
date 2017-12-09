package com.honeywell.hch.airtouch.ui.trydemo.presenter;

import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.main.ui.title.presenter.AllDeviceIndicatorBasePresenter;
import com.honeywell.hch.airtouch.ui.main.ui.title.view.IAllDeviceIndactorView;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeListContructor;

/**
 * Created by h127856 on 7/26/16.
 */
public class TryDemoAllDeviceIndicatorPresenter extends AllDeviceIndicatorBasePresenter {


    public TryDemoAllDeviceIndicatorPresenter(IAllDeviceIndactorView indactorView, int index) {
        mAllDeviceIndicatorView = indactorView;
        mCurrentIndex = index;
    }

    public TryDemoAllDeviceIndicatorPresenter(IAllDeviceIndactorView indactorView) {
        mAllDeviceIndicatorView = indactorView;
    }

    

    protected UserLocationData getUserLocationFromList() {
        try {
            return UserDataOperator.getAllDevicePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList()).get(mCurrentIndex);
        } catch (Exception e) {

        }
        return null;
    }

    public int getDataSourceSize(){
        return UserDataOperator.getAllDevicePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList()).size();
    }
}
