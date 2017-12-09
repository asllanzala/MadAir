package com.honeywell.hch.airtouch.plateform.appmanager.protocol;


import com.honeywell.hch.airtouch.plateform.appmanager.role.Role;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

/**
 * Created by wuyuan on 3/10/16.
 * 中国账号和印度账号相同的特性，个人账号与企业账号不同的特性
 */
public interface DistributionProtocol {

    boolean canShowChangePasswordSidebar();

    boolean canShowEditHomeSidebar();

    boolean canShowAuthorizationSidebar();

    boolean canEditHome(UserLocationData userLocationData);

    boolean canEnrollToHome(UserLocationData userLocationData);

    boolean canSetDefaultHome();

    boolean canGoToGroupPage();

    boolean canOpenReminder();

    Role getRole();

    int getMaxHomeLimit();

    boolean canSendAuthorization();

    /**
     * 在all device界面是否需要接受定时刷新的消息
     *
     * @return
     */
    boolean isNeedReceiveRefreshInAllDeviceActivity(UserLocationData userLocationData);

    /**
     * 是否需要点点点动画，如果是企业账号，就不需要。如果是个人账号，设备少于20台的时候，需要动画。
     *
     * @return
     */
    boolean canShowWaitingDotAnimation();

    int getEnrollLocationName();

    int getEnrollLocationNameHint();

    int getMenuPlaceCare();

    int[] getEnrollDeviceType();

    boolean isCanShowTryDemo();

    boolean canShowSelectCity();
}
