package com.honeywell.hch.airtouch.plateform.http.model.group;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Qian Jin on 3/23/16.
 */
public class GroupDataResponseTest {
    @Test
    public void testGetGroupId() {
        GroupDataResponse response = new GroupDataResponse();

        response.setGroupId(0);
        Assert.assertEquals(0, response.getGroupId());

        response.setGroupId(14);
        Assert.assertEquals(14, response.getGroupId());

        response.setGroupId(-1);
        Assert.assertEquals(-1, response.getGroupId());
    }

    @Test
    public void testGetLocationId() {
        GroupDataResponse response = new GroupDataResponse();

        response.setLocationId(0);
        Assert.assertEquals(0, response.getLocationId());

        response.setLocationId(14);
        Assert.assertEquals(14, response.getLocationId());

        response.setLocationId(-1);
        Assert.assertEquals(-1, response.getLocationId());
    }

    @Test
    public void testGetGroupName() {
        GroupDataResponse response = new GroupDataResponse();

        response.setGroupName("");
        Assert.assertEquals("", response.getGroupName());

        response.setGroupName("Jin");
        Assert.assertEquals("Jin", response.getGroupName());

        response.setGroupName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getGroupName());
    }

}
