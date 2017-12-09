package com.honeywell.hch.airtouch.plateform.http.model.authorize;


import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Qian Jin on 3/1/16.
 */
@RunWith(RobolectricTestRunner.class)
public class AuthDeviceModelTest {
    @Test
    public void testGetDeviceId() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setmDeviceId(0);
        Assert.assertEquals(0, authDeviceModel.getmDeviceId());

        authDeviceModel.setmDeviceId(14);
        Assert.assertEquals(14, authDeviceModel.getmDeviceId());

        authDeviceModel.setmDeviceId(-1);
        Assert.assertEquals(-1, authDeviceModel.getmDeviceId());
    }

    @Test
    public void testSetDeviceId() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setmDeviceId(0);
        Assert.assertEquals(0, authDeviceModel.getmDeviceId());

        authDeviceModel.setmDeviceId(14);
        Assert.assertEquals(14, authDeviceModel.getmDeviceId());

        authDeviceModel.setmDeviceId(-1);
        Assert.assertEquals(-1, authDeviceModel.getmDeviceId());
    }

    @Test
    public void testGetDeviceType() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setmDeviceType(0);
        Assert.assertEquals(0, authDeviceModel.getmDeviceType());

        authDeviceModel.setmDeviceType(14);
        Assert.assertEquals(14, authDeviceModel.getmDeviceType());

        authDeviceModel.setmDeviceType(-1);
        Assert.assertEquals(-1, authDeviceModel.getmDeviceType());
    }

    @Test
    public void testSetDeviceType() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setmDeviceType(0);
        Assert.assertEquals(0, authDeviceModel.getmDeviceType());

        authDeviceModel.setmDeviceType(14);
        Assert.assertEquals(14, authDeviceModel.getmDeviceType());

        authDeviceModel.setmDeviceType(-1);
        Assert.assertEquals(-1, authDeviceModel.getmDeviceType());
    }

    @Test
    public void testGetOwnerName() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setOwnerName("");
        Assert.assertEquals("", authDeviceModel.getOwnerName());

        authDeviceModel.setOwnerName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getOwnerName());

        authDeviceModel.setOwnerName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getOwnerName());
    }

    @Test
    public void testSetOwnerName() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setOwnerName("");
        Assert.assertEquals("", authDeviceModel.getOwnerName());

        authDeviceModel.setOwnerName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getOwnerName());

        authDeviceModel.setOwnerName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getOwnerName());
    }

    @Test
    public void testGetOwnerId() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setOwnerId(0);
        Assert.assertEquals(0, authDeviceModel.getOwnerId());

        authDeviceModel.setOwnerId(14);
        Assert.assertEquals(14, authDeviceModel.getOwnerId());

        authDeviceModel.setOwnerId(-1);
        Assert.assertEquals(-1, authDeviceModel.getOwnerId());
    }

    @Test
    public void testSetOwnerId() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setOwnerId(0);
        Assert.assertEquals(0, authDeviceModel.getOwnerId());

        authDeviceModel.setOwnerId(14);
        Assert.assertEquals(14, authDeviceModel.getOwnerId());

        authDeviceModel.setOwnerId(-1);
        Assert.assertEquals(-1, authDeviceModel.getOwnerId());
    }

    @Test
    public void testGetDeviceName() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setmDeviceName("");
        Assert.assertEquals("", authDeviceModel.getmDeviceName());

        authDeviceModel.setmDeviceName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getmDeviceName());

        authDeviceModel.setmDeviceName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getmDeviceName());
    }

    @Test
    public void testSetDeviceName() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setmDeviceName("");
        Assert.assertEquals("", authDeviceModel.getmDeviceName());

        authDeviceModel.setmDeviceName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getmDeviceName());

        authDeviceModel.setmDeviceName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getmDeviceName());
    }

    @Test
    public void testGetmLocationName() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setLocationName("");
        Assert.assertEquals("", authDeviceModel.getmLocationName());

        authDeviceModel.setLocationName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getmLocationName());

        authDeviceModel.setLocationName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getmLocationName());
    }

    @Test
    public void testSetLocationName() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setLocationName("");
        Assert.assertEquals("", authDeviceModel.getmLocationName());

        authDeviceModel.setLocationName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getmLocationName());

        authDeviceModel.setLocationName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getmLocationName());
    }

    @Test
    public void testGetParentPosition() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setParentPosition(0);
        Assert.assertEquals(0, authDeviceModel.getParentPosition());

        authDeviceModel.setParentPosition(14);
        Assert.assertEquals(14, authDeviceModel.getParentPosition());

        authDeviceModel.setParentPosition(-1);
        Assert.assertEquals(-1, authDeviceModel.getParentPosition());
    }

    @Test
    public void testSetParentPosition() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();

        authDeviceModel.setParentPosition(0);
        Assert.assertEquals(0, authDeviceModel.getParentPosition());

        authDeviceModel.setParentPosition(14);
        Assert.assertEquals(14, authDeviceModel.getParentPosition());

        authDeviceModel.setParentPosition(-1);
        Assert.assertEquals(-1, authDeviceModel.getParentPosition());
    }

    @Test
    public void testGetModelName() {
        AuthDeviceModel authGroupModel = new AuthDeviceModel();
        authGroupModel.setmDeviceName("vincent");
        Assert.assertEquals(null, authGroupModel.getModelName());
    }

    @Test
    public void testGetModelId() {
        AuthDeviceModel authGroupModel = new AuthDeviceModel();
        authGroupModel.setmDeviceId(123);
        Assert.assertEquals(123, authGroupModel.getModelId());
    }
}
