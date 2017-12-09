package com.honeywell.hch.airtouch.plateform.http.model.authorize;

import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthGroupModel;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Vincent on 15/4/16.
 */
public class AuthGroupModelTest {

    @Test
    public void testGetGroupId() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setmGroupId(0);
        Assert.assertEquals(0, authDeviceModel.getmGroupId());

        authDeviceModel.setmGroupId(14);
        Assert.assertEquals(14, authDeviceModel.getmGroupId());

        authDeviceModel.setmGroupId(-1);
        Assert.assertEquals(-1, authDeviceModel.getmGroupId());
    }

    @Test
    public void testSetGroupId() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setmGroupId(0);
        Assert.assertEquals(0, authDeviceModel.getmGroupId());

        authDeviceModel.setmGroupId(14);
        Assert.assertEquals(14, authDeviceModel.getmGroupId());

        authDeviceModel.setmGroupId(-1);
        Assert.assertEquals(-1, authDeviceModel.getmGroupId());
    }

    @Test
    public void testGetOwnerName() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setOwnerName("");
        Assert.assertEquals("", authDeviceModel.getOwnerName());

        authDeviceModel.setOwnerName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getOwnerName());

        authDeviceModel.setOwnerName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getOwnerName());
    }

    @Test
    public void testSetOwnerName() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setOwnerName("");
        Assert.assertEquals("", authDeviceModel.getOwnerName());

        authDeviceModel.setOwnerName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getOwnerName());

        authDeviceModel.setOwnerName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getOwnerName());
    }

    @Test
    public void testGetOwnerId() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setOwnerId(0);
        Assert.assertEquals(0, authDeviceModel.getOwnerId());

        authDeviceModel.setOwnerId(14);
        Assert.assertEquals(14, authDeviceModel.getOwnerId());

        authDeviceModel.setOwnerId(-1);
        Assert.assertEquals(-1, authDeviceModel.getOwnerId());
    }

    @Test
    public void testSetOwnerId() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setOwnerId(0);
        Assert.assertEquals(0, authDeviceModel.getOwnerId());

        authDeviceModel.setOwnerId(14);
        Assert.assertEquals(14, authDeviceModel.getOwnerId());

        authDeviceModel.setOwnerId(-1);
        Assert.assertEquals(-1, authDeviceModel.getOwnerId());
    }

    @Test
    public void testGetDeviceName() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setmGroupName("");
        Assert.assertEquals("", authDeviceModel.getmGroupName());

        authDeviceModel.setmGroupName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getmGroupName());

        authDeviceModel.setmGroupName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getmGroupName());
    }

    @Test
    public void testSetDeviceName() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setmGroupName("");
        Assert.assertEquals("", authDeviceModel.getmGroupName());

        authDeviceModel.setmGroupName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getmGroupName());

        authDeviceModel.setmGroupName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getmGroupName());
    }

    @Test
    public void testGetmLocationName() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setLocationName("");
        Assert.assertEquals("", authDeviceModel.getmLocationName());

        authDeviceModel.setLocationName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getmLocationName());

        authDeviceModel.setLocationName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getmLocationName());
    }

    @Test
    public void testSetLocationName() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setLocationName("");
        Assert.assertEquals("", authDeviceModel.getmLocationName());

        authDeviceModel.setLocationName("Jin");
        Assert.assertEquals("Jin", authDeviceModel.getmLocationName());

        authDeviceModel.setLocationName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authDeviceModel.getmLocationName());
    }

    @Test
    public void testGetParentPosition() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setParentPosition(0);
        Assert.assertEquals(0, authDeviceModel.getParentPosition());

        authDeviceModel.setParentPosition(14);
        Assert.assertEquals(14, authDeviceModel.getParentPosition());

        authDeviceModel.setParentPosition(-1);
        Assert.assertEquals(-1, authDeviceModel.getParentPosition());
    }

    @Test
    public void testSetParentPosition() {
        AuthGroupModel authDeviceModel = new AuthGroupModel();

        authDeviceModel.setParentPosition(0);
        Assert.assertEquals(0, authDeviceModel.getParentPosition());

        authDeviceModel.setParentPosition(14);
        Assert.assertEquals(14, authDeviceModel.getParentPosition());

        authDeviceModel.setParentPosition(-1);
        Assert.assertEquals(-1, authDeviceModel.getParentPosition());
    }

    @Test
    public void testGetModelName() {
        AuthGroupModel authGroupModel = new AuthGroupModel();
        authGroupModel.setmGroupName("vincent");
        Assert.assertEquals("vincent", authGroupModel.getModelName());
    }

    @Test
    public void testGetModelId() {
        AuthGroupModel authGroupModel = new AuthGroupModel();
        authGroupModel.setmGroupId(123);
        Assert.assertEquals(123, authGroupModel.getModelId());
    }
}
