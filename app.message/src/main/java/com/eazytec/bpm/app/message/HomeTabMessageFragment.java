package com.eazytec.bpm.app.message;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazytec.bpm.app.message.main.MessageMainContract;
import com.eazytec.bpm.app.message.main.MessageMainPresenter;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.eazytec.bpm.lib.common.message.commonparams.CommonParams;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import java.util.List;

/**
 * 消息改为tab样式！
 * @author Beckett_W
 * @version Id: HomeTabMessageFragment, v 0.1 2017/9/26 13:29 Beckett_W Exp $$
 */
public class HomeTabMessageFragment extends ContractViewFragment<MessageMainPresenter> implements MessageMainContract.View{

    private static final long FIVE_MINUTES_MILLS = 300000; //五分钟（毫秒）

    private boolean isfirst = true;
    private boolean isforward = false;
    private boolean isrefresh = false;


    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MessageMainTabAdapter messageMainTabAdapter;

    private PushAgent mPushAgent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.activity_message_main, container, false);

        // 初始化
        tabLayout = (TabLayout) parentView.findViewById(R.id.message_main_tab_layout);
        viewPager = (ViewPager) parentView.findViewById(R.id.message_main_viewpager);

        initData();

        receivePushMessage();

        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().loadTopicsByDB();  //初次进入先请求一次
    }

    private void initData() {
        messageMainTabAdapter = new MessageMainTabAdapter(getFragmentManager(),getContext());
        viewPager.setAdapter(messageMainTabAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
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
    public void loadSuccess(List<MessageTopicDataTObject> data) {
        //有推送，刷新viewpager，更新两个fragmnet的数据
        messageMainTabAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadSuccessFromDB(List<MessageTopicDataTObject> data) {

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
