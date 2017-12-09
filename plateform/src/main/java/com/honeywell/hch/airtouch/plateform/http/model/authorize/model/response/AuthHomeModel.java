package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthGroupModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qian Jin on 2/1/16.
 * <p/>
 * -- My device
 * -- Jin Home
 * -- Jin Device
 * -- Vincent Home
 * -- Vincent Device
 * -- Authorize to me
 * -- Stephen Home
 * -- Stephen Device
 */
public class AuthHomeModel implements  Serializable {
    public static final String AUTH_HOME_DATA = "auth_home_data";

    @SerializedName("locationName")
    private String mLocationName;

    @SerializedName("locationId")
    private int LocationNameId;

    @SerializedName("isLocationOwner")
    private boolean mIsLocationOwner;

    @SerializedName("deviceList")
    private List<AuthDeviceModel> mAuthDevices;

    @SerializedName("groupList")
    private List<AuthGroupModel> mAuthGroups;


    private String mOwnerName;

    private int mOwnerId;

    public String getLocationName() {
        return mLocationName;
    }

    public int getLocationNameId() {
        return LocationNameId;
    }

    public List<? extends AuthBaseModel> getAuthDevices() {
        return mAuthDevices;
    }

    private List<AuthBaseModel> mAuthBaseModelList;

    public List<AuthBaseModel> setmAuthBaseListModel() {
        List<AuthBaseModel> authBaseModelList = new ArrayList<>();
        if (mAuthGroups != null && mAuthGroups.size() > 0) {
            authBaseModelList.addAll(mAuthGroups);
        }
        if (mAuthDevices != null && mAuthDevices.size() > 0) {
            authBaseModelList.addAll(mAuthDevices);
        }
        mAuthBaseModelList = authBaseModelList;
        return mAuthBaseModelList;
    }

    public List<AuthBaseModel> getmAuthBaseModel() {
        if (mAuthBaseModelList == null) {
            mAuthBaseModelList = setmAuthBaseListModel();
        }
        return mAuthBaseModelList;
    }

    public List<? extends AuthBaseModel> getmAuthGroups() {
        return mAuthGroups;
    }

    public void setmAuthGroups(List<AuthGroupModel> mAuthGroups) {
        this.mAuthGroups = mAuthGroups;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    public void setLocationNameId(int locationNameId) {
        LocationNameId = locationNameId;
    }

    public void setIsLocationOwner(boolean isLocationOwner) {
        mIsLocationOwner = isLocationOwner;
    }

    public void setAuthDevices(List<AuthDeviceModel> authDevices) {
        mAuthDevices = authDevices;
    }

    public boolean isLocationOwner() {
        return mIsLocationOwner;
    }

    public void setAuthDevieLocationName() {
        for (AuthBaseModel authDeviceModel : mAuthDevices) {
            authDeviceModel.setmLocationName(mLocationName);
        }
    }

    /**
     * 当前类别Item总数。Category也需要占用一个Item
     *
     * @return
     */
    public int getItemCount() {
        return mAuthBaseModelList.size() + 1;
    }

    /**
     * 获取Item内容
     *
     * @param pPosition
     * @return
     */
    public Object getItem(int pPosition) {
        // Category排在第一位
        if (pPosition == 0) {
            return mLocationName;
        } else {
            mAuthBaseModelList.get(pPosition - 1).setmIsLocationOwner(mIsLocationOwner);
            return mAuthBaseModelList.get(pPosition - 1);
        }
    }

    public boolean ismIsLocationOwner(int mPosition) {
        return isLocationOwner();
    }

    public String getmOwnerName() {
        return mOwnerName;
    }

    public void setmOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public int getmOwnerId() {
        return mOwnerId;
    }

    public void setmOwnerId(int mOwnerId) {
        this.mOwnerId = mOwnerId;
    }
}
