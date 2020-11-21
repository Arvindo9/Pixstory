package com.app.pixstory.ui.dashboard.others.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.pixstory.ui.dashboard.others.fragment.interest.ByInterestFragment;
import com.app.pixstory.ui.dashboard.others.fragment.nearby.NearbyFragment;

public class HomeOtherPager extends FragmentStatePagerAdapter {
    private int tabCount;

    public HomeOtherPager(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ByInterestFragment();
            case 1:
                return new NearbyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}


