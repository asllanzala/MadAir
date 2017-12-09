package com.honeywell.hch.airtouch.ui.main.ui.title.presenter;

import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.main.ui.title.view.IAllDeviceIndactorView;

/**
 * Created by h127856 on 7/26/16.
 */
public class AllDeviceIndicatorBasePresenter implements IAllDeviceIndicatorPresenter {

    private final int DEFAULT_LOACTION = -1;
    protected IAllDeviceIndactorView mAllDeviceIndicatorView;
    protected int mCurrentIndex;

//    public AllDeviceIndicatorBasePresenter(IAllDeviceIndactorView indactorView, int index) {
//        mAllDeviceIndicatorView = indactorView;
//        mCurrentIndex = index;
//    }
//
//    public AllDeviceIndicatorBasePresenter(IAllDeviceIndactorView indactorView) {
//        mAllDeviceIndicatorView = indactorView;
//    }

    public void reInitPresenterIndex(int index) {
        mCurrentIndex = index;
    }

    @Override
    public void setAllDeviceView() {

        mAllDeviceIndicatorView.setHomeName(getLocationHomeName());

        mAllDeviceIndicatorView.setDefaultHomeIcon(isDefaultHome(), isSelfHome(),isRealHome());
    }


    /**
     * 是否是默认家
     *
     * @return
     */
    private boolean isDefaultHome() {
        UserLocationData userLocationData = getUserLocationFromList();
        if (userLocationData != null) {
            return UserDataOperator.isDefaultHome(getUserLocationFromList(), mCurrentIndex);
        }
        return false;
    }

    /**
     * 是否是自己的家，如果不是就是授权过来的
     *
     * @return
     */
    public boolean isSelfHome() {
        UserLocationData userLocationData = getUserLocationFromList();
        if (userLocationData != null) {
            return userLocationData.isIsLocationOwner();
        }
        return false;

    }


    /**
     * 获取家的名字
     *
     * @return
     */
    private String getLocationHomeName() {
        UserLocationData userLocationData = getUserLocationFromList();
        if (userLocationData != null) {
            return userLocationData.getAllDeviceTitleName();
        }
        return "";
    }

    private int getLocationId() {
        UserLocationData userLocationData = getUserLocationFromList();
        if (userLocationData != null) {
            return userLocationData.getLocationID();
        }
        return DEFAULT_LOACTION;
    }


    /**
     * 判断是真实家还是虚拟家
     *
     * @return
     */
    public boolean isRealHome() {
        UserLocationData userLocationData = getUserLocationFromList();
        if (userLocationData != null) {
            return userLocationData instanceof RealUserLocationData;
        }
        return false;

    }




    protected UserLocationData getUserLocationFromList() {
        return null;
    }

    public int getDataSourceSize(){
        return 0;
    }
}
