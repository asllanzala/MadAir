package com.honeywell.hch.airtouch.ui.main.manager.common;

import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCityDashBoard;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.ui.MadAirFragment;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.view.HomeFragment;
import com.honeywell.hch.airtouch.ui.main.ui.devices.view.AllDeviceFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by h127856 on 16/8/1.
 */
public class MainActivityUIManager {

    private List<CategoryHomeCityDashBoard> mHomeList = new ArrayList<>();

    private List<UserLocationData> mUserLocationDataList = new ArrayList<>();

    private List<VirtualUserLocationData> mVirtualUserLocationDataList = new ArrayList<>();  //virtualLocationData


    /**
     * 获取当前家的左边有异常家的个数
     *
     * @param currentIndex
     * @return
     */
    public int getLeftErrorHomeNumber(int currentIndex) {
        int number = 0;
        for (int i = 0; i < currentIndex; i++) {
            UserLocationData userLocationData = UserDataOperator.getHomePageUserLocationDataList(mUserLocationDataList,mVirtualUserLocationDataList).get(i);
            if (UserDataOperator.isHasUnnormalStatusInHome(userLocationData)) {
                number++;
            }
        }

        return number;
    }

    public MainActivityUIManager(List<UserLocationData> userLocationDataList,List<VirtualUserLocationData> virtualUserLocationDataList) {
        mUserLocationDataList = userLocationDataList;
        mVirtualUserLocationDataList = virtualUserLocationDataList;

    }

    /**
     * 获取当前家的右边有异常家的个数
     *
     * @param currentIndex
     * @return
     */
    public int getRightErrorHomeNumber(int currentIndex) {
        int number = 0;
        int totalSize = UserDataOperator.getHomePageUserLocationDataList(mUserLocationDataList,mVirtualUserLocationDataList).size();
        for (int i = currentIndex + 1; i < totalSize; i++) {
            if (i < totalSize) {
                UserLocationData userLocationData = UserDataOperator.getHomePageUserLocationDataList(mUserLocationDataList,mVirtualUserLocationDataList).get(i);
                if (UserDataOperator.isHasUnnormalStatusInHome(userLocationData)) {
                    number++;
                }
            }

        }
        return number;
    }


    public int getHomeListSize() {
        int size = 0;
        for (CategoryHomeCityDashBoard homeAndCity : mHomeList) {
            size += homeAndCity.getItemCount();
        }
        return size;
    }

    public List<CategoryHomeCityDashBoard> getHomeNameList() {
        mHomeList = UserDataOperator.getHomeListDashBoard(mUserLocationDataList,mVirtualUserLocationDataList);
        return mHomeList;
    }

    public List<CategoryHomeCityDashBoard> getHomeNameListAllDevice() {
        mHomeList = UserDataOperator.getHomeListAllDevice(mUserLocationDataList,mVirtualUserLocationDataList);
        return mHomeList;
    }

    public int getCurrentRealHomeList(List<BaseRequestFragment> homeFragmentList) {
        int realHomeSize = 0;
        for (BaseRequestFragment homeFragment : homeFragmentList) {
            if (homeFragment instanceof HomeFragment) {
                realHomeSize++;
            }
        }
        return realHomeSize;
    }

    public int getCurrentVirtualHomeList(List<BaseRequestFragment> homeFragmentList) {
        int virtualHomeSize = 0;
        for (BaseRequestFragment homeFragment : homeFragmentList) {
            if (homeFragment instanceof MadAirFragment) {
                virtualHomeSize++;
            }
        }
        return virtualHomeSize;
    }

    public int getCurrentVirtualAllDeviceList(List<AllDeviceFragment> homeFragmentList) {
        int virtualHomeSize = 0;
        for (AllDeviceFragment homeFragment : homeFragmentList) {
            if (((AllDeviceFragment) homeFragment).getUserLocationData() instanceof VirtualUserLocationData) {
                virtualHomeSize++;
            }
        }
        return virtualHomeSize;
    }

    public int getCurrentRealAllDeviceList(List<AllDeviceFragment> homeFragmentList) {
        int realHomeSize = 0;
        for (AllDeviceFragment homeFragment : homeFragmentList) {
            if (((AllDeviceFragment) homeFragment).getUserLocationData() instanceof RealUserLocationData) {
                realHomeSize++;
            }
        }
        return realHomeSize;
    }
}
