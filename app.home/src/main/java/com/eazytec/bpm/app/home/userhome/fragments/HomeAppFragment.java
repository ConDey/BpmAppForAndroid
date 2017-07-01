package com.eazytec.bpm.app.home.userhome.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.userhome.adapters.HomeAppAdapter;
import com.eazytec.bpm.appstub.view.gridview.SingleDividerGridView;
import com.eazytec.bpm.lib.common.fragment.CommonFragment;

/**
 * APP列表页的Fragment
 *
 * @author ConDey
 * @version Id: HomeAppFragment, v 0.1 2017/6/30 上午9:06 ConDey Exp $$
 */
public class HomeAppFragment extends CommonFragment {

    private SingleDividerGridView appGridView;
    private HomeAppAdapter homeAppAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_homeapp, container, false);

        appGridView = (SingleDividerGridView) parentView.findViewById(R.id.gv_homeapp);

        // 设置AppGridView的适配器
        homeAppAdapter = new HomeAppAdapter(getContext());
        appGridView.setAdapter(homeAppAdapter);

        return parentView;
    }


}
