package com.honeywell.hch.airtouch.plateform.http.model.group;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Qian Jin on 3/23/16.
 */
public class DevicesForGroupResponseTest {
    @Test
    public void testGetDeviceName() {
        DevicesForGroupResponse response = new DevicesForGroupResponse();

        response.setDeviceName("");
        Assert.assertEquals("", response.getDeviceName());

        response.setDeviceName("Jin");
        Assert.assertEquals("Jin", response.getDeviceName());

        response.setDeviceName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getDeviceName());
    }

    @Test
    public void testGetDeviceId() {
        DevicesForGroupResponse response = new DevicesForGroupResponse();

        response.setDeviceId(0);
        Assert.assertEquals(0, response.getDeviceId());

        response.setDeviceId(14);
        Assert.assertEquals(14, response.getDeviceId());

        response.setDeviceId(-1);
        Assert.assertEquals(-1, response.getDeviceId());
    }

    @Test
    public void testGetDeviceType() {
        DevicesForGroupResponse response = new DevicesForGroupResponse();

        response.setDeviceType(0.0);
        Assert.assertEquals(0.0, response.getDeviceType());

        response.setDeviceType(14.0);
        Assert.assertEquals(14.0, response.getDeviceType());

        response.setDeviceType(-1.0);
        Assert.assertEquals(-1.0, response.getDeviceType());
    }

    @Test
    public void testGetIsAlive() {
        DevicesForGroupResponse response = new DevicesForGroupResponse();

        response.setIsAlive(true);
        Assert.assertEquals(true, response.getIsAlive());

        response.setIsAlive(false);
        Assert.assertEquals(false, response.getIsAlive());

    }

    @Test
    public void testGetIsUpgrading() {
        DevicesForGroupResponse response = new DevicesForGroupResponse();

        response.setIsUpgrading(true);
        Assert.assertEquals(true, response.getIsUpgrading());

        response.setIsUpgrading(false);
        Assert.assertEquals(false, response.getIsUpgrading());

    }

    @Test
    public void testGetMacId() {
        DevicesForGroupResponse response = new DevicesForGroupResponse();

        response.setMacId("");
        Assert.assertEquals("", response.getMacId());

        response.setMacId("Jin");
        Assert.assertEquals("Jin", response.getMacId());

        response.setMacId("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getMacId());
    }

    @Test
    public void testGetIsMasterDevice() {
        DevicesForGroupResponse response = new DevicesForGroupResponse();

        response.setIsMasterDevice(0);
        Assert.assertEquals(0, response.getIsMasterDevice());

        response.setIsMasterDevice(14);
        Assert.assertEquals(14, response.getIsMasterDevice());

        response.setIsMasterDevice(-1);
        Assert.assertEquals(-1, response.getIsMasterDevice());
    }


}
