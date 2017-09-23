package com.eazytec.bpm.app.home.userhome;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.TextView;

import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.update.UpdateHelper;
import com.eazytec.bpm.app.home.userhome.fragments.HomeAppFragment;
import com.eazytec.bpm.app.home.userhome.fragments.HomeSettingFragment;
import com.eazytec.bpm.appstub.view.mdbottom.BottomNavigation;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.eazytec.bpm.lib.common.bundle.BundleApplication;
import com.eazytec.bpm.lib.utils.DoubleClickExitHelper;

import net.wequick.small.Small;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 用户首页Activity
 * <p>
 * <p>
 * 其中userhome.app和userhome.setting模块是受app.home插件托管的
 * <p>
 * userhome.message 交由app.message插件托管
 * userhome.contact 交由通app.contact插件托管
 *
 * @author ConDey
 * @version Id: UserHomeActivity, v 0.1 2017/6/29 下午4:19 ConDey Exp $$
 */
public class UserHomeActivity extends CommonActivity implements BottomNavigation.OnMenuItemSelectionListener {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;
    private BottomNavigation bottomNavigation;

    // Fragment管理工具
    private FragmentManager fragmentManager;
    private List<Fragment> fragments;

    private DoubleClickExitHelper doubleClickExitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        toolbar = (Toolbar) findViewById(R.id.tb_common_toolbar);
        toolbarTitleTextView = (TextView) findViewById(R.id.tv_common_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomNavigation = (BottomNavigation) findViewById(R.id.bn_userhome);
        bottomNavigation.setOnMenuItemClickListener(this);

        doubleClickExitHelper = new DoubleClickExitHelper(this); // 注册双击退出事件
        initFragments(); // 初始化Fragments

        bottomNavigation.setDefaultSelectedIndex(0);
        toolbarTitleTextView.setText(getString(R.string.userhome_message));
        switchContentFragment(HOME_MESSAGE_FRAGMENT);

        // 执行更新操作
        UpdateHelper.doUpdate(false, getContext(), this);

    }

    private static final String HOME_MESSAGE_FRAGMENT = "HOME_MESSAGE";
    private static final String HOME_APP_FRAGMENT = "HOME_APP";
    private static final String HOME_CONTACT_FRAGMENT = "HOME_CONTACT";
    private static final String HOME_SETTING_FRAGMENT = "HOME_SETTING";

    /**
     * 初始化Fragments, Fragments交由FragmentManager进行管理
     */
    private void initFragments() {
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (!BundleApplication.isDebug()) {
                Fragment homeMessageFragment = Small.createObject("fragment-v4", "app.message/forhomefragment", UserHomeActivity.this);
                fragmentTransaction.add(R.id.userhome_fl_content, homeMessageFragment, HOME_MESSAGE_FRAGMENT);
                fragments.add(homeMessageFragment);
            }

            HomeAppFragment homeAppFragment = new HomeAppFragment();
            fragmentTransaction.add(R.id.userhome_fl_content, homeAppFragment, HOME_APP_FRAGMENT);
            fragments.add(homeAppFragment);

            // 这段代码最好能Mock掉，侵入性太强了
            if (!BundleApplication.isDebug()) {
                Fragment homeContractFragment = Small.createObject("fragment-v4", "app.contact/forhomefragment", UserHomeActivity.this);
                fragmentTransaction.add(R.id.userhome_fl_content, homeContractFragment, HOME_CONTACT_FRAGMENT);
                fragments.add(homeContractFragment);
            }

            HomeSettingFragment homeSettingFragment = new HomeSettingFragment();
            fragmentTransaction.add(R.id.userhome_fl_content, homeSettingFragment, HOME_SETTING_FRAGMENT);
            fragments.add(homeSettingFragment);

            fragmentTransaction.commit();
            switchContentFragment(HOME_MESSAGE_FRAGMENT);
        }
    }

    /**
     * 双击退出APP事件
     *
     * @param keyCode 键盘keyCode
     * @param event   键盘事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return doubleClickExitHelper.onKeyDown(keyCode, event);
    }

    @Override public void onMenuItemSelect(@IdRes int itemId, int position) {
        switch (itemId) {
            case R.id.bbn_userhome_message_item:
                toolbarTitleTextView.setText(getString(R.string.userhome_message));
                switchContentFragment(HOME_MESSAGE_FRAGMENT);
                break;
            case R.id.bbn_userhome_app_item:
                toolbarTitleTextView.setText(getString(R.string.userhome_app));
                switchContentFragment(HOME_APP_FRAGMENT);
                break;
            case R.id.bbn_userhome_contact_item:
                toolbarTitleTextView.setText(getString(R.string.userhome_contact));
                switchContentFragment(HOME_CONTACT_FRAGMENT);
                break;
            case R.id.bbn_userhome_setting_item:
                toolbarTitleTextView.setText(getString(R.string.userhome_setting));
                switchContentFragment(HOME_SETTING_FRAGMENT);
                break;
        }
    }

    @Override public void onMenuItemReselect(@IdRes int itemId, int position) {
        // do nothing
    }

    /**
     * 触发Tabbar，切换Framgnet
     *
     * @param fragment fragment标识
     */
    private void switchContentFragment(String fragment) {
        FragmentTransaction localFragmentTransaction = this.fragmentManager.beginTransaction();
        Iterator localIterator = this.fragments.iterator();
        while (localIterator.hasNext()) {
            Fragment localFragment = (Fragment) localIterator.next();
            if (localFragment.getTag().equals(fragment)) {
                localFragmentTransaction.show(localFragment);
                localFragment.setUserVisibleHint(true);
            } else {
                localFragmentTransaction.hide(localFragment);
            }
        }
        localFragmentTransaction.commit();
    }
}
