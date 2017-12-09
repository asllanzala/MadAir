package com.honeywell.hch.airtouch.ui.common.manager;

import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 11/11/16.
 */
public class CategoryTest {

    @Test
    public void testCategory() {
        Category category = new Category("");

        category.setType(0);
        Assert.assertEquals(0, category.getType());
        category.setmCategoryName("");
        Assert.assertEquals("", category.getmCategoryName());

        category.addGoupItem(null);
        Assert.assertEquals(null, category.getmDeviceGroupListItem());

        category.addSelectItem(null);
        Assert.assertNull(category.getmSelectDeviceListItem());

        category.setmDeviceGroupListItem(null);
        Assert.assertNull(category.getmDeviceGroupListItem());

        category.setmSelectDeviceListItem(null);
        Assert.assertNull(category.getmSelectDeviceListItem());

        Assert.assertEquals("", category.getItem(0));

        DeviceGroupItem deviceGroupItem0 = new DeviceGroupItem();
        DeviceGroupItem deviceGroupItem1 = new DeviceGroupItem();
        List<DeviceGroupItem> deviceGroupItemList = new ArrayList<>();
        deviceGroupItemList.add(deviceGroupItem0);
        deviceGroupItemList.add(deviceGroupItem1);
        category.setmDeviceGroupListItem(deviceGroupItemList);
        category.setType(0);
        Assert.assertEquals(deviceGroupItem0, category.getItem(1));

        SelectStatusDeviceItem selectStatusDeviceItem0 = new SelectStatusDeviceItem();
        SelectStatusDeviceItem selectStatusDeviceItem1 = new SelectStatusDeviceItem();
        ArrayList<SelectStatusDeviceItem> selectStatusDeviceItemArrayList = new ArrayList<>();
        selectStatusDeviceItemArrayList.add(selectStatusDeviceItem0);
        selectStatusDeviceItemArrayList.add(selectStatusDeviceItem1);
        category.setmSelectDeviceListItem(selectStatusDeviceItemArrayList);
        category.setType(1);
        Assert.assertEquals(selectStatusDeviceItem0, category.getItem(1));

        category.setType(0);
        Assert.assertEquals(3, category.getItemCount());
        category.setType(1);
        Assert.assertEquals(3, category.getItemCount());
        category.setType(2);
        Assert.assertEquals(0, category.getItemCount());

        category.setAllDeviceSelectStatus(true);
        Assert.assertEquals(true, category.getmSelectDeviceListItem().get(0).isSelected());
    }
}
