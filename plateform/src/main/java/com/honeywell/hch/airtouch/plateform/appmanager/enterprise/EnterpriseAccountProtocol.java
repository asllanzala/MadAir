package com.honeywell.hch.airtouch.plateform.appmanager.enterprise;


import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.protocol.DistributionProtocol;
import com.honeywell.hch.airtouch.plateform.appmanager.role.EnterpriseRole;
import com.honeywell.hch.airtouch.plateform.appmanager.role.Role;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

/**
 * Created by wuyuan on 3/11/16.
 */
public class EnterpriseAccountProtocol implements DistributionProtocol {
    private static EnterpriseRole mEnterpriseRole;

    @Override
    public boolean canEditHome(UserLocationData userLocationData) {
        return userLocationData.isIsLocationOwner();
    }

    @Override
    public boolean canShowChangePasswordSidebar() {
        return false;
    }

    @Override
    public boolean canShowEditHomeSidebar() {
        return false;
    }

    @Override
    public boolean canShowAuthorizationSidebar() {
        return false;
    }

    @Override
    public boolean canEnrollToHome(UserLocationData userLocationData) {
        return userLocationData.isIsLocationOwner();
    }

    @Override
    public Role getRole() {
        if (mEnterpriseRole == null) {
            mEnterpriseRole = new EnterpriseRole();
        }
        return mEnterpriseRole;
    }

    @Override
    public boolean canSetDefaultHome() {
        return false;
    }

    @Override
    public boolean canGoToGroupPage() {
        return false;
    }

    @Override
    public int getMaxHomeLimit() {
        return AppConfig.ENTERPRISE_MAX_HOME_COUNT;
    }

    @Override
    public boolean canSendAuthorization() {
        return false;
    }

    @Override
    public boolean isNeedReceiveRefreshInAllDeviceActivity(UserLocationData userLocationData) {
        return false;
    }

    @Override
    public boolean canShowWaitingDotAnimation() {
        return false;
    }

    @Override
    public int getEnrollLocationName() {
        return R.string.enroll_location;
    }

    @Override
    public int getEnrollLocationNameHint() {
        return R.string.my_location;
    }

    @Override
    public int getMenuPlaceCare() {
        return R.string.places_care_location;
    }

    @Override
    public boolean canOpenReminder() {
        return false;
    }

    @Override
    public int[] getEnrollDeviceType() {
        return new int[]{HPlusConstants.AIRTOUCH_FFAC_TYPE};
    }

    @Override
    public boolean isCanShowTryDemo() {
        return false;
    }

    @Override
    public boolean canShowSelectCity() {
        return false;
    }
}
