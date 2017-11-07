package com.eazytec.bpm.app.contact;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.eazytec.bpm.app.contact.adapters.DepartmentViewAdapter;
import com.eazytec.bpm.app.contact.adapters.UserViewAdapter;
import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.helper.ListViewHelper;
import com.eazytec.bpm.app.contact.usercontact.department.DepartmentActivity;
import com.eazytec.bpm.app.contact.usercontact.localcontact.LocalContactActivity;
import com.eazytec.bpm.app.contact.usercontact.search.UserSearchActivity;
import com.eazytec.bpm.app.contact.usercontact.userdetail.UserDetailActivity;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.eazytec.bpm.lib.utils.IntentUtils;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 提供给App.home的通讯录模块Fragment
 * <p>
 * 989899898
 *
 * @author ConDey
 * @version Id: HomeContactFragment, v 0.1 2017/6/30 上午9:06 ConDey Exp $$
 */
public class HomeContactFragment extends ContractViewFragment<UserContactPresenter> implements UserContactContract.View{

    private ScrollView scrollView;
    private RelativeLayout contractSearchRelativeLayout;
    private RelativeLayout localContactRelativeLayout;

    private LinearLayout departmentLayout;
    private ListView deparmentRecyclerView;

    private LinearLayout userLayout;
    private ListView userRecyclerView;

    private DepartmentViewAdapter departmentViewAdapter;
    private UserViewAdapter userViewAdapter;

    private List<DepartmentDataTObject> departmentDataTObjects;
    private List<UserDetailDataTObject> userDetailDataTObjects;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_homecontact, container, false);

        scrollView = (ScrollView) parentView.findViewById(R.id.fragment_contract_scrollview);
        contractSearchRelativeLayout = (RelativeLayout)parentView.findViewById(R.id.fragment_contract_search_relativelayout);
        localContactRelativeLayout = (RelativeLayout)parentView.findViewById(R.id.fragment_contract_local_relativelayout);

        departmentLayout = (LinearLayout) parentView.findViewById(R.id.fragment_department_layout);
        deparmentRecyclerView = (ListView) parentView.findViewById(R.id.fragment_department_listview);

        userLayout = (LinearLayout) parentView.findViewById(R.id.fragment_user_layout);
        userRecyclerView = (ListView)parentView.findViewById(R.id.fragment_user_listview);

        departmentViewAdapter = new DepartmentViewAdapter(getContext());
        deparmentRecyclerView.setAdapter(departmentViewAdapter);
        deparmentRecyclerView.setFocusable(false);

        userViewAdapter = new UserViewAdapter(getContext());
        userRecyclerView.setAdapter(userViewAdapter);
        userRecyclerView.setFocusable(false);

        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.requestFocus();


        //人员搜索
        RxView.clicks(this.contractSearchRelativeLayout).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getCommonActivity().startActivity(getCommonActivity(), UserSearchActivity.class);
                    }
                });

        //手机通讯录
        RxView.clicks(this.localContactRelativeLayout).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        RxPermissions rxPermissions = new RxPermissions(getCommonActivity());
                        rxPermissions.request(Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean aBoolean) {
                                        if(aBoolean){
                                            getCommonActivity().startActivity(getCommonActivity(), LocalContactActivity.class);
                                        }else{
                                            ToastDelegate.info(getContext(),"您没有权限查看手机通讯录");
                                        }
                                    }
                                });


                    }
                });

        //部门条目点击
        RxAdapterView.itemClicks(this.deparmentRecyclerView).throttleFirst(100,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DepartmentDataTObject dataTObject = departmentDataTObjects.get(integer);

                        if (dataTObject.getChildCount() == 0 && dataTObject.getUserCount() == 0) {
                            return;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("id", dataTObject.getId());
                        bundle.putString("name", dataTObject.getName());
                        getCommonActivity().startActivity(getCommonActivity(), DepartmentActivity.class,bundle);
                    }
                });
        //员工条目点击事件
        RxAdapterView.itemClicks(this.userRecyclerView).throttleFirst(100,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        UserDetailDataTObject dataTObject = userDetailDataTObjects.get(integer);

                        Bundle bundle = new Bundle();
                        bundle.putString("id", dataTObject.getId());
                        bundle.putString("name", dataTObject.getFullName());
                        getCommonActivity().startActivity(getCommonActivity(), UserDetailActivity.class,bundle);

                    }
                });

        userViewAdapter.setOnItemCallClickListener(new UserViewAdapter.onItemCallListener() {
            @Override
            public void onCallClick(int i) {

                final String tel = userDetailDataTObjects.get(i).getMobile().toString();
                if (!StringUtils.isEmpty(tel)) {
                    RxPermissions rxPermissions = new RxPermissions(getCommonActivity());
                    rxPermissions.request(Manifest.permission.CALL_PHONE)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if(aBoolean){
                                        Intent intent = IntentUtils.getCallIntent(tel);
                                        startActivity(intent);
                                    }else{
                                        ToastDelegate.info(getContext(), "您没有授权拨打电话");
                                    }
                                }
                            });
                } else {
                    ToastDelegate.info(getContext(), "该用户没有电话号码");
                }
            }
        });

        userViewAdapter.setOnItemMsgClickListener(new UserViewAdapter.onItemMsgListener() {
            @Override
            public void onMsgClick(int i) {
                final String tel = userDetailDataTObjects.get(i).getMobile().toString();
                if (!StringUtils.isEmpty(tel)) {
                    RxPermissions rxPermissions = new RxPermissions(getCommonActivity());
                    rxPermissions.request(Manifest.permission.SEND_SMS)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if(aBoolean){
                                        Intent intent = IntentUtils.getSendSmsIntent(tel,"");
                                        startActivity(intent);
                                    }else{
                                        ToastDelegate.info(getContext(), "您没有授权发送短信");
                                    }
                                }
                            });
                } else {
                    ToastDelegate.info(getContext(), "该用户没有手机号码");
                }

            }
        });
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //加载一级部门列表
        getPresenter().loadDepSuccess();
    }

    @Override
    public void loadDepSuccess(DepartmentDataTObject departmentDataTObject) {
        if (departmentDataTObject.getChilds() != null && departmentDataTObject.getChilds().size() > 0) {

            departmentLayout.setVisibility(View.VISIBLE);

            DepartmentDataTObject[] childs = new DepartmentDataTObject[departmentDataTObject.getChilds().size()];
            departmentDataTObject.getChilds().toArray(childs);
         //   Arrays.sort(childs);
            departmentViewAdapter.resetList(departmentDataTObjects = Arrays.asList(childs));
            departmentViewAdapter.notifyDataSetChanged();
            ListViewHelper.setListViewHeightBasedOnChildren(deparmentRecyclerView);
        } else {
            departmentLayout.setVisibility(View.GONE);
        }

        if (departmentDataTObject.getUsers() != null && departmentDataTObject.getUsers().size() > 0) {
            userLayout.setVisibility(View.VISIBLE);
            userDetailDataTObjects = departmentDataTObject.getUsers();

            userViewAdapter.resetList(departmentDataTObject.getUsers());
            userViewAdapter.notifyDataSetChanged();
            ListViewHelper.setListViewHeightBasedOnChildren(userRecyclerView);
        } else {
            userLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected UserContactPresenter createPresenter() {
        return new UserContactPresenter();
    }
}
