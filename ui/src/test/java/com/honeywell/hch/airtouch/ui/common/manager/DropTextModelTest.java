package com.honeywell.hch.airtouch.ui.common.manager;

import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.util.HashMap;

/**
 * Created by Vincent on 11/11/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DropTextModelTest {

    @Test
    public void testDropTextModel() {

        DropTextModel dropTextModel = new DropTextModel("");
        Assert.assertEquals("", dropTextModel.getmTextViewString());
        City city = null;
        DropTextModel dropTextModel1 = new DropTextModel(city);
        Assert.assertEquals(null, dropTextModel1.getmCity());

        DropTextModel dropTextModel2 = new DropTextModel("", 1);
        Assert.assertEquals("", dropTextModel2.getmTextViewString());
        Assert.assertEquals(1, dropTextModel2.getLeftImageId());

        HashMap< String, String >  hashMap = new HashMap<>();
        hashMap.put("code", "code");
        hashMap.put("nameZh", "nameZh");
        hashMap.put("nameEn","nameEn");
        hashMap.put("isCurrent", "2");
        City city1 = new City(hashMap);
        dropTextModel.setmCity(city1);
        Assert.assertEquals(city1, dropTextModel.getmCity());

        dropTextModel.setTextViewString("123");
        Assert.assertEquals("123", dropTextModel.getmTextViewString());

        dropTextModel.setLeftImageId(22);
        Assert.assertEquals(22, dropTextModel.getLeftImageId());
        dropTextModel.setRightTextViewString("2323");
        Assert.assertEquals("2323", dropTextModel.getRightTextViewString());
        dropTextModel.setLocationId(333);
        Assert.assertEquals(333,dropTextModel.getLocationId());

        DropTextModel dropTextModel3 = new DropTextModel("222","222");
        Assert.assertEquals("222",dropTextModel3.getRightTextViewString());
        Assert.assertEquals("222",dropTextModel3.getmTextViewString());
    }
}
