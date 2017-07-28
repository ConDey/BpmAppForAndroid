package com.eazytec.bpm.app.contact.usercontact.search;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.adapters.UserViewAdapter;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.data.UsersDataTObject;
import com.eazytec.bpm.app.contact.usercontact.userdetail.UserDetailActivity;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.edittext.ClearEditText;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 通讯录人员搜索列表
 * Created by beckett_W on 2017/7/4.
 */

public class UserSearchActivity extends ContractViewActivity<UserSearchPresenter> implements UserSearchContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private ClearEditText searchEditText;
    private ListView searchListView;

    private String keyword = "";
    private List<UserDetailDataTObject> userDetailDataTObjects;

    private UserViewAdapter userViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("人员搜索");


        searchEditText = (ClearEditText) findViewById(R.id.search_input_edittext);
        searchListView = (ListView) findViewById(R.id.user_search_listview);

        userViewAdapter = new UserViewAdapter(getContext());
        searchListView.setAdapter(userViewAdapter);
        searchListView.setFocusable(false);

        getPresenter().loadUserDetail(keyword);


        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = searchEditText.getText().toString();
                    getPresenter().loadUserDetail(keyword);
                    return true;
                } else {
                    return false;
                }
            }
        });

        RxAdapterView.itemClicks(this.searchListView).throttleFirst(100, TimeUnit.MILLISECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                UserDetailDataTObject dataTObject = userDetailDataTObjects.get(integer);

                Bundle bundle = new Bundle();
                bundle.putString("id", dataTObject.getId());
                bundle.putString("name", dataTObject.getFullName());

                startActivity(UserSearchActivity.this, UserDetailActivity.class, bundle);

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSearchActivity.this.finish();
            }
        });


    }


    @Override
    public void loadSuccess(UsersDataTObject usersDataTObject) {
        if (usersDataTObject.getDatas() != null && usersDataTObject.getDatas().size() > 0) {
            userViewAdapter.resetList(userDetailDataTObjects = usersDataTObject.getDatas());
        } else {
            userViewAdapter.resetList(new ArrayList<UserDetailDataTObject>());
            ToastDelegate.info(getContext(),"没有搜索到人员信息");

        }
        userViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected UserSearchPresenter createPresenter() {
        return new UserSearchPresenter();
    }
}
