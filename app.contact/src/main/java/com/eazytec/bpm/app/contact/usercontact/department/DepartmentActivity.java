package com.eazytec.bpm.app.contact.usercontact.department;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.adapters.DepartmentViewAdapter;
import com.eazytec.bpm.app.contact.adapters.UserViewAdapter;
import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.helper.ListViewHelper;
import com.eazytec.bpm.app.contact.usercontact.userdetail.UserDetailActivity;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/4.
 */

public class DepartmentActivity extends ContractViewActivity<DepartmentPresenter> implements DepartmentContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private ScrollView scrollView;

    private LinearLayout departmentLayout;
    private ListView deparmentRecyclerView;

    private LinearLayout userLayout;
    private ListView userRecyclerView;

    private DepartmentViewAdapter departmentViewAdapter;
    private UserViewAdapter userViewAdapter;

    private List<DepartmentDataTObject> departmentDataTObjects;
    private List<UserDetailDataTObject> userDetailDataTObjects;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText(name);

        scrollView = (ScrollView) findViewById(R.id.dep_contract_scrollview);

        departmentLayout = (LinearLayout) findViewById(R.id.dep_department_layout);
        deparmentRecyclerView = (ListView) findViewById(R.id.dep_department_listview);

        userLayout = (LinearLayout) findViewById(R.id.dep_user_layout);
        userRecyclerView = (ListView) findViewById(R.id.dep_user_listview);

        departmentViewAdapter = new DepartmentViewAdapter(getContext());
        deparmentRecyclerView.setAdapter(departmentViewAdapter);
        deparmentRecyclerView.setFocusable(false);

        userViewAdapter = new UserViewAdapter(getContext());
        userRecyclerView.setAdapter(userViewAdapter);
        userRecyclerView.setFocusable(false);

        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.requestFocus();

        //加载部门信息
        getPresenter().loadDep(id);


        //部门条目点击
        RxAdapterView.itemClicks(this.deparmentRecyclerView).throttleFirst(100, TimeUnit.MILLISECONDS)
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

                        startActivity(DepartmentActivity.this, DepartmentActivity.class, bundle);
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

                        startActivity(DepartmentActivity.this, UserDetailActivity.class, bundle);

                    }
                });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DepartmentActivity.this.finish();
            }
        });


    }

    @Override
    public void loadDepSuccess(DepartmentDataTObject departmentDataTObject) {
        if (departmentDataTObject.getChilds() != null && departmentDataTObject.getChilds().size() > 0) {

            departmentLayout.setVisibility(View.VISIBLE);

            DepartmentDataTObject[] childs = new DepartmentDataTObject[departmentDataTObject.getChilds().size()];
            departmentDataTObject.getChilds().toArray(childs);
          //  Arrays.sort(childs, Collections.<DepartmentDataTObject>reverseOrder());
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
    protected DepartmentPresenter createPresenter() {
        return new DepartmentPresenter();
    }
}
