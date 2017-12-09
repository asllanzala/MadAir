package com.honeywell.hch.airtouch.plateform.http.model.authorize.request;


import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthTo;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Qian Jin on 3/1/16.
 */
@RunWith(RobolectricTestRunner.class)
public class AuthToTest {

    @Test
    public void testGetGrantToUser() {
        AuthTo authTo = new AuthTo();

        authTo.setmGrantToUserName("");
        Assert.assertEquals("", authTo.getGrantToUserName());

        authTo.setmGrantToUserName("Jin");
        Assert.assertEquals("Jin", authTo.getGrantToUserName());

        authTo.setmGrantToUserName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authTo.getGrantToUserName());
    }

    @Test
    public void testSetmGrantToUser() {
        AuthTo authTo = new AuthTo();

        authTo.setmGrantToUserName("");
        Assert.assertEquals("", authTo.getGrantToUserName());

        authTo.setmGrantToUserName("Jin");
        Assert.assertEquals("Jin", authTo.getGrantToUserName());

        authTo.setmGrantToUserName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", authTo.getGrantToUserName());
    }

    @Test
    public void testGetGrantToUserId() {
        AuthTo authTo = new AuthTo();

        authTo.setGrantToUserId(0);
        Assert.assertEquals(0, authTo.getGrantToUserId());

        authTo.setGrantToUserId(14);
        Assert.assertEquals(14, authTo.getGrantToUserId());

        authTo.setGrantToUserId(-1);
        Assert.assertEquals(-1, authTo.getGrantToUserId());
    }

    @Test
    public void testSetGrantToUserId() {
        AuthTo authTo = new AuthTo();

        authTo.setGrantToUserId(0);
        Assert.assertEquals(0, authTo.getGrantToUserId());

        authTo.setGrantToUserId(14);
        Assert.assertEquals(14, authTo.getGrantToUserId());

        authTo.setGrantToUserId(-1);
        Assert.assertEquals(-1, authTo.getGrantToUserId());
    }

    @Test
    public void testGetRole() {
        AuthTo authTo = new AuthTo();

        authTo.setRole(0);
        Assert.assertEquals(0, authTo.getRole());

        authTo.setRole(14);
        Assert.assertEquals(14, authTo.getRole());

        authTo.setRole(-1);
        Assert.assertEquals(-1, authTo.getRole());
    }

    @Test
    public void testSetRole() {
        AuthTo authTo = new AuthTo();

        authTo.setRole(0);
        Assert.assertEquals(0, authTo.getRole());

        authTo.setRole(14);
        Assert.assertEquals(14, authTo.getRole());

        authTo.setRole(-1);
        Assert.assertEquals(-1, authTo.getRole());
    }

    @Test
    public void testGetStatus() {
        AuthTo authTo = new AuthTo();

        authTo.setStatus(0);
        Assert.assertEquals(0, authTo.getStatus());

        authTo.setStatus(14);
        Assert.assertEquals(14, authTo.getStatus());

        authTo.setStatus(-1);
        Assert.assertEquals(-1, authTo.getStatus());
    }

    @Test
    public void testSetStatus() {
        AuthTo authTo = new AuthTo();

        authTo.setStatus(0);
        Assert.assertEquals(0, authTo.getStatus());

        authTo.setStatus(14);
        Assert.assertEquals(14, authTo.getStatus());

        authTo.setStatus(-1);
        Assert.assertEquals(-1, authTo.getStatus());
    }

}
