package com.honeywell.hch.airtouch.plateform.http.model.authorize.response;


import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.RemoveAuthResponse;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Qian Jin on 3/9/16.
 */
@RunWith(RobolectricTestRunner.class)

public class RemoveAuthMessageTest {

    @Test
    public void testGetMessage() {
        RemoveAuthResponse response = new RemoveAuthResponse();

        response.setMessage("");
        Assert.assertEquals("", response.getMessage());

        response.setMessage("Jin");
        Assert.assertEquals("Jin", response.getMessage());

        response.setMessage("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getMessage());
    }

    @Test
    public void testSetMessage() {
        RemoveAuthResponse response = new RemoveAuthResponse();

        response.setMessage("");
        Assert.assertEquals("", response.getMessage());

        response.setMessage("Jin");
        Assert.assertEquals("Jin", response.getMessage());

        response.setMessage("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getMessage());
    }

    @Test
    public void testGetCode() {
        RemoveAuthResponse response = new RemoveAuthResponse();

        response.setCode("");
        Assert.assertEquals("", response.getCode());

        response.setCode("Jin");
        Assert.assertEquals("Jin", response.getCode());

        response.setCode("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getCode());
    }

    @Test
    public void testSetCode() {
        RemoveAuthResponse response = new RemoveAuthResponse();

        response.setCode("");
        Assert.assertEquals("", response.getCode());

        response.setCode("Jin");
        Assert.assertEquals("Jin", response.getCode());

        response.setCode("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getCode());
    }
}
