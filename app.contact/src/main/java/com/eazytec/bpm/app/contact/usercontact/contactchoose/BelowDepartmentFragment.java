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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.adapters.ContactBackNavigationAdapter;
import com.eazytec.bpm.app.contact.adapters.ContactChooseAdapter;
import com.eazytec.bpm.app.contact.adapters.ContactChooseHasChooseAdapter;
import com.eazytec.bpm.app.contact.adapters.DepartmentViewAdapter;
import com.eazytec.bpm.app.contact.data.BackInfoDataTObject;
import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.helper.ListViewHelper;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.checkbox.SmoothCheckBox;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.eazytec.bpm.lib.common.webkit.JsWebViewActiEvent;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 子部门fragment
 * @author Beckett_W
 * @version Id: BelowDepartmentFragment, v 0.1 2017/7/24 13:12 Administrator Exp $$
 */
public class BelowDepartmentFragment extends ContractViewFragment<BelowDepartmentPresenter> implements BelowDepartmentContract.View{

    //父activity控件
    private RecyclerView hasChooseView;
    private TextView hasChooseTextView;
    private ContactChooseHasChooseAdapter contactChooseHasChooseAdapter;

    private RecyclerView doBackView;
    private ContactBackNavigationAdapter contactBackNavigationAdapter;

    //提交按钮
    private Button submitbutton;

    private ScrollView scrollView;

    private LinearLayout departmentLayout;
    private ListView deparmentRecyclerView;

    private LinearLayout userLayout;
    private ListView userRecyclerView;

    private DepartmentViewAdapter departmentViewAdapter;
    private ContactChooseAdapter contactChooseAdapter;

    private List<DepartmentDataTObject> departmentDataTObjects;

    private ArrayList<BackInfoDataTObject> backTObjects;
    private List<UserDetailDataTObject> allDataTObjects = new ArrayList<>();
    private ArrayList<UserDetailDataTObject> chooseDataTObjects;

    private String id;
    private String name;

    //搜索
    private EditText searchEditText;
    private String keyword;
    private AllUserFragment allUserFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_dep_and_user, container, false);

        //获取父控件
        hasChooseView = (RecyclerView) getCommonActivity().findViewById(R.id.contactchoose_haschoose_recyclerview);
        hasChooseTextView = (TextView) getCommonActivity().findViewById(R.id.contactchoose_haschoose_textview);
        searchEditText = (EditText) getCommonActivity().findViewById(R.id.contactchoose_search_input_edittext);
        doBackView = (RecyclerView) getCommonActivity().findViewById(R.id.contactchoose_backnavigation_gridview);
        submitbutton = (Button) getCommonActivity().findViewById(R.id.contactchoose_submit);

        contactChooseHasChooseAdapter = new ContactChooseHasChooseAdapter(getContext());
        hasChooseView.setAdapter(contactChooseHasChooseAdapter);
        contactBackNavigationAdapter = new ContactBackNavigationAdapter(getContext());
        doBackView.setAdapter(contactBackNavigationAdapter);

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

        scrollView = (ScrollView) parentView.findViewById(R.id.dep_and_user_contract_scrollview);

        departmentLayout = (LinearLayout) parentView.findViewById(R.id.dep_and_user_department_layout);
        deparmentRecyclerView = (ListView) parentView.findViewById(R.id.dep_and_user_department_listview);

        userLayout = (LinearLayout) parentView.findViewById(R.id.dep_and_user_layout);
        userRecyclerView = (ListView)parentView.findViewById(R.id.dep_and_user_listview);

        departmentViewAdapter = new DepartmentViewAdapter(getContext());
        deparmentRecyclerView.setAdapter(departmentViewAdapter);
        deparmentRecyclerView.setFocusable(false);

        contactChooseAdapter = new ContactChooseAdapter(getContext());
        userRecyclerView.setAdapter(contactChooseAdapter);
        userRecyclerView.setFocusable(false);

        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.requestFocus();


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
                        JSONArray jsonArray = new JSONArray();

                        if (chooseDataTObjects != null && chooseDataTObjects.size() > 0) {
                            for (UserDetailDataTObject object : chooseDataTObjects) {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("id", object.getId());
                                    jsonObject.put("name", object.getFullName());
                                    jsonArray.put(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("datas", jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        intent.putExtra("datas", jsonObject.toString());
                        intent.putExtra(JsWebViewActiEvent.SMALL_RESULT, JsWebViewActiEvent.USER_CHOOSE);
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

                        BackInfoDataTObject temp = new BackInfoDataTObject();
                        temp.setId(dataTObject.getId());
                        temp.setName(dataTObject.getName());
                        backTObjects.add(temp);
                        bundle.putParcelableArrayList("backdatas",backTObjects);
                        bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);
                        BelowDepartmentFragment belowDepartmentFragment = new BelowDepartmentFragment();
                        belowDepartmentFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,belowDepartmentFragment).addToBackStack(null).commit();
                    }
                });

        //员工条目点击事件
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
                    } else{
                        checkBox.setChecked(false);
                        ToastDelegate.info(getContext(),"最多只能选"+UserChooseManager.getOurInstance().getMaxCount()+"个人！");
                        return;
                    }
                }

                // hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + ""));
                contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
                hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + "",UserChooseManager.getOurInstance().getMaxCount()+""));
                contactChooseHasChooseAdapter.setItems(chooseDataTObjects);
                contactChooseHasChooseAdapter.notifyDataSetChanged();
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
        //加载子部门及人员
        getPresenter().loadDep(id);
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
            allDataTObjects = departmentDataTObject.getUsers();
            contactChooseAdapter.resetList(departmentDataTObject.getUsers());
            //防止多选
            contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
            hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + "",UserChooseManager.getOurInstance().getMaxCount()+""));
            contactChooseAdapter.notifyDataSetChanged();
            ListViewHelper.setListViewHeightBasedOnChildren(userRecyclerView);
        } else {
            userLayout.setVisibility(View.GONE);
        }

    }

    @Override
    protected BelowDepartmentPresenter createPresenter() {
        return new BelowDepartmentPresenter();
    }
}

