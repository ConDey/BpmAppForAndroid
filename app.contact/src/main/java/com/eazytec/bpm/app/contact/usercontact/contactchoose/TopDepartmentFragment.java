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
 * @author Beckett_W
 * @version Id: TopDepartmentFragment, v 0.1 2017/7/24 13:11 Administrator Exp $$
 */
public class TopDepartmentFragment extends ContractViewFragment<TopDepartmentPresenter> implements TopDepartmentContract.View {

    //返回相关
    private RecyclerView doBackView;
    private ContactBackNavigationAdapter contactBackNavigationAdapter;
    private ArrayList<BackInfoDataTObject> backTObjects;

    //已选人员
    private RecyclerView hasChooseView;
    private TextView hasChooseTextView;
    private ArrayList<UserDetailDataTObject> chooseDataTObjects;
    private ContactChooseHasChooseAdapter contactChooseHasChooseAdapter;

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

    private List<UserDetailDataTObject> allDataTObjects = new ArrayList<>();
    private boolean singlechoose;

    //搜索
    private EditText searchEditText;
    private String keyword;
    private AllUserFragment allUserFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_dep_and_user, container, false);

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
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            //返回的
            backTObjects = bundle.getParcelableArrayList("backdatas");
            if (backTObjects == null) {
                backTObjects = new ArrayList<>();
            }
            contactBackNavigationAdapter.setItems(backTObjects);
            contactBackNavigationAdapter.notifyDataSetChanged();

            //已经选择的人
            chooseDataTObjects = bundle.getParcelableArrayList("choosedatas");
            if (chooseDataTObjects == null) {
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
        userRecyclerView = (ListView) parentView.findViewById(R.id.dep_and_user_listview);

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

    private void initListener() {
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
                contactChooseHasChooseAdapter.notifyDataSetChanged();
                contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
                hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + "", UserChooseManager.getOurInstance().getMaxCount() + ""));
                contactChooseAdapter.notifyDataSetChanged();
                //已选择列表也要监听变动
            }
        });

        //部门条目点击
        RxAdapterView.itemClicks(this.deparmentRecyclerView).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DepartmentDataTObject dataTObject = departmentDataTObjects.get(integer);

                        if (dataTObject.getChildCount() == 0 && dataTObject.getUserCount() == 0) {
                            ToastDelegate.info(getContext(), "此部门下面没有相关信息");
                            return;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("id", dataTObject.getId());
                        bundle.putString("name", dataTObject.getName());

                        BackInfoDataTObject temp = new BackInfoDataTObject();
                        temp.setId(dataTObject.getId());
                        temp.setName(dataTObject.getName());
                        backTObjects.add(temp);
                        bundle.putParcelableArrayList("backdatas", backTObjects);
                        bundle.putParcelableArrayList("choosedatas", chooseDataTObjects);
                        BelowDepartmentFragment belowDepartmentFragment = new BelowDepartmentFragment();
                        belowDepartmentFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout, belowDepartmentFragment).addToBackStack(null).commit();


                    }
                });


        //员工条目点击事件
        RxAdapterView.itemClicks(this.userRecyclerView).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        //总的组织架构下没有员工，我就不写了，如果有的话，参照子部门传递参数选择即可
                    }
                });

        //搜索
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = searchEditText.getText().toString();
                if (StringUtils.isSpace(keyword)) {
                    // 不做变化
                    return;
                } else {
                    //进行搜索
                    Bundle bundle = new Bundle();
                    bundle.putString("id", "Organization");
                    bundle.putString("name", "全部");
                    bundle.putParcelableArrayList("choosedatas", chooseDataTObjects);  //当前有没有选择的人员
                    bundle.putParcelableArrayList("backdatas", backTObjects);   //用来返回组织架构的
                    allUserFragment = new AllUserFragment();
                    allUserFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout, allUserFragment).addToBackStack(null).commit();
                }
            }
        });

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
            allDataTObjects = departmentDataTObject.getUsers();
            contactChooseAdapter.resetList(departmentDataTObject.getUsers());
            contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
            contactChooseAdapter.notifyDataSetChanged();
            ListViewHelper.setListViewHeightBasedOnChildren(userRecyclerView);
        } else {
            userLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected TopDepartmentPresenter createPresenter() {
        return new TopDepartmentPresenter();
    }
}

