package com.eazytec.bpm.app.message.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.app.message.R;
import com.eazytec.bpm.app.message.utils.RefreshRecyclerViewUtil;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.recyclerview.OnNextPageListener;
import com.eazytec.bpm.appstub.view.recyclerview.OnRecyclerViewItemClickListener;
import com.eazytec.bpm.appstub.view.recyclerview.RefreshRecyclerView;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.eazytec.bpm.lib.common.message.CurrentMessage;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.utils.EncodeUtils;
import com.eazytec.bpm.lib.utils.StringUtils;

import net.wequick.small.Small;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beckett_W
 * @version Id: ReadedFragment, v 0.1 2017/11/4 9:31 Beckett_W Exp $$
 */
public class ReadedFragment extends ContractViewFragment<MessageDetailPresenter> implements MessageDetailContract.View {

    private static final int PAGE_STARTING = 0; // 代表起始页

    private RefreshRecyclerView refreshRecyclerView;
    private MessageDetailAdapter messageDetailAdapter;

    // 分页部分
    private int pageNo;
    private int pageSize = 10;
    private boolean isPullRefresh = false;

    //空view
    private LinearLayout emptyLinearLayout;

    private boolean longPress;

    private boolean init;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        init = true;
        View parentView = inflater.inflate(R.layout.activity_message_detail, container, false);

        emptyLinearLayout = (LinearLayout) parentView.findViewById(R.id.message_detail_list_empty_view);
        refreshRecyclerView = (RefreshRecyclerView) parentView.findViewById(R.id.message_detail_listview);

        List<MessageDataTObject> datas = new ArrayList<>();
        messageDetailAdapter = new MessageDetailAdapter(getContext(),datas);
        refreshRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshRecyclerView.setAdapter(messageDetailAdapter);
        refreshRecyclerView.setRefreshEnable(true);
        messageDetailAdapter.notifyDataSetChanged();

        RefreshRecyclerViewUtil.initRefreshViewColorSchemeColors(refreshRecyclerView,getResources());

        setListener();
        initData();

        return parentView;
    }


    private void setListener() {
        // 刷新监听
        refreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onDoRefresh();
            }
        });

        // 请求下一页监听
        refreshRecyclerView.setOnNextPageListener(new OnNextPageListener() {
            @Override
            public void onNextPage() {
                onDoNextPage();
            }
        });


        //点击刷新
        emptyLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshRecyclerView.setVisibility(View.VISIBLE);
                emptyLinearLayout.setVisibility(View.GONE);
                refreshRecyclerView.setRefreshing(true);
                onDoRefresh();
            }
        });

        messageDetailAdapter.setOnLongItemClickListener(new MessageDetailAdapter.OnRecyclerViewLongItemClickListener() {
            @Override
            public void onLongItemClick(View view, final int position, final Object data) {
                new MaterialDialog.Builder(getContext())
                        .content("删除这条消息？")
                        .cancelable(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //跳转
                                MessageDataTObject messageDataTObject = (MessageDataTObject) data;

                                if (messageDataTObject != null) {
                                    //从数据库删除这条数据
                                    getPresenter().deleteMessage(messageDataTObject.getTopicId(),messageDataTObject.getId());
                                    isPullRefresh = false;
                                    onDoRefresh();
                                }
                            }
                        })
                        .positiveText("删除")
                        .negativeText("取消")
                        .positiveColor(getResources().getColor(R.color.color_primary))
                        .negativeColor(getResources().getColor(R.color.color_grey_primary))
                        .show();
            }
        });

        messageDetailAdapter.setOnItemClickListener(new MessageDetailAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                MessageDataTObject messageDataTObject = (MessageDataTObject) data;
                if (messageDataTObject != null) {
                    if (messageDataTObject.isCanClick()) {
                        if(!messageDataTObject.getIsRead()){
                            CurrentMessage.getCurrentMessage().upDateMessageIsReadState(messageDataTObject.getTopicId(),messageDataTObject.getId());
                            getPresenter().setReaded(messageDataTObject.getInternalMsgId());

                        }
                        //根据url跳转
                        String url;
                        String clicklurl = messageDataTObject.getClickUrl();
                        if(!StringUtils.isEmpty(clicklurl)){
                            if(clicklurl.startsWith("native")){
                                clicklurl = clicklurl.replace("native:","");
                                Small.openUri(clicklurl, getContext());
                            }else{
                                clicklurl = clicklurl.replace("h5:","");
                                if (clicklurl.startsWith("http:") ||
                                        clicklurl.startsWith("https:") ||
                                        clicklurl.startsWith("file:")) {
                                    url = EncodeUtils.urlEncode(clicklurl).toString();
                                } else {
                                    url = EncodeUtils.urlEncode(Config.WEB_SERVICE_URL + clicklurl).toString();
                                }
                                Small.openUri("app.webkit?url=" + url + "&title=" +messageDataTObject.getTitle(), getContext());
                            }
                        }else{
                            ToastDelegate.error(getContext(),"消息出错，请联系管理员！");
                        }
                    }
                }
            }
        });
    }
    public void initData() {
        if(init) {
            refreshRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    refreshRecyclerView.setRefreshing(true);
                    onDoRefresh();
                }
            });
        }
    }

    protected void onDoRefresh() {
        if (!isPullRefresh) {
            pageNo = PAGE_STARTING;
            isPullRefresh = true;
            getPresenter().loadMessages("1",pageNo,pageSize);
        }
    }

    protected void onDoNextPage() {
        if (!isPullRefresh) {
            pageNo = pageNo + pageSize;
            isPullRefresh = true;
            //自己的
            getPresenter().loadMessages("1",pageNo,pageSize);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getUserVisibleHint()){
            initData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            initData();
        }
    }

    @Override
    public void loadSuccess(List<MessageDataTObject> messages) {

        if(this.pageNo == PAGE_STARTING){
            messageDetailAdapter.resetList(messages);
        }else {
            messageDetailAdapter.addList(messages);
        }

        //判断是否存在下一页
        long i = CurrentMessage.getCurrentMessage().getCount("1");//总条数

        if (pageNo+messages.size()<i) {
            this.refreshRecyclerView.setLoadMoreEnable(true);
            messageDetailAdapter.notifyDataSetChanged();
        }else{
            this.refreshRecyclerView.setLoadMoreEnable(false);
        }

    }

    @Override
    public void completeLoading() {
        refreshRecyclerView.refreshComplete();
        isPullRefresh = false;
    }

    @Override
    protected MessageDetailPresenter createPresenter() {
        return new MessageDetailPresenter();
    }
}
