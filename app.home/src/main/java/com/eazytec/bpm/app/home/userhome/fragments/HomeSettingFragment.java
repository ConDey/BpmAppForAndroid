package com.eazytec.bpm.app.home.userhome.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.lib.common.fragment.CommonFragment;

/**
 * 用户设置页的Fragment
 *
 * @author ConDey
 * @version Id: HomeSettingFragment, v 0.1 2017/6/30 上午9:06 ConDey Exp $$
 */
public class HomeSettingFragment extends CommonFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_homesetting, container, false);

        return parentView;
    }


}
