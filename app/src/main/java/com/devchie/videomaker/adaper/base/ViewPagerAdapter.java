package com.devchie.videomaker.adaper.base;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragments.ensureCapacity(4);
        mTitles = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments != null ? mFragments.get(position) : null;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void addFragment(int position, Fragment fragment) {
        mFragments.add(position, fragment);
        mTitles.add(String.valueOf(position));
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
        mTitles.add(String.valueOf(mFragments.size()));
    }

    public void addFragment(String title, Fragment fragment) {
        mFragments.add(fragment);
        mTitles.add(title);
    }

    public void removeAll() {
        if (getCount() > 0) {
            mFragments.clear();
            mTitles.clear();
        }
    }

    public ArrayList<Fragment> getFragments() {
        return mFragments;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
