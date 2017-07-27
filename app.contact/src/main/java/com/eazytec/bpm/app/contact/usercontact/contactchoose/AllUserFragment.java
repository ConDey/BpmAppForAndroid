package com.eazytec.bpm.app.contact.usercontact.contactchoose;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.adapters.ContactBackNavigationAdapter;
import com.eazytec.bpm.app.contact.adapters.ContactChooseAdapter;
import com.eazytec.bpm.app.contact.adapters.ContactChooseHasChooseAdapter;
import com.eazytec.bpm.app.contact.data.BackInfoDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.data.UsersDataTObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.checkbox.SmoothCheckBox;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 *
 * @author Beckett_W
 * @version Id: AllUserFragment, v 0.1 2017/7/25 14:52 Administrator Exp $$
 */
public class AllUserFragment extends ContractViewFragment<AllUserPresenter> implements AllUserContract.View{

    //父控件
    private RecyclerView hasChooseView;
    private TextView hasChooseTextView;
    private EditText searchEditText;
    private ContactChooseHasChooseAdapter contactChooseHasChooseAdapter;

    private RecyclerView doBackView;
    private ContactBackNavigationAdapter contactBackNavigationAdapter;

    //提交按钮
    private Button submitbutton;

    private ListView userRecyclerView;
    private ContactChooseAdapter contactChooseAdapter;

    private ArrayList<BackInfoDataTObject> backTObjects;
    private List<UserDetailDataTObject> allDataTObjects = new ArrayList<>();
    private ArrayList<UserDetailDataTObject> chooseDataTObjects;

    private String id;
    private String name;
    private String keyword;

    private AllUserFragment allUserFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View parentView = inflater.inflate(R.layout.fragment_choose_alluser, container, false);
        //获取父控件
        hasChooseView = (RecyclerView) getCommonActivity().findViewById(R.id.contactchoose_haschoose_recyclerview);
        hasChooseTextView = (TextView) getCommonActivity().findViewById(R.id.contactchoose_haschoose_textview);
        doBackView = (RecyclerView) getCommonActivity().findViewById(R.id.contactchoose_backnavigation_gridview);
        searchEditText = (EditText) getCommonActivity().findViewById(R.id.contactchoose_search_input_edittext);
        submitbutton = (Button) getCommonActivity().findViewById(R.id.contactchoose_submit);

        contactChooseHasChooseAdapter = new ContactChooseHasChooseAdapter(getContext());
        hasChooseView.setAdapter(contactChooseHasChooseAdapter);
        contactBackNavigationAdapter = new ContactBackNavigationAdapter(getContext());
        doBackView.setAdapter(contactBackNavigationAdapter);

        //获取传过来的参数，多一个关键字
        //获取前面传过来的参数
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            id = bundle.getString("id");
            name = bundle.getString("name");
            //返回的
            backTObjects = bundle.getParcelableArrayList("backdatas");
            if(backTObjects == null){
                backTObjects = new ArrayList<>();
            }
            contactBackNavigationAdapter.setItems(backTObjects);
            contactBackNavigationAdapter.notifyDataSetChanged();

