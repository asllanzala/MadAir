package com.honeywell.hch.airtouch.plateform.http.model.authorize.model;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.protocol.AuthorizeProtocol;

import java.io.Serializable;

/**
 * Created by Vincent on 1/4/16.
 */
public class AuthGroupModel extends AuthBaseModel implements Serializable, AuthorizeProtocol {
    @SerializedName("groupName")
    private String mGroupName;

    @SerializedName("groupId")
    private int mGroupId;

    public String getmGroupName() {
        return mGroupName;
    }

    public void setmGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public int getmGroupId() {
        return mGroupId;
    }

    public void setmGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    @Override
    public String getModelName() {
        return this.mGroupName;
    }

    @Override
    public int getModelId() {
        return this.mGroupId;
    }

    @Override
    public int getModelIcon() {
        return R.drawable.all_device_group_icon;
    }

    @Override
    public int getAuthSendAction() {
        return R.string.authorize_group_detail;
    }

    @Override
    public int getAuthRevokeAction() {
        return R.string.authorize_group_detail;
    }

    @Override
    public boolean revmoveClickAble() {
        return true;
    }

    @Override
    public boolean revokeClickAble() {
        return false;
    }

    @Override
    public boolean authorizeClickAble() {
        return false;
    }

    @Override
    public boolean canShowDeviceStatus() {
        return false;
    }

    @Override
    public int parseGroupDeviceName() {
        return R.string.authorize_send_invitation_group;
    }
}
