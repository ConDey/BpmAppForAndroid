package com.eazytec.bpm.app.message.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.eazytec.bpm.lib.common.fragment.CommonFragment;

import java.util.List;

/**
 * @author Beckett_W
 * @version Id: MessageTabAdapter, v 0.1 2017/9/26 13:09 Beckett_W Exp $$
 */
public class MessageTabAdapter extends FragmentPagerAdapter {

    private List<String> mTitleList;
    private List<CommonFragment> mFragmentList;

    public MessageTabAdapter(FragmentManager fm, List<String> titles, List<CommonFragment> fragments) {
        super(fm);
        this.mTitleList = titles;
        this.mFragmentList = fragments;
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