            //已经选择的人
            chooseDataTObjects = bundle.getParcelableArrayList("choosedatas");
            if(chooseDataTObjects == null){
                //没有传入
                chooseDataTObjects = new ArrayList<>();
            }
            contactChooseHasChooseAdapter.setItems(chooseDataTObjects);
            contactChooseHasChooseAdapter.notifyDataSetChanged();
        }
        userRecyclerView = (ListView)parentView.findViewById(R.id.fragment_choose_alluser_listview);
        contactChooseAdapter = new ContactChooseAdapter(getContext());
        userRecyclerView.setAdapter(contactChooseAdapter);
        userRecyclerView.setFocusable(false);

        initListener();
        return parentView;
    }

    public void initListener(){

        //提交
        RxView.clicks(this.submitbutton).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("datas", chooseDataTObjects);
                        getCommonActivity().setResult(Activity.RESULT_OK, intent);
                        getCommonActivity().finish();
                    }
                });


        //监听已选择人员的动态删除
        contactChooseHasChooseAdapter.setOnItemClickListener(new ContactChooseHasChooseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                chooseDataTObjects.remove(position); //移除这个
                hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + "",UserChooseManager.getOurInstance().getMaxCount()+""));
                contactChooseHasChooseAdapter.notifyDataSetChanged();
                contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
                contactChooseAdapter.notifyDataSetChanged();
                //已选择列表也要监听变动
            }
        });


        //返回条目
        contactBackNavigationAdapter.setOnItemClickListener(new ContactBackNavigationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(backTObjects.get(position).getId().contains("Organization")){
                    //返回到组织架构页面
                    Bundle bundle = new Bundle();
                    bundle.putString("id", backTObjects.get(position).getId());
                    bundle.putString("name", backTObjects.get(position).getName());
                    ArrayList<BackInfoDataTObject> newbackdatas = new ArrayList<BackInfoDataTObject>();
                    for(int i=0; i<=position ;i++){
                        newbackdatas.add(backTObjects.get(i));
                    }
                    bundle.putParcelableArrayList("backdatas",newbackdatas);
                    bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);
                    TopDepartmentFragment topDepartmentFragment = new TopDepartmentFragment();
                    topDepartmentFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,topDepartmentFragment).addToBackStack(null).commit();
                }else{

                    Bundle bundle = new Bundle();
                    bundle.putString("id", backTObjects.get(position).getId());
                    bundle.putString("name", backTObjects.get(position).getName());
                    ArrayList<BackInfoDataTObject> newbackdatas = new ArrayList<BackInfoDataTObject>();
                    for(int i=0; i<=position ;i++){
                        newbackdatas.add(backTObjects.get(i));
                    }
                    bundle.putParcelableArrayList("backdatas",newbackdatas);
                    bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);
                    BelowDepartmentFragment belowDepartmentFragment = new BelowDepartmentFragment();
                    belowDepartmentFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,belowDepartmentFragment).addToBackStack(null).commit();
                }
            }
        });

        //人员选择点击
        userRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SmoothCheckBox checkBox = (SmoothCheckBox) view.findViewById(R.id.item_contactchoose_checkbox);

                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);

                    String needRemoteId = allDataTObjects.get(position).getId();
                    UserDetailDataTObject needRemoteObject = null;

                    for (UserDetailDataTObject obj : chooseDataTObjects) {

                        if (obj.getId().equals(needRemoteId)) {
                            needRemoteObject = obj;
                            break;
                        }
                    }

                    if (needRemoteObject != null) {
                        chooseDataTObjects.remove(needRemoteObject);

                    }

                } else {
                    if(chooseDataTObjects.size() < (UserChooseManager.getOurInstance().getMaxCount())) {
                        checkBox.setChecked(true);
                        UserDetailDataTObject needAddObject = allDataTObjects.get(position);
                        chooseDataTObjects.add(needAddObject);
                    }else{
                        checkBox.setChecked(false);
                        ToastDelegate.info(getContext(),"最多只能选"+UserChooseManager.getOurInstance().getMaxCount()+"个人！");
                        return;
                    }
                }
                contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
                hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + "",UserChooseManager.getOurInstance().getMaxCount()+""));
                contactChooseHasChooseAdapter.setItems(chooseDataTObjects);
                contactChooseHasChooseAdapter.notifyDataSetChanged();
            }
        });

        //搜索监听,为空时不发生任何变化
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword=searchEditText.getText().toString();
                if(StringUtils.isSpace(keyword)){
                    //回到刚才的页面,是一级部门还是回到某个二级子部门
                    if(id.contains("Organization")){
                        //回到一级部门
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);  //有没有选择的人员
                        bundle.putParcelableArrayList("backdatas",backTObjects);   //用来返回组织架构的
                        TopDepartmentFragment topDepartmentFragment = new TopDepartmentFragment();
                        topDepartmentFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,topDepartmentFragment).addToBackStack(null).commit();
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("name", name);
                        bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);  //当前有没有选择的人员
                        bundle.putParcelableArrayList("backdatas",backTObjects);   //用来返回组织架构的
                        BelowDepartmentFragment belowDepartmentFragment = new BelowDepartmentFragment();
                        belowDepartmentFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,belowDepartmentFragment).addToBackStack(null).commit();
                    }
                }else{
                    //继续搜索
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    bundle.putString("name", name);
                    bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);  //当前有没有选择的人员
                    bundle.putParcelableArrayList("backdatas",backTObjects);   //用来返回组织架构的
                    allUserFragment = new AllUserFragment();
                    allUserFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,allUserFragment).addToBackStack(null).commit();
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //根据关键字删选人员
        keyword = searchEditText.getText().toString();
        getPresenter().loadUserDetail(keyword);
    }

    @Override
    public void loadSuccess(UsersDataTObject usersDataTObject) {
        if (usersDataTObject.getDatas() != null && usersDataTObject.getDatas().size() > 0) {
            allDataTObjects = usersDataTObject.getDatas();
            contactChooseAdapter.resetList(usersDataTObject.getDatas());
            //防止多选
            contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
            hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + "",UserChooseManager.getOurInstance().getMaxCount()+""));
        } else {
            contactChooseAdapter.resetList(new ArrayList<UserDetailDataTObject>());
            ToastDelegate.info(getContext(),"没有搜索到人员信息");

        }
            contactChooseAdapter.notifyDataSetChanged();
    }


    @Override
    protected AllUserPresenter createPresenter() {
        return new AllUserPresenter();
    }
}
