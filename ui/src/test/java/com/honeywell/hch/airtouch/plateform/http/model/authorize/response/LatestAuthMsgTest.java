package com.honeywell.hch.airtouch.plateform.http.model.authorize.response;


import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.LatestAuthMsg;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Qian Jin on 3/9/16.
 */
@RunWith(RobolectricTestRunner.class)

public class LatestAuthMsgTest {

    @Test
    public void testGetmSenderName() {
        LatestAuthMsg msg = new LatestAuthMsg();

        msg.setSenderName("");
        Assert.assertEquals("", msg.getmSenderName());

        msg.setSenderName("Jin");
        Assert.assertEquals("Jin", msg.getmSenderName());

        msg.setSenderName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", msg.getmSenderName());
    }

    @Test
    public void testSetSenderName() {
        LatestAuthMsg msg = new LatestAuthMsg();

        msg.setSenderName("");
        Assert.assertEquals("", msg.getmSenderName());

        msg.setSenderName("Jin");
        Assert.assertEquals("Jin", msg.getmSenderName());

        msg.setSenderName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", msg.getmSenderName());
    }

}
