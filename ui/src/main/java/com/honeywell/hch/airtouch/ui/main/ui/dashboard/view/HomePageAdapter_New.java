package com.honeywell.hch.airtouch.ui.main.ui.dashboard.view;

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
 * <p/>
 * Modified from {@link android.support.v4.app.FragmentPagerAdapter},
 * change the type of the itemId to String (generate unique id based on location id and devices id).
 */

public class HomePageAdapter_New extends FragmentStatePagerAdapter {

    private List<BaseRequestFragment> homeFragments;

    private FragmentManager mFm;

    public HomePageAdapter_New(FragmentManager fm, List<BaseRequestFragment> homeFragments) {
        super(fm);
        this.homeFragments = homeFragments;
        mFm = fm;
    }


    @Override
    public BaseRequestFragment getItem(int arg0) {
        return homeFragments.get(arg0);
    }

    @Override
    public int getCount() {
        return homeFragments.size();
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

