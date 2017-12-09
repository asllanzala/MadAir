package com.honeywell.hch.airtouch.plateform.http.model.authorize.response;

import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.CheckAuthUserResponse;

import junit.framework.Assert;
import junit.runner.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Qian Jin on 3/9/16.
 */
@RunWith(RobolectricTestRunner.class)
public class CheckAuthUserResponseTest {
    @Test
    public void testGetPhoneNumber() {
        CheckAuthUserResponse response = new CheckAuthUserResponse();

        response.setPhoneNumber("");
        Assert.assertEquals("", response.getPhoneNumber());

        response.setPhoneNumber("Jin");
        Assert.assertEquals("Jin", response.getPhoneNumber());

        response.setPhoneNumber("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getPhoneNumber());

    }

    @Test
    public void testSetPhoneNumber() {
        CheckAuthUserResponse response = new CheckAuthUserResponse();

        response.setPhoneNumber("");
        Assert.assertEquals("", response.getPhoneNumber());

        response.setPhoneNumber("Jin");
        Assert.assertEquals("Jin", response.getPhoneNumber());

        response.setPhoneNumber("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getPhoneNumber());

    }

    @Test
    public void testIsHPlusUser() {
        CheckAuthUserResponse response = new CheckAuthUserResponse();

        response.setIsHPlusUser(true);
        Assert.assertEquals(true, response.isHPlusUser());

        response.setIsHPlusUser(false);
        Assert.assertEquals(false, response.isHPlusUser());

    }

    @Test
    public void testSetHPlusUser() {
        CheckAuthUserResponse response = new CheckAuthUserResponse();

        response.setIsHPlusUser(true);
        Assert.assertEquals(true, response.isHPlusUser());

        response.setIsHPlusUser(false);
        Assert.assertEquals(false, response.isHPlusUser());

    }

    @Test
     public void testGetmUserName() {
        CheckAuthUserResponse response = new CheckAuthUserResponse();

        response.setUserName("");
        Assert.assertEquals("", response.getmUserName());

        response.setUserName("Jin");
        Assert.assertEquals("Jin", response.getmUserName());

        response.setUserName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getmUserName());
    }

    @Test
    public void testSetUserName() {
        CheckAuthUserResponse response = new CheckAuthUserResponse();

        response.setUserName("");
        Assert.assertEquals("", response.getmUserName());

        response.setUserName("Jin");
        Assert.assertEquals("Jin", response.getmUserName());

        response.setUserName("@#$%s安康");
        Assert.assertEquals("@#$%s安康", response.getmUserName());
    }

}
