package com.honeywell.hch.airtouch.ui.control.manager.model;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.ui.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Vincent on 14/11/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DeviceModleTest {
    protected Application application = RuntimeEnvironment.application;

    @Before
    public void setUp() throws Exception {
        AppManager.getInstance().setHPlusApplication(application);
    }

    @Test
    public void testDeviceModel() {
//        AppManager.getInstance().setHPlusApplication(application);
        DeviceMode deviceMode = new DeviceMode(1, 60, 70, 80, 90);
        Assert.assertEquals(1, deviceMode.getModeType());
        Assert.assertEquals(70, deviceMode.getEndAngle());
        Assert.assertEquals(60, deviceMode.getStartAngle());
        Assert.assertEquals(10, deviceMode.getSweepAngle());
        Assert.assertEquals(80, deviceMode.getColorNormal());
        Assert.assertEquals(90, deviceMode.getColorClicked());
        deviceMode = new DeviceMode(123, 70, 50, 80, 90);
        Assert.assertEquals(340, deviceMode.getSweepAngle());
        Assert.assertEquals(R.drawable.undefied_small, deviceMode.getAllDeviceModeImg(-1));
        Assert.assertEquals(R.drawable.away_mode_small, deviceMode.getAllDeviceModeImg(0));
        Assert.assertEquals(R.drawable.home_mode_small, deviceMode.getAllDeviceModeImg(1));
        Assert.assertEquals(R.drawable.sleep_mode_small, deviceMode.getAllDeviceModeImg(2));
        Assert.assertEquals(R.drawable.awake_mode_small, deviceMode.getAllDeviceModeImg(3));

    }
}