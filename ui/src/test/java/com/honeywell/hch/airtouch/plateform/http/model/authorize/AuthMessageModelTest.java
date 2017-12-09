package com.honeywell.hch.airtouch.plateform.http.model.authorize;

import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthMessageModel;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Vincent on 15/4/16.
 */
public class AuthMessageModelTest {
    @Test
    public void testGetMessageId() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmMessageId(123);
        Assert.assertEquals(123, authMessageModel.getMessageId());
    }

    @Test
    public void testGetMessageType() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmMessageType(123);
        Assert.assertEquals(123, authMessageModel.getMessageType());
    }

    @Test
    public void getSenderID() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmSenderID(123);
        Assert.assertEquals(123, authMessageModel.getSenderID());
    }

    @Test
    public void getSenderName() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmSenderName("vincent");
        Assert.assertEquals("vincent", authMessageModel.getSenderName());
    }

    @Test
    public void getLocationId() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmLocationId(123);
        Assert.assertEquals(123, authMessageModel.getLocationId());

    }

    @Test
    public void getLocationName() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmLocationName("vincent");
        Assert.assertEquals("vincent", authMessageModel.getLocationName());
    }

    @Test
    public void getTargetType() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmTargetType(123);
        Assert.assertEquals(123, authMessageModel.getTargetType());
    }

    @Test
    public void getTargetID() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmTargetID(123);
        Assert.assertEquals(123, authMessageModel.getTargetID());
    }

    @Test
    public void getTargetName() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmTargetName("vincent");
        Assert.assertEquals("vincent", authMessageModel.getTargetName());
    }

    @Test
    public void getAuthorityRole() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmAuthorityRole(123);
        Assert.assertEquals(123, authMessageModel.getAuthorityRole());
    }

    @Test
    public void getInvitationTime() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmInvitationTime("1234");
        Assert.assertEquals("1234", authMessageModel.getInvitationTime());
    }

    @Test
    public void getExpirationTime() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmExpirationTime("1234");
        Assert.assertEquals("1234", authMessageModel.getExpirationTime());
    }

    @Test
    public void isExpired() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmIsExpired(true);
        Assert.assertEquals(true, authMessageModel.isExpired());
    }
}
