package com.honeywell.hch.airtouch.plateform.http.model.authorize.response;


import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.HandleAuthMessageResponse;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Qian Jin on 3/9/16.
 */
@RunWith(RobolectricTestRunner.class)

public class HandleAuthMessageTest {
    @Test
    public void testGetId() {
        HandleAuthMessageResponse response = new HandleAuthMessageResponse();

        response.setId(0);
        Assert.assertEquals(0, response.getId());

        response.setId(14);
        Assert.assertEquals(14, response.getId());

        response.setId(-1);
        Assert.assertEquals(-1, response.getId());

    }

    @Test
    public void testSetId() {
        HandleAuthMessageResponse response = new HandleAuthMessageResponse();

        response.setId(0);
        Assert.assertEquals(0, response.getId());

        response.setId(14);
        Assert.assertEquals(14, response.getId());

        response.setId(-1);
        Assert.assertEquals(-1, response.getId());

    }

    @Test
    public void testGetMessage() {
        HandleAuthMessageResponse response = new HandleAuthMessageResponse();

        response.setMessage("");
        Assert.assertEquals("", response.getMessage());

        response.setMessage("Jin");
        Assert.assertEquals("Jin", response.getMessage());

        response.setMessage("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getMessage());
    }

    @Test
    public void testSetMessage() {
        HandleAuthMessageResponse response = new HandleAuthMessageResponse();

        response.setMessage("");
        Assert.assertEquals("", response.getMessage());

        response.setMessage("Jin");
        Assert.assertEquals("Jin", response.getMessage());

        response.setMessage("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getMessage());
    }

    @Test
    public void testGetCode() {
        HandleAuthMessageResponse response = new HandleAuthMessageResponse();

        response.setCode("");
        Assert.assertEquals("", response.getCode());

        response.setCode("Jin");
        Assert.assertEquals("Jin", response.getCode());

        response.setCode("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getCode());
    }

    @Test
    public void testSetCode() {
        HandleAuthMessageResponse response = new HandleAuthMessageResponse();

        response.setCode("");
        Assert.assertEquals("", response.getCode());

        response.setCode("Jin");
        Assert.assertEquals("Jin", response.getCode());

        response.setCode("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getCode());
    }
}
