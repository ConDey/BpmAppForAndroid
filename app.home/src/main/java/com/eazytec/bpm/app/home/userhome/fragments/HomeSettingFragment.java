package com.eazytec.bpm.app.home.userhome.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.authentication.AuthenticationActivity;
import com.eazytec.bpm.app.home.update.UpdateHelper;
import com.eazytec.bpm.app.home.updatepwd.UpdatePwdActivity;
import com.eazytec.bpm.app.home.userhome.appsetting.HomeAppSettingActivity;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.fragment.CommonFragment;
import com.eazytec.bpm.lib.utils.AppUtils;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 用户设置页的Fragment
 *
 * @author ConDey
 * @version Id: HomeSettingFragment, v 0.1 2017/6/30 上午9:06 ConDey Exp $$
 */
public class HomeSettingFragment extends CommonFragment {

    private TextView nameTextView;
    private TextView departmentNameTextView;
    private TextView positionTextView;

    private RelativeLayout updateApkLayout;
    private RelativeLayout updatePwdLayout;
    private RelativeLayout loginoutLayout;
    private RelativeLayout setmenuLayout;
    private RelativeLayout cleanLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_homesetting, container, false);

        nameTextView = (TextView) parentView.findViewById(R.id.homesetting_userinfo_name_textview);
        departmentNameTextView = (TextView)parentView.findViewById(R.id.homesetting_userinfo_departmentname_textview);
        positionTextView = (TextView) parentView.findViewById(R.id.homesetting_userinfo_position_textview);

        updateApkLayout = (RelativeLayout) parentView.findViewById(R.id.homesetting_updateapk_layout);
        updatePwdLayout = (RelativeLayout) parentView.findViewById(R.id.homesetting_updatepwd_layout);
        loginoutLayout = (RelativeLayout) parentView.findViewById(R.id.homesetting_loginout_layout);
        setmenuLayout = (RelativeLayout) parentView.findViewById(R.id.homesetting_menu_setting_layout);
        cleanLayout = (RelativeLayout) parentView.findViewById(R.id.homesetting_clean_cache_layout);

        nameTextView.setText(CurrentUser.getCurrentUser().getUserDetails().getFullName());
        departmentNameTextView.setText(CurrentUser.getCurrentUser().getUserDetails().getDepartmentName());
        positionTextView.setText(CurrentUser.getCurrentUser().getUserDetails().getPosition());

        // 更新apk
        RxView.clicks(this.updateApkLayout).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UpdateHelper.doUpdate(true, getContext(), getActivity());
                    }
                });

        //退出
        RxView.clicks(this.loginoutLayout).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getCommonActivity().skipActivity(getCommonActivity(), AuthenticationActivity.class);
                    }
                });

        //修改密码
        RxView.clicks(this.updatePwdLayout).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getCommonActivity().startActivity(getCommonActivity(), UpdatePwdActivity.class);
                    }
                });

        //设置菜单
        RxView.clicks(this.setmenuLayout).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getCommonActivity().startActivity(getCommonActivity(),HomeAppSettingActivity.class);
                    }
                });

        //清除缓存
        RxView.clicks(this.cleanLayout).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        new MaterialDialog.Builder(getContext())
                                .content("是否清空缓存文件？")
                                .cancelable(true)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        DataCleanManager.cleanApplicationData(getContext(), Environment.getExternalStorageDirectory()+"/"+ AppUtils.getAppName());  //后面那个是自定义的下载路径
                                    }
                                })
                                .positiveText("确定")
                                .positiveColor(getResources().getColor(R.color.color_primary))
                                .negativeText("取消")
                                .negativeColor(getResources().getColor(R.color.color_grey_primary))
                                .show();
                    }
                });

        return parentView;
    }

}
