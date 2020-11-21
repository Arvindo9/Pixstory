package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.login.LoginFragment;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.SignUpFragment;

public class UserLoginAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public UserLoginAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginFragment();
            case 1:
                return new SignUpFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

