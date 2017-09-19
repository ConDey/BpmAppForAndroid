package com.eazytec.bpm.app.message.detail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eazytec.bpm.app.message.MessageConstant;
import com.eazytec.bpm.app.message.R;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.common.message.CurrentMessage;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.util.List;

/**
 * @author Beckett_W
 * @version Id: MessageDetailActivity, v 0.1 2017/9/18 14:59 Beckett_W Exp $$
 */
public class MessageDetailActivity extends ContractViewActivity<MessageDetailPresenter> implements AbsListView.OnScrollListener, MessageDetailContract.View {

    private static final int PAGE_STARTING = 0; // 代表起始页

    private Toolbar toolbar;
    private TextView titleTv;
    private TextView subTitleTv;

    private ListView mListView;
    private MessageDetailAdapter messageDetailAdapter;
    private ProgressBar loadInfo;
    private LinearLayout loadLayout;
    private int firstItem = -1;

    private String topicId;
    private String topicName;
    private String topicType;

    // 分页部分
    private int pageNo;
    private int pageSize = 10;
    private boolean isPullRefresh = false;
    private boolean canRefresh = true;

    //空view
    private LinearLayout emptyLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        toolbar = (Toolbar) findViewById(R.id.message_detail_toolbar);
        titleTv = (TextView) findViewById(R.id.message_detail_toolbar_title);
        subTitleTv = (TextView) findViewById(R.id.message_detail_toolbar_sub_title);
        emptyLinearLayout = (LinearLayout) findViewById(R.id.message_detail_list_empty_view);
        mListView = (ListView) findViewById(R.id.message_detail_listview);

        initData();
        setListener();

    }

    private void initData() {
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        topicId = getIntent().getStringExtra(MessageConstant.TOPIC_ID);
        topicName = getIntent().getStringExtra(MessageConstant.TOPIC_NAME);
        topicType = getIntent().getStringExtra(MessageConstant.TOPIC_TYPE);

        if (!StringUtils.isEmpty(topicName)) {
            titleTv.setText(topicName);
        }
        if (!StringUtils.isEmpty(topicType)) {
            subTitleTv.setText(topicType);
        }else{
            subTitleTv.setVisibility(View.GONE);  //和易工作不一样，可能没有副标题
        }

        // 创建线性布局来显示正在加载
        loadLayout = new LinearLayout(this);
        loadLayout.setGravity(Gravity.CENTER);
        // 定义一个progressbar表示正在加载
        loadInfo = new ProgressBar(this, null, R.drawable.rv_load_progress_round);
        // 增加组件
        loadLayout.addView(loadInfo, new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        // 增加到listview头部
        mListView.addHeaderView(loadLayout);
        mListView.setOnScrollListener(this);

        messageDetailAdapter = new MessageDetailAdapter(getContext());
        mListView.setAdapter(messageDetailAdapter);
        messageDetailAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                        //未读需要去数据库更为已读
                        if(!messageDataTObject.getIsRead()){
                        CurrentMessage.getCurrentMessage().upDateMessageIsReadState(topicId,messageDataTObject.getId());
                        //要给接口传已读，暂时没有这个接口
                        }
                        //根据url跳转
                        String url = messageDataTObject.getClickUrl();



                    }
                }
            }
        });
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
            getPresenter().loadMessages(topicId, pageNo, pageSize);
        }
    }

    protected void onDoNextPage() {
        if (!isPullRefresh) {
            pageNo = pageNo + pageSize;
            isPullRefresh = true;
            getPresenter().loadMessages(topicId, pageNo, pageSize);
        }
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
    public void completeLoading() {
        isPullRefresh = false;
    }

    @Override
    protected MessageDetailPresenter createPresenter() {
        return new MessageDetailPresenter();
    }
}
