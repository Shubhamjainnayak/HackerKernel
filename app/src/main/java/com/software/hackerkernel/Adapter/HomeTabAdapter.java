package com.software.hackerkernel.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.software.hackerkernel.Fragment.Posts;
import com.software.hackerkernel.Fragment.photos;

public class HomeTabAdapter extends FragmentStatePagerAdapter {

    private String[] tabarray=new String[]{"Photos","Posts"};


    public HomeTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new photos();
            case 1:
                return  new Posts();

        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabarray[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}

