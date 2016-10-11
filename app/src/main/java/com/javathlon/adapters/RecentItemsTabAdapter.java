package com.javathlon.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.javathlon.fragments.RecentFilesFragment;

/**
 * Created by talha on 30.07.2015.
 */
public class RecentItemsTabAdapter extends FragmentPagerAdapter {

    public RecentItemsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new RecentFilesFragment();
            case 1:
                return new RecentFilesFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
