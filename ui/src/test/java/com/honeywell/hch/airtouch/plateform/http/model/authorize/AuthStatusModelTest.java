package com.honeywell.hch.airtouch.plateform.http.model.authorize;

import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthStatusModel;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Vincent on 15/4/16.
 */
public class AuthStatusModelTest {

    @Test
    public void getHasMessage() {
        AuthStatusModel authStatusModel = new AuthStatusModel();
        authStatusModel.setHasMessage(true);
        Assert.assertEquals(true, authStatusModel.getHasMessage());
    }
}
