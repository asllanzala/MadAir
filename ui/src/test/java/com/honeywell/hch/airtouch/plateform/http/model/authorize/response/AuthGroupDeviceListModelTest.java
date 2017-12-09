package com.honeywell.hch.airtouch.plateform.http.model.authorize.response;

import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthGroupDeviceListModel;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 12/4/16.
 */
public class AuthGroupDeviceListModelTest {

    @Test
    public void getmGroupName() {
        AuthGroupDeviceListModel authGroupDeviceListModel = new AuthGroupDeviceListModel();
        authGroupDeviceListModel.setmGroupName("vincent");
        Assert.assertEquals("vincent", authGroupDeviceListModel.getmGroupName());
    }

    @Test
    public void getmGroupId() {
        AuthGroupDeviceListModel authGroupDeviceListModel = new AuthGroupDeviceListModel();
        authGroupDeviceListModel.setmGroupId(123);
        Assert.assertEquals(123, authGroupDeviceListModel.getmGroupId());
    }

    @Test
    public void getmLocationName() {
        AuthGroupDeviceListModel authGroupDeviceListModel = new AuthGroupDeviceListModel();
        authGroupDeviceListModel.setmLocationName("vincent");
        Assert.assertEquals("vincent", authGroupDeviceListModel.getmLocationName());
    }

    @Test
    public void getmLocationId() {
        AuthGroupDeviceListModel authGroupDeviceListModel = new AuthGroupDeviceListModel();
        authGroupDeviceListModel.setmLocationId(123);
        Assert.assertEquals(123, authGroupDeviceListModel.getmLocationId());
    }

    @Test
    public void ismIsLocatonOwner() {
        AuthGroupDeviceListModel authGroupDeviceListModel = new AuthGroupDeviceListModel();
        authGroupDeviceListModel.setmIsLocatonOwner(true);
        Assert.assertEquals(true, authGroupDeviceListModel.ismIsLocatonOwner());
    }

    @Test
    public void getmAuthDevices() {
        AuthGroupDeviceListModel authGroupDeviceListModel = new AuthGroupDeviceListModel();
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();
        List<AuthDeviceModel> mAuthDevices = new ArrayList<>();
        mAuthDevices.add(authDeviceModel);
        authGroupDeviceListModel.setmAuthDevices(mAuthDevices);
        Assert.assertEquals(1, authGroupDeviceListModel.getmAuthDevices().size());
    }

}
