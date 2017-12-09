package com.honeywell.hch.airtouch.plateform.http.model.group;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Qian Jin on 3/23/16.
 */
public class GroupResponseTest {
    @Test
    public void testGetMessage() {
        GroupResponse response = new GroupResponse();

        response.setMessage("");
        Assert.assertEquals("", response.getMessage());

        response.setMessage("Jin");
        Assert.assertEquals("Jin", response.getMessage());

        response.setMessage("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getMessage());
    }

    @Test
    public void testGetCode() {
        GroupResponse response = new GroupResponse();

        response.setCode(0);
        Assert.assertEquals(0, response.getCode());

        response.setCode(14);
        Assert.assertEquals(14, response.getCode());

        response.setCode(-1);
        Assert.assertEquals(-1, response.getCode());
    }

}
