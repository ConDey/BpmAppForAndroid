package com.eazytec.bpm.app.filepicker.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.fragments.BaseFragment;

/**
 * @author Administrator
 * @version Id: FragmentUtil, v 0.1 2017/7/19 10:40 Administrator Exp $$
 */
public class FragmentUtil {
    public static boolean hadFragment(AppCompatActivity activity) {
        return activity.getSupportFragmentManager().getBackStackEntryCount() != 0;
    }

    public static void replaceFragment(AppCompatActivity activity, int contentId, BaseFragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);

        transaction.replace(contentId, fragment, fragment.getClass().getSimpleName());

        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static void addFragment(AppCompatActivity activity, int contentId, BaseFragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);

        transaction.add(contentId, fragment, fragment.getClass().getSimpleName());

        transaction.commit();
    }

    public static void removeFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }


    public static void showFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commit();
    }

    public static void hideFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commit();
    }

    public static void attachFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .attach(fragment)
                .commit();
    }

    public static void detachFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .detach(fragment)
                .commit();
    }

    public static Fragment getFragmentByTag(AppCompatActivity appCompatActivity, String tag)
    {
        return appCompatActivity.getSupportFragmentManager().findFragmentByTag(tag);
    }
}
