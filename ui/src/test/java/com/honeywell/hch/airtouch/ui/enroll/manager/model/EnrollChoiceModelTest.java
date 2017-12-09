package com.honeywell.hch.airtouch.ui.enroll.manager.model;

import com.honeywell.hch.airtouch.ui.enroll.models.EnrollChoiceModel;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Vincent on 17/11/16.
 */
public class EnrollChoiceModelTest {

    @Test
    public void testCategory() {
        EnrollChoiceModel enrollChoiceModel = new EnrollChoiceModel(123, 123, 123);
        Assert.assertEquals(123, enrollChoiceModel.getDeviceImage());
        Assert.assertEquals(123, enrollChoiceModel.getDeviceName());
        Assert.assertEquals(123, enrollChoiceModel.getDeviceType());

        enrollChoiceModel.setDeviceImage(11);
        Assert.assertEquals(11, enrollChoiceModel.getDeviceImage());
        enrollChoiceModel.setDeviceName(11);
        Assert.assertEquals(11, enrollChoiceModel.getDeviceName());
        enrollChoiceModel.setDeviceType(11);
        Assert.assertEquals(11, enrollChoiceModel.getDeviceType());
        enrollChoiceModel.setAlpha(11f);
        Assert.assertEquals(11f, enrollChoiceModel.getAlpha());
    }
}
