package com.honeywell.hch.airtouch.library.util;

import android.app.Application;
import android.net.wifi.WifiConfiguration;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by wuyuan on 3/21/16.
 */
@RunWith(RobolectricTestRunner.class)
public class WifiUtilTest {

    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);

    }

   @Test
    public void testGetWifiCipher(){
       String capacity = "WEP";
       Assert.assertEquals(WifiUtil.WifiCipherType.WIFICIPHER_WEP,WifiUtil.getWifiCipher(capacity));

       capacity = "WPA";
       Assert.assertEquals(WifiUtil.WifiCipherType.WIFICIPHER_WPA,WifiUtil.getWifiCipher(capacity));


       capacity = "/WEP2/DKLS";
       Assert.assertEquals(WifiUtil.WifiCipherType.WIFICIPHER_WEP,WifiUtil.getWifiCipher(capacity));

       capacity = "";
       Assert.assertEquals(WifiUtil.WifiCipherType.WIFICIPHER_INVALID, WifiUtil.getWifiCipher(capacity));

       capacity = "HELLO/WPS/";
       Assert.assertEquals(WifiUtil.WifiCipherType.WIFICIPHER_WPA, WifiUtil.getWifiCipher(capacity));
   }

    @Test
    public void testGetEncryptString(){
        String capacity = "WEP";
        Assert.assertEquals("WEP",WifiUtil.getEncryptString(capacity));

        capacity = "W/E/P";
        Assert.assertEquals("OPEN",WifiUtil.getEncryptString(capacity));

        capacity = "WPA2";
        Assert.assertEquals("WPA/WPA2",WifiUtil.getEncryptString(capacity));

        capacity = "WPS";
        Assert.assertEquals("/WPS",WifiUtil.getEncryptString(capacity));

        capacity = "WPA2/WPA/WPS";
        Assert.assertEquals("WPA/WPA2/WPS",WifiUtil.getEncryptString(capacity));

        capacity = "WPA2/WPEA/WPS";
        Assert.assertEquals("WPA/WPA2/WPS",WifiUtil.getEncryptString(capacity));

        capacity = "";
        Assert.assertEquals("unknow",WifiUtil.getEncryptString(capacity));
    }


    @Test
    public void testCreateWifiConfig(){
        String SSID = "MY-WIFI";
        String Password = "123456";
        WifiUtil.WifiCipherType Type = WifiUtil.WifiCipherType.WIFICIPHER_NOPASS;

        WifiConfiguration wifiConfiguration = WifiUtil.createWifiConfig(SSID,Password,Type);
        Assert.assertEquals("\"" + SSID + "\"",wifiConfiguration.SSID);
        Assert.assertEquals(0,wifiConfiguration.wepTxKeyIndex);
        Assert.assertEquals("\""+"\"",wifiConfiguration.wepKeys[0]);

    }
}
