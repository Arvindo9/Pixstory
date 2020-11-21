package com.app.pixstory.ui.dashboard.you.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.pixstory.ui.dashboard.you.fragments.followers.Followers;
import com.app.pixstory.ui.dashboard.you.fragments.followings.Followings;
import com.app.pixstory.ui.dashboard.you.fragments.friends.Friends;

public class YouUsernamePager extends FragmentStatePagerAdapter {
    private int tabCount;

    public YouUsernamePager(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Friends();
            case 1:
                return new Followings();
            case 2:
                return new Followers();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}



