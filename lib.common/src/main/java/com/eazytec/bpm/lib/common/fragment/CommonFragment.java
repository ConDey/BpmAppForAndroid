package com.eazytec.bpm.lib.common.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.eazytec.bpm.lib.common.activity.CommonActivity;

/**
 * 封装的基础CommonFragment, 需要和CommonActivity配合使用
 *
 * @author ConDey
 * @version Id: CommonFragment, v 0.1 2017/6/30 上午8:18 ConDey Exp $$
 */
public class CommonFragment extends Fragment {

    public CommonActivity getCommonActivity() {
        return (CommonActivity) getActivity();
    }

    public Context getContext() {
        return getCommonActivity().getContext();
    }
}
