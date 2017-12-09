package com.honeywell.hch.airtouch.ui.enroll.manager.model;

import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Vincent on 17/11/16.
 */
public class EnrollScanEntityTest {

    @Test
    public void testCategory() {
        EnrollScanEntity enrollChoiceModel = EnrollScanEntity.getEntityInstance();
        Assert.assertNotNull(enrollChoiceModel);
        enrollChoiceModel.setSmartEntranch("");
        Assert.assertEquals("", enrollChoiceModel.getSmartEntrance());

        enrollChoiceModel.setmProductUUID("");
        Assert.assertEquals("", enrollChoiceModel.getmProductUUID());

        enrollChoiceModel.setmMacID("");
        Assert.assertEquals("", enrollChoiceModel.getmMacID());

        enrollChoiceModel.setmModel("");
        Assert.assertEquals("", enrollChoiceModel.getmModel());

        enrollChoiceModel.setmDeviceType(1);
        Assert.assertEquals(1, enrollChoiceModel.getmDeviceType());

        enrollChoiceModel.setmEnrollType(new String[2]);
        Assert.assertEquals(2, enrollChoiceModel.getmEnrollType().length);

        enrollChoiceModel.setmCountry("");
        Assert.assertEquals("", enrollChoiceModel.getmCountry());

        enrollChoiceModel.setFromTimeout(true);
        Assert.assertEquals(true, enrollChoiceModel.isFromTimeout());

        enrollChoiceModel.setData("", "", "", null, "", true);
        Assert.assertEquals(true, enrollChoiceModel.isFromTimeout());
        Assert.assertEquals("", enrollChoiceModel.getmProductUUID());
        Assert.assertEquals("", enrollChoiceModel.getmMacID());
        Assert.assertEquals("", enrollChoiceModel.getmModel());
        Assert.assertNull(enrollChoiceModel.getmEnrollType());
        Assert.assertEquals("", enrollChoiceModel.getmCountry());

        enrollChoiceModel.clearData();
        Assert.assertEquals(false, enrollChoiceModel.isFromTimeout());
        Assert.assertEquals("", enrollChoiceModel.getmProductUUID());
        Assert.assertEquals("", enrollChoiceModel.getmMacID());
        Assert.assertEquals("", enrollChoiceModel.getmModel());
        Assert.assertNull(enrollChoiceModel.getmEnrollType());
        Assert.assertEquals("", enrollChoiceModel.getmCountry());

        enrollChoiceModel.setIsRegisteredByThisUser(true);
        Assert.assertEquals(true, enrollChoiceModel.isRegisteredByThisUser());

        enrollChoiceModel.setmDeviceType(1048579);
        Assert.assertEquals(com.honeywell.hch.airtouch.plateform.R.drawable.enroll_air_touch_450, enrollChoiceModel.getDeviceImage());
//        enrollChoiceModel.setmModel("KHN6YM");
//        Assert.assertEquals(2130837763, enrollChoiceModel.getDeviceImage());

//        Assert.assertEquals(2131166118, enrollChoiceModel.getDeviceName());
//        Assert.assertEquals(2130837595, enrollChoiceModel.getEnrollChoiceDeivceImage());
        Assert.assertEquals("AirTouch P", enrollChoiceModel.getDevicePrefixWifiName());
        enrollChoiceModel.setIsApMode(true);
        Assert.assertEquals(true, enrollChoiceModel.isApMode());
    }

}
