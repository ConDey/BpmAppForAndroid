package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.adapters.ContactBackNavigationAdapter;
import com.eazytec.bpm.app.contact.adapters.ContactChooseHasChooseAdapter;
import com.eazytec.bpm.app.contact.adapters.SpacesItemDecoration;
import com.eazytec.bpm.app.contact.data.BackInfoDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.eazytec.bpm.lib.utils.ConvertUtils;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.WindowUtil;
import com.jakewharton.rxbinding.view.RxView;

import net.wequick.small.Small;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * @author Beckett_W
 * @version Id: UserChooseActivity, v 0.1 2017/7/24 13:13 Administrator Exp $$
 */
public class UserChooseActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;
    private Button submitbutton;

    private TopDepartmentFragment topDepartmentFragment;
    private AllUserFragment allUserFragment;


    //返回相关
    private RecyclerView doBackView;
    private ContactBackNavigationAdapter contactBackNavigationAdapter;
    private ArrayList<BackInfoDataTObject> backTObjects;

    //已选人员
    private RecyclerView hasChooseView;
    private TextView hasChooseTextView;
    private ArrayList<UserDetailDataTObject> chooseDataTObjects; //和钉钉那样，可能之前已经有选人的记录了，判断是否有传入选择参数
    private ContactChooseHasChooseAdapter contactChooseHasChooseAdapter;

    //搜索
    private EditText searchEditText;
    private String keyword;


    //多选的照片数量
    private int chooseMaxNum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactchoose);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        submitbutton = (Button) findViewById(R.id.contactchoose_submit);
        submitbutton.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("人员选择");

        hasChooseView = (RecyclerView) findViewById(R.id.contactchoose_haschoose_recyclerview);
        hasChooseTextView = (TextView) findViewById(R.id.contactchoose_haschoose_textview);
        doBackView = (RecyclerView) findViewById(R.id.contactchoose_backnavigation_gridview);
        searchEditText = (EditText)findViewById(R.id.contactchoose_search_input_edittext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hasChooseView.addItemDecoration(new SpacesItemDecoration(3));
        hasChooseView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        doBackView.addItemDecoration(new SpacesItemDecoration(3));
        doBackView.setLayoutManager(linearLayoutManager2);
       // int numColumns = (WindowUtil.getScreenWidth(getContext()) - ConvertUtils.dp2px(10)) / ConvertUtils.dp2px( 55);
       // hasChooseView.setNumColumns(numColumns);

        contactChooseHasChooseAdapter = new ContactChooseHasChooseAdapter(getContext());
        hasChooseView.setAdapter(contactChooseHasChooseAdapter);
        contactBackNavigationAdapter = new ContactBackNavigationAdapter(getContext());
        doBackView.setAdapter(contactBackNavigationAdapter);

        //判断是否有传入参数
        //返回导航肯定没有传入参数,所以要初始化，但是如果有啥公司名，可以修改为自动获取这个名字，这里就写死部门组织架构
        backTObjects = new ArrayList<>();
        BackInfoDataTObject backInfoDataTObject = new BackInfoDataTObject();
        backInfoDataTObject.setId("Organization");
        backInfoDataTObject.setName("全部");
        backTObjects.add(backInfoDataTObject);
        contactBackNavigationAdapter.setItems(backTObjects);
        contactBackNavigationAdapter.notifyDataSetChanged();

        //看看是否有已选择的人
        chooseDataTObjects = getIntent().getParcelableArrayListExtra("choosedatas");
        if(chooseDataTObjects == null){
            //没有传入
            chooseDataTObjects = new ArrayList<>();
        }

        //判断是否有大小传入
            String tempnum = getIntent().getStringExtra("numdatas");
            if (!StringUtils.isSpace(tempnum)) {
                chooseMaxNum = Integer.parseInt(tempnum);
            }

         //插件传入判断
        Uri uri = Small.getUri(this);
        if (uri != null) {
            //判断是否有大小传入
            String numString = uri.getQueryParameter("numdatas");
            if (!StringUtils.isSpace(numString)) {
                chooseMaxNum = Integer.parseInt(tempnum);
            }
        }

        if(chooseMaxNum >= 1){
            UserChooseManager.getOurInstance().setMaxCount(chooseMaxNum);
        }else{
            UserChooseManager.getOurInstance().setMaxCount(UserChooseConst.DEFAULT_MAX_COUNT); //这个最大值可以在常量文件里修改
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
    }

    public void initListener(){

        //监听已选择人员的动态删除
        contactChooseHasChooseAdapter.setOnItemClickListener(new ContactChooseHasChooseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                chooseDataTObjects.remove(position); //移除这个
                hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + ""));
                contactChooseHasChooseAdapter.notifyDataSetChanged();
            }
        });
        //提交
        RxView.clicks(this.submitbutton).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("datas", chooseDataTObjects);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });

        //搜索
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = searchEditText.getText().toString();
                if(StringUtils.isSpace(keyword)){
                      // 不做变化
                    return;
                }else{
                    //进行搜索
                    Bundle bundle = new Bundle();
                    bundle.putString("id", "Organization");
                    bundle.putString("name", "全部");
                    bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);  //当前有没有选择的人员
                    bundle.putParcelableArrayList("backdatas",backTObjects);   //用来返回组织架构的
                    allUserFragment = new AllUserFragment();
                    allUserFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,allUserFragment).addToBackStack(null).commit();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(UserChooseActivity.this)
                        .content("确认退出此操作吗？")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                UserChooseActivity.this.finish();
                            }
                        })
                        .negativeText("取消")
                        .negativeColor(Color.BLUE)
                        .show();

            }
        });
    }
}

