package com.eazytec.bpm.app.message;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazytec.bpm.app.message.detail.MessageDetailActivity;
import com.eazytec.bpm.app.message.main.MessageMainAdapter;
import com.eazytec.bpm.app.message.main.MessageMainContract;
import com.eazytec.bpm.app.message.main.MessageMainPresenter;
import com.eazytec.bpm.app.message.utils.RefreshRecyclerViewUtil;
import com.eazytec.bpm.appstub.view.recyclerview.OnRecyclerViewItemClickListener;
import com.eazytec.bpm.appstub.view.recyclerview.RefreshRecyclerView;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.eazytec.bpm.lib.common.message.commonparams.CommonParams;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息改为tab样式！
 * 每几分钟，或者是有推送，更新数据库
 * @author Beckett_W
 * @version Id: HomeTabMessageFragment, v 0.1 2017/9/26 13:29 Beckett_W Exp $$
 */
public class HomeTabMessageFragment extends ContractViewFragment<MessageMainPresenter> implements MessageMainContract.View,OnRecyclerViewItemClickListener {

    private static final long FIVE_MINUTES_MILLS = 180000; //三分钟（毫秒）

    private boolean isfirst = true;
    private boolean isforward = false;
    private boolean isrefresh = false;

    private RefreshRecyclerView messageMainListView;
    private MessageMainAdapter messageMainAdapter;

    private List<MessageTopicDataTObject> datas;
    private PushAgent mPushAgent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.activity_message_main, container, false);

        // 初始化
        messageMainListView = (RefreshRecyclerView) parentView.findViewById(R.id.message_main_listview);

        initData();
        setListener();
        receivePushMessage();

        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().loadTopicsByDB();  //初次进入先请求一次
    }

    private void initData() {
        datas = new ArrayList<>();

        messageMainAdapter = new MessageMainAdapter(getContext());
        messageMainListView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageMainAdapter.resetList(datas);
        messageMainListView.setRefreshEnable(true);
        messageMainListView.setRefreshing(true);
        messageMainListView.setAdapter(messageMainAdapter);
        messageMainAdapter.setListener(this);
        messageMainAdapter.notifyDataSetChanged();
        RefreshRecyclerViewUtil.initRefreshViewColorSchemeColors(messageMainListView,getResources());


    }

    private void setListener() {
        messageMainListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messageMainListView.refreshComplete();
                    }
                },1000); // 默认刷新一秒
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
            long lastRequestTime = Long.valueOf(CurrentUser.getCurrentUser().getLastRequestTime(false));
            if (CommonParams.getCommonParams().getIsRefresh().equals(CommonParams.IS_REFRESH_TRUE)) {
                CommonParams.getCommonParams().updateIsRefresh(CommonParams.IS_REFRESH_FALSE);
                loadTopicFromNetworks();
            } else {
                if ((TimeUtils.getNowMills() - lastRequestTime) > FIVE_MINUTES_MILLS) {
                    loadTopicFromNetworks();
                }else {
                      getPresenter().loadTopicsByDB();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void receivePushMessage() {
        mPushAgent = PushAgent.getInstance(getContext());
        UmengMessageHandler handler = new UmengMessageHandler() {

            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);

                // 发送一个广播告诉任务需要刷新了
                // 如果页面在前台那么我们就直接
                if (isforward) {
                    // 这里有个问题，会有竞争，会造成多次请求出现资源的浪费，如果要解决这个问题，要判断一下上次请求时间跟本次请求时间的间隔要大于1-2秒
                    // 暂时先不考虑这个问题，
                    loadTopicFromNetworks();
                } else {
                    CommonParams.getCommonParams().updateIsRefresh(CommonParams.IS_REFRESH_TRUE);
                }
            }
        };
        mPushAgent.setMessageHandler(handler);
    }



    @Override
    protected MessageMainPresenter createPresenter() {
        return new MessageMainPresenter();
    }

    @Override
    public void onItemClick(View view, Object data) {
        //跳到消息详情页面
        MessageTopicDataTObject dataTObject = (MessageTopicDataTObject)data;

        Bundle it = new Bundle();
        it.putString(MessageConstant.TOPIC_ID, dataTObject.getId());
        it.putString(MessageConstant.TOPIC_NAME, dataTObject.getName());
        it.putString(MessageConstant.TOPIC_TYPE, dataTObject.getTopic());
        getCommonActivity().startActivity(getCommonActivity(), MessageDetailActivity.class,it);
    }

    @Override
    public void loadSuccess(List<MessageTopicDataTObject> data) {
        messageMainListView.refreshComplete();
        datas = data;
        messageMainAdapter.resetList(datas);
        messageMainAdapter.notifyDataSetChanged();
    }

     @Override
     public void loadSuccessFromDB(List<MessageTopicDataTObject> data) {
     datas = data;
     messageMainAdapter.resetList(datas);
     messageMainAdapter.notifyDataSetChanged();
     Handler handler = new Handler();
     handler.postDelayed(new Runnable() {
     @Override
     public void run() {
     loadTopicFromNetworks();
     }
     }, 1000);//1秒后执行Runnable中的run方法
     }

    private void loadTopicFromNetworks() {
        getPresenter().loadTopics();
    }
}
