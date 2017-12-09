package com.honeywell.hch.airtouch.plateform.appmanager.personal;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.protocol.DistributionProtocol;
import com.honeywell.hch.airtouch.plateform.appmanager.role.PersonalRole;
import com.honeywell.hch.airtouch.plateform.appmanager.role.Role;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

/**
 * Created by wuyuan on 3/11/16.
 */
public class PersonalAccountProtocol implements DistributionProtocol {
    private static PersonalRole mPersonalRole;

    @Override
    public boolean canEnrollToHome(UserLocationData userLocationData) {
        return userLocationData.isIsLocationOwner();
    }

    @Override
    public Role getRole() {
        if (mPersonalRole == null) {
            mPersonalRole = new PersonalRole();
        }
        return mPersonalRole;
    }

    @Override
    public boolean canEditHome(UserLocationData userLocationData) {
        return userLocationData.isIsLocationOwner();
    }

    @Override
    public boolean canShowChangePasswordSidebar() {
        return true;
    }

    @Override
    public boolean canShowEditHomeSidebar() {
        return true;
    }

    @Override
    public boolean canShowAuthorizationSidebar() {
        return true;
    }

    @Override
    public boolean canSetDefaultHome() {
        return true;
    }

    @Override
    public boolean canGoToGroupPage() {
        return true;
    }

    @Override
    public int getMaxHomeLimit() {
        return AppConfig.PERSONAL_MAX_HOME_COUNT;
    }


    @Override
    public boolean canSendAuthorization() {
        return true;
    }

    @Override
    public boolean isNeedReceiveRefreshInAllDeviceActivity(UserLocationData userLocationData) {
        if (UserAllDataContainer.shareInstance().getAllDeviceNumber() > AppConfig.DEVICE_NUMBER_THRESHOLD) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canShowWaitingDotAnimation() {
        if (UserAllDataContainer.shareInstance().getAllDeviceNumber() > AppConfig.DEVICE_NUMBER_THRESHOLD) {
            return false;
        }
        return true;
    }

    @Override
    public int getEnrollLocationName() {
        return R.string.enroll_home;
    }

    @Override
    public int getEnrollLocationNameHint() {
        return R.string.my_home;
    }

    @Override
    public int getMenuPlaceCare() {
        return R.string.places_care_home;
    }

    @Override
    public int[] getEnrollDeviceType() {
        return new int[]{HPlusConstants.AIRTOUCH_450_TYPE, HPlusConstants.AIRTOUCH_X_TYPE, HPlusConstants.AIRTOUCH_S_TYPE};
    }

    @Override
    public boolean canOpenReminder() {
        return true;
    }

    @Override
    public boolean isCanShowTryDemo() {
        return true;
    }

    @Override
    public boolean canShowSelectCity() {
        return true;
    }
}