package com.honeywell.hch.airtouch.plateform.http.model.authorize.response;


import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GrantAuthDevice;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Qian Jin on 3/9/16.
 */
@RunWith(RobolectricTestRunner.class)

public class GrantAuthDeviceTest {
    @Test
    public void testGetId() {
        GrantAuthDevice device = new GrantAuthDevice();

        device.setId(0);
        Assert.assertEquals(0, device.getId());

        device.setId(14);
        Assert.assertEquals(14, device.getId());

        device.setId(-1);
        Assert.assertEquals(-1, device.getId());

    }

    @Test
    public void testSetId() {
        GrantAuthDevice device = new GrantAuthDevice();

        device.setId(0);
        Assert.assertEquals(0, device.getId());

        device.setId(14);
        Assert.assertEquals(14, device.getId());

        device.setId(-1);
        Assert.assertEquals(-1, device.getId());

    }

    @Test
    public void testGetTelephone() {
        GrantAuthDevice device = new GrantAuthDevice();

        device.setTelephone("");
        Assert.assertEquals("", device.getTelephone());

        device.setTelephone("Jin");
        Assert.assertEquals("Jin", device.getTelephone());

        device.setTelephone("@#$%s安康");
        Assert.assertEquals("@#$%s安康", device.getTelephone());
    }

    @Test
    public void testSetTelephone() {
        GrantAuthDevice device = new GrantAuthDevice();

        device.setTelephone("");
        Assert.assertEquals("", device.getTelephone());

        device.setTelephone("Jin");
        Assert.assertEquals("Jin", device.getTelephone());

        device.setTelephone("@#$%s安康");
        Assert.assertEquals("@#$%s安康", device.getTelephone());
    }

    @Test
    public void testGetMessage() {
        GrantAuthDevice device = new GrantAuthDevice();

        device.setMessage("");
        Assert.assertEquals("", device.getMessage());

        device.setMessage("Jin");
        Assert.assertEquals("Jin", device.getMessage());

        device.setMessage("@#$%s安康");
        Assert.assertEquals("@#$%s安康", device.getMessage());
    }

    @Test
    public void testSetMessage() {
        GrantAuthDevice device = new GrantAuthDevice();

        device.setMessage("");
        Assert.assertEquals("", device.getMessage());

        device.setMessage("Jin");
        Assert.assertEquals("Jin", device.getMessage());

        device.setMessage("@#$%s安康");
        Assert.assertEquals("@#$%s安康", device.getMessage());
    }

    @Test
    public void testGetCode() {
        GrantAuthDevice device = new GrantAuthDevice();

        device.setCode(0);
        Assert.assertEquals(0, device.getCode());

        device.setCode(14);
        Assert.assertEquals(14, device.getCode());

        device.setCode(-1);
        Assert.assertEquals(-1, device.getCode());
    }

    @Test
    public void testSetCode() {
        GrantAuthDevice device = new GrantAuthDevice();

        device.setCode(0);
        Assert.assertEquals(0, device.getCode());

        device.setCode(14);
        Assert.assertEquals(14, device.getCode());

        device.setCode(-1);
        Assert.assertEquals(-1, device.getCode());
    }
}
