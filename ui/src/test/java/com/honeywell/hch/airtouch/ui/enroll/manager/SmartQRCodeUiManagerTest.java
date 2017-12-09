package com.honeywell.hch.airtouch.ui.enroll.manager;

import android.app.Application;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Vincent on 18/4/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SmartQRCodeUiManagerTest {
    private final String TAG = "SmartQRCodeUiManagerTest";
    private SmartLinkUiManager mSmartQRCodeUiManager;
    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);

        mSmartQRCodeUiManager = new SmartLinkUiManager();
    }

    @Test
    public void parseURLTest() {
        EnrollScanEntity enrollScanEntity = new EnrollScanEntity();
        String recode = "http://eccap.honeywell.cn/eacmobileinstall.html?model=A-1";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "model: " + enrollScanEntity.getmModel());
        recode = "http://smart.jd.com/download?g=XXXXXXXXXXXXXXX";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.INVALID, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        recode = "http://eccap.honeywell.cn/eacmobileinstall.html?model=KJ300F-PAC1101W";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        recode = "http://eccap.honeywell.cn/eacmobileinstall.html?model=KJ300F-PAC1101G";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        recode = "http://hch.honeywell.com.cn/landingpage/air-touch-install.html";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        Assert.assertEquals(HPlusConstants.AIR_TOUCH_S_SILVER, enrollScanEntity.getmModel());
        recode = "http://hch.honeywell.com.cn/landingpage/air-touch-install.html?KJ300F-PAC2101S/T1/T2";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        recode = "http://hch.honeywell.com.cn/landingpage/air-touch-install.html?KJ700G-PAC2127W";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        recode = "PAC45M1022W";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        Assert.assertEquals(HPlusConstants.AIR_TOUCH_P_MODEL, enrollScanEntity.getmModel());
        recode = "http://hch.honeywell.com.cn/landingpage/air-touch-install.html?model=KJ450F-PAC2022S&h=XXXXXXXXXXX";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.INVALID, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        recode = "http://hch.honeywell.com.cn/landingpage/air-touch-install.html?model=SKJ900G-PAC3454W&h=XXXXXXXXXXX";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.INVALID, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
        http:
//hch.honeywell.com.cn/landingpage/air-touch-install.html?model=HAC35M2101S&country=India
        recode = "http://hch.honeywell.com.cn/landingpage/air-touch-install.html?model=HAC35M2101S&country=India";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.parseURL(recode, enrollScanEntity));
    }

    @Test
    public void splitJDUrlTest() {
        EnrollScanEntity enrollScanEntity = new EnrollScanEntity();
        String recode = "http://smart.jd.com/download?g=XXXXXXXXXXXXXXX";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.INVALID, mSmartQRCodeUiManager.splitJDUrl(recode, enrollScanEntity));
        recode = "http://smart.jd.com/download?h=XXXXXXXXXXXXXXX";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.FAIL, mSmartQRCodeUiManager.splitJDUrl(recode, enrollScanEntity));
        recode = "http://hch.honeywell.com.cn/landingpage/air-touch-install.html?model=KJ450F-PAC2022S&h=XXXXXXXXXXX";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.FAIL, mSmartQRCodeUiManager.splitJDUrl(recode, enrollScanEntity));
    }

    @Test
    public void splitBaseUrlTest() {
        EnrollScanEntity enrollScanEntity = new EnrollScanEntity();
        String recode = "http://smart.jd.com/download?g=XXXXXXXXXXXXXXX";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.splitBaseUrl(recode, enrollScanEntity));
        recode = "http://hch.honeywell.com.cn/landingpage/air-touch-install.html?model=KJ450F-PAC2022S&h=XXXXXXXXXXX";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.INVALID, mSmartQRCodeUiManager.splitBaseUrl(recode, enrollScanEntity));
        recode = "123";
        Assert.assertEquals(SmartLinkUiManager.ScanDeviceType.HPLUS_SUCCESS, mSmartQRCodeUiManager.splitBaseUrl(recode, enrollScanEntity));
    }

    @Test
    public void parseCheckTypeResponseTest() {
        EnrollScanEntity enrollScanEntity = new EnrollScanEntity();
        String result = "[1, 0]";
        mSmartQRCodeUiManager.parseCheckTypeResponse(result, enrollScanEntity);
        Assert.assertNotNull(enrollScanEntity.getmEnrollType());
        result = "[ 0]";
        mSmartQRCodeUiManager.parseCheckTypeResponse(result, enrollScanEntity);
        Assert.assertNotNull(enrollScanEntity.getmEnrollType());
        result = "[1]";
        mSmartQRCodeUiManager.parseCheckTypeResponse(result, enrollScanEntity);
        Assert.assertNotNull(enrollScanEntity.getmEnrollType());
        result = "[-1]";
        mSmartQRCodeUiManager.parseCheckTypeResponse(result, enrollScanEntity);
        Assert.assertNotNull(enrollScanEntity.getmEnrollType());
        result = "[null]";
        mSmartQRCodeUiManager.parseCheckTypeResponse(result, enrollScanEntity);
        Assert.assertNotNull(enrollScanEntity.getmEnrollType());
    }

}
