package com.example.umorning.tabswipeadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.umorning.fragments.AlarmsFragment;
import com.example.umorning.fragments.EventsFragment;
import com.example.umorning.fragments.HomeFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new HomeFragment();
            case 1:
                // Games fragment activity
                return new AlarmsFragment();
            case 2:
                // Movies fragment activity
                return new EventsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}