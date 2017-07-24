package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.adapters.ContactBackNavigationAdapter;
import com.eazytec.bpm.app.contact.adapters.ContactChooseHasChooseAdapter;
import com.eazytec.bpm.app.contact.data.BackInfoDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.eazytec.bpm.lib.utils.ConversionUtil;
import com.eazytec.bpm.lib.utils.WindowUtil;

import java.util.ArrayList;

/**
 * @author Beckett_W
 * @version Id: UserChooseActivity, v 0.1 2017/7/24 13:13 Administrator Exp $$
 */
public class UserChooseActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private TopDepartmentFragment topDepartmentFragment;


    //返回相关
    private GridView doBackView;
    private ContactBackNavigationAdapter contactBackNavigationAdapter;
    private ArrayList<BackInfoDataTObject> backTObjects;

    //已选人员
    private GridView hasChooseView;
    private TextView hasChooseTextView;
    private ArrayList<UserDetailDataTObject> chooseDataTObjects; //和钉钉那样，可能之前已经有选人的记录了，判断是否有传入选择参数
    private ContactChooseHasChooseAdapter contactChooseHasChooseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactchoose);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("人员选择");

        hasChooseView = (GridView) findViewById(R.id.contactchoose_haschoose_gridview);
        hasChooseTextView = (TextView) findViewById(R.id.contactchoose_haschoose_textview);
        doBackView = (GridView) findViewById(R.id.contactchoose_backnavigation_gridview);
        int numColumns = (WindowUtil.getScreenWidth(getContext()) - ConversionUtil.dp2px(getContext(), 10)) / ConversionUtil.dp2px(getContext(), 60);
        hasChooseView.setNumColumns(numColumns);

        contactChooseHasChooseAdapter = new ContactChooseHasChooseAdapter(getContext());
        hasChooseView.setAdapter(contactChooseHasChooseAdapter);
        contactBackNavigationAdapter = new ContactBackNavigationAdapter(getContext());
        doBackView.setAdapter(contactBackNavigationAdapter);

        //判断是否有传入参数
        //返回导航肯定没有传入参数,所以要初始化，但是如果有啥公司名，可以修改为自动获取这个名字，这里就写死部门组织架构
        backTObjects = new ArrayList<>();
        BackInfoDataTObject backInfoDataTObject = new BackInfoDataTObject();
        backInfoDataTObject.setId("Organization");
        backInfoDataTObject.setName("部门组织架构");
        backTObjects.add(backInfoDataTObject);
        contactBackNavigationAdapter.setItems(backTObjects);
        contactBackNavigationAdapter.notifyDataSetChanged();

        //看看是否有已选择的人
        chooseDataTObjects = getIntent().getParcelableArrayListExtra("choosedatas");
        if(chooseDataTObjects == null){
            //没有传入
            chooseDataTObjects = new ArrayList<>();
        }
        hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + ""));
        contactChooseHasChooseAdapter.setItems(chooseDataTObjects);
        contactChooseHasChooseAdapter.notifyDataSetChanged();


        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);  //有没有选择的人员
        bundle.putParcelableArrayList("backdatas",backTObjects);   //用来返回组织架构的
        topDepartmentFragment = new TopDepartmentFragment();
        topDepartmentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,topDepartmentFragment).addToBackStack(null).commit();

        initListener();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChooseActivity.this.finish();
            }
        });
    }

    public void initListener(){

        //监听已选择人员的动态删除
        hasChooseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  chooseDataTObjects.remove(position); //移除这个
                   hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + ""));
                  contactChooseHasChooseAdapter.notifyDataSetChanged();
            }
        });
    }
}

