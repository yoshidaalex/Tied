package com.tied.android.tiedapp.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tied.android.tiedapp.ui.fragments.FirstFragment;
import com.tied.android.tiedapp.ui.fragments.SecondFragment;
import com.tied.android.tiedapp.ui.fragments.ThirdFragment;

/**
 * Created by Emmanuel on 6/17/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FirstFragment.newInstance();
            case 1:
                return SecondFragment.newInstance();
            case 2:
                return ThirdFragment.newInstance();
            case 3:
                return FirstFragment.newInstance();
            default:
                return null;
        }
    }


}

