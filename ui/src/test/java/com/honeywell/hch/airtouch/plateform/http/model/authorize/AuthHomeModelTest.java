package com.honeywell.hch.airtouch.plateform.http.model.authorize;


import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthHomeModel;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Qian Jin on 3/1/16.
 */
@RunWith(RobolectricTestRunner.class)
public class AuthHomeModelTest {
    @Test
    public void testGetLocationName() {
        AuthHomeModel authHomeModel = new AuthHomeModel();

        authHomeModel.setLocationName("");
        Assert.assertEquals("", authHomeModel.getLocationName());

        authHomeModel.setLocationName("Jin");
        Assert.assertEquals("Jin", authHomeModel.getLocationName());

        authHomeModel.setLocationName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authHomeModel.getLocationName());
    }

    @Test
    public void testSetLocationName() {
        AuthHomeModel authHomeModel = new AuthHomeModel();

        authHomeModel.setLocationName("");
        Assert.assertEquals("", authHomeModel.getLocationName());

        authHomeModel.setLocationName("Jin");
        Assert.assertEquals("Jin", authHomeModel.getLocationName());

        authHomeModel.setLocationName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authHomeModel.getLocationName());
    }

    @Test
    public void testGetLocationNameId() {
        AuthHomeModel authHomeModel = new AuthHomeModel();

        authHomeModel.setLocationNameId(0);
        Assert.assertEquals(0, authHomeModel.getLocationNameId());

        authHomeModel.setLocationNameId(14);
        Assert.assertEquals(14, authHomeModel.getLocationNameId());

        authHomeModel.setLocationNameId(-1);
        Assert.assertEquals(-1, authHomeModel.getLocationNameId());
    }

    @Test
    public void testSetLocationNameId() {
        AuthHomeModel authHomeModel = new AuthHomeModel();

        authHomeModel.setLocationNameId(0);
        Assert.assertEquals(0, authHomeModel.getLocationNameId());

        authHomeModel.setLocationNameId(14);
        Assert.assertEquals(14, authHomeModel.getLocationNameId());

        authHomeModel.setLocationNameId(-1);
        Assert.assertEquals(-1, authHomeModel.getLocationNameId());
    }

    @Test
    public void testIsLocationOwner() {
        AuthHomeModel authHomeModel = new AuthHomeModel();

        authHomeModel.setIsLocationOwner(true);
        Assert.assertEquals(true, authHomeModel.isLocationOwner());

        authHomeModel.setIsLocationOwner(false);
        Assert.assertEquals(false, authHomeModel.isLocationOwner());
    }

    @Test
    public void testSetIsLocationOwner() {
        AuthHomeModel authHomeModel = new AuthHomeModel();

        authHomeModel.setIsLocationOwner(true);
        Assert.assertEquals(true, authHomeModel.isLocationOwner());

        authHomeModel.setIsLocationOwner(false);
        Assert.assertEquals(false, authHomeModel.isLocationOwner());
    }

}
