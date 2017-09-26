package com.eazytec.bpm.app.message;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazytec.bpm.app.message.fragmet.ReadMessageFragment;
import com.eazytec.bpm.app.message.fragmet.UnReadMessageFragment;
import com.eazytec.bpm.app.message.main.MessageMainContract;
import com.eazytec.bpm.app.message.main.MessageMainPresenter;
import com.eazytec.bpm.app.message.main.MessageTabAdapter;
import com.eazytec.bpm.appstub.view.recyclerview.RefreshRecyclerView;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.fragment.CommonFragment;
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
public class HomeTabMessageFragment extends ContractViewFragment<MessageMainPresenter> implements MessageMainContract.View{

    private TabLayout messageTablayout;
    private ViewPager messageViewpager;

    private List<String> mTitleLists;
    private List<CommonFragment> mFragmentLists;

    private MessageTabAdapter messageTabAdapter;

    private static final long FIVE_MINUTES_MILLS = 180000; //三分钟（毫秒）

    private boolean isfirst = true;  //第一次请求的时候，请求5天内的
    private boolean isforward = false;
    private boolean isrefresh = false;

    private List<MessageTopicDataTObject> datas;
    private PushAgent mPushAgent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.activity_message_tab_main, container, false);

        messageTablayout = (TabLayout)parentView.findViewById(R.id.activity_message_tab_main_tablayout);
        messageViewpager = (ViewPager) parentView.findViewById(R.id.activity_message_tab_main_tablayout_viewpager);

        initData();
        receivePushMessage();
        // 初始化
        return parentView;
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
                }
            }
        }
    }



    private void initData(){

        mTitleLists = new ArrayList<>();
        mTitleLists.add("未读");
        mTitleLists.add("已读");

        mFragmentLists = new ArrayList<>();

        messageViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                messageViewpager.requestLayout();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //两个fragment
        UnReadMessageFragment unReadMessageFragment = new UnReadMessageFragment();
        ReadMessageFragment readMessageFragment = new ReadMessageFragment();

        mFragmentLists.add(unReadMessageFragment);
        mFragmentLists.add(readMessageFragment);

        messageTabAdapter = new MessageTabAdapter(getChildFragmentManager(),mTitleLists,mFragmentLists);
        messageViewpager.setAdapter(messageTabAdapter);
        messageTablayout.setupWithViewPager(messageViewpager);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().loadTopics();  //一进去先请求一次
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void receivePushMessage() {
        mPushAgent = PushAgent.getInstance(getCommonActivity());
        UmengMessageHandler handler = new UmengMessageHandler() {

            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);

                // 发送一个广播告诉任务需要刷新了
                // 如果页面在前台那么我们就直接
                if (isforward) {
                    // 这里有个问题，会有竞争，会造成多次请求出现资源的浪费，如果要解决这个问题，要判断一下上次请求时间跟本次请求时间的间隔要大于1-2秒
                    // 如果有推送，就获取网络数据，更新数据库
                    loadTopicFromNetworks();
                } else {
                    CommonParams.getCommonParams().updateIsRefresh(CommonParams.IS_REFRESH_TRUE);
                }
            }
        };
        mPushAgent.setMessageHandler(handler);
    }


    private void loadTopicFromNetworks() {
        getPresenter().loadTopics();
    }

    @Override
    public void loadSuccess(List<MessageTopicDataTObject> data) {
        //要刷新两个Fragment

    }

    @Override
    protected MessageMainPresenter createPresenter() {
        return new MessageMainPresenter();
    }
}
