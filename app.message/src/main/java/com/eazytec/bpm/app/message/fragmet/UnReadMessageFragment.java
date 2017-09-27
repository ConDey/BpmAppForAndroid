package com.eazytec.bpm.app.message.fragmet;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.eazytec.bpm.app.message.R;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.eazytec.bpm.lib.common.message.CurrentMessage;
import com.eazytec.bpm.lib.common.message.commonparams.CommonParams;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;
import com.eazytec.bpm.lib.utils.EncodeUtils;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import net.wequick.small.Small;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 未读
 * @author Beckett_W
 * @version Id: UnReadMessageFragment, v 0.1 2017/9/26 14:00 Beckett_W Exp $$
 */
public class UnReadMessageFragment extends ContractViewFragment<MessagePresenter> implements AbsListView.OnScrollListener, MessageContract.View{

    private boolean isfirst = true;
    private boolean isforward = false;

    private static final int PAGE_STARTING = 0; // 代表起始页

    private ListView mListView;
    private MessageAdapter messageDetailAdapter;
    private ProgressBar loadInfo;
    private LinearLayout loadLayout;
    private int firstItem = -1;

    // 分页部分
    private int pageNo;
    private int pageSize = 10;
    private boolean isPullRefresh = false;
    private boolean canRefresh = true;

    //空view
    private LinearLayout emptyLinearLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_message_list, container, false);

        emptyLinearLayout = (LinearLayout) parentView.findViewById(R.id.fragment_message_detail_list_empty_view);
        mListView = (ListView) parentView.findViewById(R.id.fragment_message_listview);
        // 初始化

        initData();
        setListener();
        return parentView;
    }

    private void initData() {

        // 创建线性布局来显示正在加载
        loadLayout = new LinearLayout(getContext());
        loadLayout.setGravity(Gravity.CENTER);
        // 定义一个progressbar表示正在加载
        loadInfo = new ProgressBar(getContext(), null, R.drawable.rv_load_progress_round);
        // 增加组件
        loadLayout.addView(loadInfo, new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        // 增加到listview头部
        mListView.addHeaderView(loadLayout);
        mListView.setOnScrollListener(this);

        messageDetailAdapter = new MessageAdapter(getContext());
        mListView.setAdapter(messageDetailAdapter);
        messageDetailAdapter.notifyDataSetChanged();
    }

    private void setListener() {

        mListView.post(new Runnable() {
            @Override
            public void run() {
                // 第一次请求
                pageNo = PAGE_STARTING;
                onDoRefresh();
            }
        });

        //点击刷新
        emptyLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setVisibility(View.VISIBLE);
                emptyLinearLayout.setVisibility(View.GONE);
                onDoRefresh();
            }
        });

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDataTObject messageDataTObject;
                if (canRefresh) {
                    messageDataTObject = (MessageDataTObject) messageDetailAdapter.getDatas().get(position-1);
                }else {
                    messageDataTObject = (MessageDataTObject) messageDetailAdapter.getDatas().get(position);
                }
                if (messageDataTObject != null) {
                    if (messageDataTObject.isCanClick()) {
                           if(!messageDataTObject.getIsRead()){
                            CurrentMessage.getCurrentMessage().upDateMessageIsReadState(messageDataTObject.getTopicId(),messageDataTObject.getId());
                        //要给接口传已读，暂时没有这个接口，传送已读
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

    @Override
    public void onPause() {
        super.onPause();
        isforward = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isforward = true;
        if (isfirst) {
            isfirst = false;
        } else {
            getPresenter().refreshMessages("0", 0, pageSize);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onScroll(AbsListView absView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scorllState) {
        if (firstItem == 0 && canRefresh && scorllState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {// 不再滚动
            onDoNextPage();
        }
    }

    protected void onDoRefresh() {
        if (!isPullRefresh) {
            pageNo = PAGE_STARTING;
            isPullRefresh = true;
            getPresenter().loadMessages("0", pageNo, pageSize);
        }
    }

    protected void onDoNextPage() {
        if (!isPullRefresh) {
            pageNo = pageNo + pageSize;
            isPullRefresh = true;
            getPresenter().loadMessages("0", pageNo, pageSize);
        }
    }

    @Override
    public void loadTopicSuccess(List<MessageTopicDataTObject> data) {
       getPresenter().refreshMessages("0", 0, pageSize);
    }

    public void loadSuccess(List<MessageDataTObject> messages) {
        messageDetailAdapter.addList(messages);
        if (messages.size() < pageSize) {
            mListView.removeHeaderView(loadLayout);
            canRefresh = false;
        }
        messageDetailAdapter.notifyDataSetChanged();
        mListView.setSelectionFromTop(messages.size(), 0);

    }

    @Override
    public void refreshSuccess(List<MessageDataTObject> messages) {
        //把数据清掉，然后重新加载一遍最新的消息
        messageDetailAdapter.resetList(messages);
        if (messages.size() < pageSize) {
            mListView.removeHeaderView(loadLayout);
            canRefresh = false;
        }
        messageDetailAdapter.notifyDataSetChanged();
        mListView.setSelectionFromTop(messages.size(), 0);
    }

    @Override
    public void completeLoading() {
        isPullRefresh = false;
    }

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter();
    }

}
