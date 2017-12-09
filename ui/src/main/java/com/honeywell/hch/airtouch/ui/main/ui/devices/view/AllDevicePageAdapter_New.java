package com.honeywell.hch.airtouch.ui.main.ui.devices.view;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;

import java.util.List;

/**
 * Implementation of {@link android.support.v4.view.PagerAdapter} that
 * represents each page as a {@link Fragment} that is persistently
 * kept in the fragment manager as long as the user can return to the page.
 *
 * Modified from {@link android.support.v4.app.FragmentPagerAdapter},
 * change the type of the itemId to String (generate unique id based on location id and devices id).
 *
 *
 */

public class AllDevicePageAdapter_New extends FragmentStatePagerAdapter {

    private List<AllDeviceFragment> allDeviceFragments;

    public AllDevicePageAdapter_New(FragmentManager fm, List<AllDeviceFragment> allDeviceFragments) {
        super(fm);

        this.allDeviceFragments = allDeviceFragments;
    }


    @Override
    public BaseRequestFragment getItem(int arg0) {
        return allDeviceFragments.get(arg0);
    }

    @Override
    public int getCount() {
        return allDeviceFragments.size();
    }


    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

