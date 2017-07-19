package com.eazytec.bpm.app.filepicker.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * @author Administrator
 * @version Id: BaseFragment, v 0.1 2017/7/19 10:10 Administrator Exp $$
 */
public abstract class BaseFragment extends Fragment {

    public static final String FILE_TYPE="FILE_TYPE";
    public BaseFragment() {
        // Required empty public constructor
    }

    protected void fadeIn(View view)
    {
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in);

        view.startAnimation(bottomUp);
        view.setVisibility(View.VISIBLE);
    }

    protected void fadeOut(View view)
    {
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_out);

        view.startAnimation(bottomUp);
        view.setVisibility(View.GONE);
    }
}
