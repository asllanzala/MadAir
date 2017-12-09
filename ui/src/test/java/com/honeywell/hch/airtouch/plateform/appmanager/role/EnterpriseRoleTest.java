package com.honeywell.hch.airtouch.plateform.appmanager.role;


import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthTo;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)

/**
 * Created by Qian Jin on 3/15/16.
 */
public class EnterpriseRoleTest {

    @Test
    public void testCanShowDeviceDetail() {
        EnterpriseRole role = new EnterpriseRole();

        Assert.assertEquals(false, role.canShowDeviceDetail());
    }

    @Test
    public void testCanShowFilter() {
        EnterpriseRole role = new EnterpriseRole();
        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.setPermission(0);
        Assert.assertEquals(false, role.canShowFilter(deviceInfo));

        deviceInfo.setPermission(AuthTo.OBSERVER);
        Assert.assertEquals(false, role.canShowFilter(deviceInfo));

        deviceInfo.setPermission(AuthTo.CONTROLLER);
        Assert.assertEquals(false, role.canShowFilter(deviceInfo));

        deviceInfo.setPermission(AuthTo.MASTER);
        Assert.assertEquals(false, role.canShowFilter(deviceInfo));

        deviceInfo.setPermission(AuthTo.OWNER);
        Assert.assertEquals(false, role.canShowFilter(deviceInfo));
    }

    @Test
    public void testCanShowPurchase() {
        EnterpriseRole role = new EnterpriseRole();
        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.setPermission(0);
        Assert.assertEquals(false, role.canShowPurchase(deviceInfo));

        deviceInfo.setPermission(AuthTo.OBSERVER);
        Assert.assertEquals(false, role.canShowPurchase(deviceInfo));

        deviceInfo.setPermission(AuthTo.CONTROLLER);
        Assert.assertEquals(false, role.canShowPurchase(deviceInfo));

        deviceInfo.setPermission(AuthTo.MASTER);
        Assert.assertEquals(false, role.canShowPurchase(deviceInfo));

        deviceInfo.setPermission(AuthTo.OWNER);
        Assert.assertEquals(false, role.canShowPurchase(deviceInfo));
    }

    @Test
    public void testCanControlDevice() {
        EnterpriseRole role = new EnterpriseRole();
        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.setPermission(0);
        Assert.assertEquals(false, role.canControlDevice(deviceInfo));

        deviceInfo.setPermission(AuthTo.OBSERVER);
        Assert.assertEquals(false, role.canControlDevice(deviceInfo));

        deviceInfo.setPermission(AuthTo.CONTROLLER);
        Assert.assertEquals(true, role.canControlDevice(deviceInfo));

        deviceInfo.setPermission(AuthTo.MASTER);
        Assert.assertEquals(true, role.canControlDevice(deviceInfo));

        deviceInfo.setPermission(AuthTo.OWNER);
        Assert.assertEquals(true, role.canControlDevice(deviceInfo));
    }

    @Test
    public void testCanDeleteDevice() {
        EnterpriseRole role = new EnterpriseRole();
        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.setPermission(0);
        Assert.assertEquals(false, role.canDeleteDevice(deviceInfo));

        deviceInfo.setPermission(AuthTo.OBSERVER);
        Assert.assertEquals(false, role.canDeleteDevice(deviceInfo));

        deviceInfo.setPermission(AuthTo.CONTROLLER);
        Assert.assertEquals(false, role.canDeleteDevice(deviceInfo));

        deviceInfo.setPermission(AuthTo.MASTER);
        Assert.assertEquals(false, role.canDeleteDevice(deviceInfo));

        deviceInfo.setPermission(AuthTo.OWNER);
        Assert.assertEquals(true, role.canDeleteDevice(deviceInfo));
    }

    @Test
    public void testCanGroup() {
        EnterpriseRole role = new EnterpriseRole();
        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.setPermission(0);
        Assert.assertEquals(false, role.canGroup(deviceInfo));

        deviceInfo.setPermission(AuthTo.OBSERVER);
        Assert.assertEquals(false, role.canGroup(deviceInfo));

        deviceInfo.setPermission(AuthTo.CONTROLLER);
        Assert.assertEquals(false, role.canGroup(deviceInfo));

        deviceInfo.setPermission(AuthTo.MASTER);
        Assert.assertEquals(false, role.canGroup(deviceInfo));

        deviceInfo.setPermission(AuthTo.OWNER);
        Assert.assertEquals(false, role.canGroup(deviceInfo));
    }

}
