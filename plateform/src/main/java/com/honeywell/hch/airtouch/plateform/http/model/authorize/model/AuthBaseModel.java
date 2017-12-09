package com.honeywell.hch.airtouch.plateform.http.model.authorize.model;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthTo;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.CheckAuthUserResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.protocol.AuthorizeProtocol;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vincent on 1/4/16.
 */
public class AuthBaseModel implements Serializable, AuthorizeProtocol {
    public final int REVOKEASSIGNROLE = 0;

    protected final int HUNDRED = 100; //取最高位

    @SerializedName("ownerName")
    protected String mOwnerName;

    @SerializedName("ownerId")
    protected int mOwnerId;

    @SerializedName("role")
    protected int role;

    //    @SerializedName("locationName")
    protected String mLocationName;

    @SerializedName("authorityTo")
    protected List<AuthTo> mAuthorityToList;

    //first hierachy
    protected int parentPosition;

    //second hierachy
    protected int groupPosition;

    //third hierachy
    protected int childPosition;

    protected int mLocationId;

    protected Class mClass;

    protected boolean isLocationOwner;

    public boolean ismIsLocationOwner() {
        return isLocationOwner;
    }

    public void setmIsLocationOwner(boolean mIsLocationOwner) {
        this.isLocationOwner = mIsLocationOwner;
    }

    protected List<String> mGrantUserPhoneNumberList;
    protected List<String> mGrantUserNameList;
    protected List<CheckAuthUserResponse> mCheckAuthUserResponsesList;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getmLocationId() {
        return mLocationId;
    }

    public void setmLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public List<CheckAuthUserResponse> getmCheckAuthUserResponsesList() {
        return mCheckAuthUserResponsesList;
    }

    public void setmCheckAuthUserResponsesList(List<CheckAuthUserResponse> mCheckAuthUserResponsesList) {
        this.mCheckAuthUserResponsesList = mCheckAuthUserResponsesList;
    }

    public List<String> getmGrantUserName() {
        return mGrantUserNameList;
    }

    public void setmGrantUserName(List<String> mGrantUserName) {
        this.mGrantUserNameList = mGrantUserName;
    }

    //
    public List<String> getmGrantUserPhoneNumber() {
        return mGrantUserPhoneNumberList;
    }

    public void setmGrantUserPhoneNumber(List<String> mGrantUserPhoneNumber) {
        this.mGrantUserPhoneNumberList = mGrantUserPhoneNumber;
    }

    public List<AuthTo> getmAuthorityToList() {
        return mAuthorityToList;
    }

    public void setmAuthorityToList(List<AuthTo> mAuthorityToList) {
        this.mAuthorityToList = mAuthorityToList;
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public int getOwnerId() {
        return mOwnerId;
    }

    public String getmLocationName() {
        return mLocationName;
    }

    public int getParentPosition() {
        return parentPosition;
    }

    public void setOwnerId(int ownerId) {
        mOwnerId = ownerId;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    public void setAuthorityToList(List<AuthTo> authorityToList) {
        mAuthorityToList = authorityToList;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    //Set location name
    public void setmLocationName(String mLocationName) {
        this.mLocationName = mLocationName;
    }

    public void setLocationNameAndPosition(String mLocationName, int parentPosition, int groupPosition, int childPosition,
                                           int mLocationId, Class mClass) {
        this.mLocationName = mLocationName;
        this.parentPosition = parentPosition;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        this.mLocationId = mLocationId;
        this.mClass = mClass;
    }

    public void setLocationNameAndPosition(String mLocationName, int parentPosition, int mLocationId, Class mClass) {
        this.mLocationName = mLocationName;
        this.parentPosition = parentPosition;
        this.mLocationId = mLocationId;
        this.mClass = mClass;
    }

    public int parseRole() {
        int uiRole = 0;
        switch (role / HUNDRED) {
            case AuthTo.CONTROLLER / HUNDRED:
                uiRole = R.string.authorize_control_role;
                break;
            case AuthTo.OBSERVER / HUNDRED:
                uiRole = R.string.authorize_viewer_role;
                break;
            case AuthTo.MASTER / HUNDRED:
                uiRole = R.string.authorize_master_role;
                break;
            default:
                uiRole = R.string.authorize_viewer_role;
                break;
        }
        return uiRole;
    }

    public int getRemoveIcon(){
        return R.drawable.shared_to_me;
    }

    public int parseGroupDeviceName() {
        return R.string.authorize_send_invitation_device;
    }

    public String authToPhoneNumberListToString() {
        return mGrantUserPhoneNumberList.toString().replace("[", "").replace("]", "");
    }

    public String authToPhoneNameListToString() {
        return mGrantUserNameList.toString().replace("[", "").replace("]", "");
    }

    public String getModelName() {
        return "";
    }

    public int getModelId() {
        return 0;
    }

    public int getModelIcon() {
        return R.drawable.all_device_air_icon;
    }

    public boolean canShowRemoveAndArrow() {
        return false;
    }

    public boolean canShowRevokeAndArrow() {
        return false;
    }

    public void setModleName(String name) {
    }

    public void setModelId(int modelId) {
    }

    public int getAuthSendAction() {
        return R.string.authorize_authorize;
    }

    public int getAuthRevokeAction() {
        return R.string.authorize_revoke;
    }

    public int getAuthRemoveAction() {
        return R.string.authorize_remove;
    }

    public boolean revmoveClickAble() {
        return true;
    }

    public boolean revokeClickAble() {
        return true;
    }

    public boolean authorizeClickAble() {
        return true;
    }

    public boolean canShowDeviceStatus() {
        return true;
    }

    public Class getmClass() {
        return mClass;
    }

    public void setmClass(Class mClass) {
        this.mClass = mClass;
    }
}
