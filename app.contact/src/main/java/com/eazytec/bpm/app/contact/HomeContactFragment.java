package com.eazytec.bpm.app.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazytec.bpm.lib.common.fragment.CommonFragment;

/**
 * 提供给App.home的通讯录模块Fragment
 *
 * @author ConDey
 * @version Id: HomeContactFragment, v 0.1 2017/6/30 上午9:06 ConDey Exp $$
 */
public class HomeContactFragment extends CommonFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_homecontact, container, false);

        return parentView;
    }


}
