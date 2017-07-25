package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.UserContactActivity;
import com.eazytec.bpm.app.contact.adapters.UserViewAdapter;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.CommonActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用人员选择的简单实例
 * @author Beckett_W
 * @version Id: UserChoosedemoActivity, v 0.1 2017/7/25 8:52 Administrator Exp $$
 */
public class UserChoosedemoActivity extends CommonActivity{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    public static final int REQUEST_CODE_MULTI =  222;
    //多选
    private Button multiButton;

    //结果显示
    private ListView userRecyclerView;
    private UserViewAdapter userViewAdapter;
    private List<UserDetailDataTObject> chooseDataTObjects = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactchoose_demo);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("样例");


        userRecyclerView = (ListView) findViewById(R.id.select_result_people);
        userViewAdapter = new UserViewAdapter(getContext());
        userRecyclerView.setAdapter(userViewAdapter);
        userRecyclerView.setFocusable(false);


        multiButton = (Button) findViewById(R.id.select_type_multi);
        multiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      Intent intent = new Intent(UserChoosedemoActivity.this, UserChooseActivity.class);
                      //可以传送参数"choosedatas"，把已选的人员传进去,xxx为已选择的人
                      // intent.putParcelableArrayListExtra("choosedatas",xxx)
                      startActivityForResult(intent,REQUEST_CODE_MULTI);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChoosedemoActivity.this.finish();
            }
        });

    }

    //返回的人员信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_MULTI:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {

                    chooseDataTObjects=data.getParcelableArrayListExtra("datas");
                    userViewAdapter.resetList(chooseDataTObjects);
                    userViewAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
