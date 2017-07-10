package com.eazytec.bpm.app.notice.notice.list;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.eazytec.bpm.app.notice.R;
import com.eazytec.bpm.app.notice.adapter.NoticeListAdapter;
import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.app.notice.data.NoticeListDataTObject;
import com.eazytec.bpm.app.notice.notice.detail.NoticeDetailActivity;
import com.eazytec.bpm.app.notice.utils.RefreshRecyclerViewUtil;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.recyclerview.OnNextPageListener;
import com.eazytec.bpm.appstub.view.recyclerview.OnRecyclerViewItemClickListener;
import com.eazytec.bpm.appstub.view.recyclerview.RefreshRecyclerView;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.util.List;

public class NoticeListActivity extends ContractViewActivity<NoticeListPresenter> implements NoticeListContract.View , OnRecyclerViewItemClickListener{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private static final int PAGE_STARTING = 1; // 代表起始页

    private int pageNo;
    private int pagesize = 10;
    private boolean isPullRefresh = false;

    protected RefreshRecyclerView refreshRecyclerView;
    private NoticeListAdapter noticeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("通知公告");

        refreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.notice_list_recycle_view);
        refreshRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noticeListAdapter = new NoticeListAdapter(getContext());
        refreshRecyclerView.setAdapter(noticeListAdapter);
        refreshRecyclerView.setRefreshEnable(true);
        noticeListAdapter.setListener(this);
        RefreshRecyclerViewUtil.initRefreshViewColorSchemeColors(refreshRecyclerView,getResources());

        //刷新监听
        refreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NoticeListActivity.this.onRefresh();
            }
        });
        //请求下一页监听
        refreshRecyclerView.setOnNextPageListener(new OnNextPageListener() {
            @Override
            public void onNextPage() {
                NoticeListActivity.this.onNextPage();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoticeListActivity.this.finish();
            }
        });

        refreshRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                    refreshRecyclerView.setRefreshing(true);
                    NoticeListActivity.this.onRefresh();
            }
        });
    }


    protected void onRefresh() {
        if (!isPullRefresh) {
            pageNo = PAGE_STARTING;
            isPullRefresh = true;
            getPresenter().loadNoticeList(pageNo, pagesize,  StringUtils.blank());
        }
    }

    protected void onNextPage() {
        if (!isPullRefresh) {
            pageNo++;
            isPullRefresh = true;
            getPresenter().loadNoticeList(pageNo, pagesize, StringUtils.blank());
        }
    }


    @Override
    protected NoticeListPresenter createPresenter() {
        return new NoticeListPresenter();
    }

    @Override
    public void loadSuccess(NoticeListDataTObject dataTObject) {
        if (dataTObject.getTotalPages() <= 0) {
            ToastDelegate.info(getContext(),"暂无相关公告");
            return;
        }

        this.pageNo = dataTObject.getPageNo();

        if (this.pageNo == PAGE_STARTING) {
            noticeListAdapter.resetList(dataTObject.getDatas());
        } else {
            noticeListAdapter.addList(dataTObject.getDatas());
        }

        // 判断是否有下一页的存在
        if (dataTObject.hasNextPage()) {
            this.refreshRecyclerView.setLoadMoreEnable(true);
        } else {
            this.refreshRecyclerView.setLoadMoreEnable(false);
        }
        noticeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void completeLoading() {
        refreshRecyclerView.refreshComplete();
        isPullRefresh = false;
    }

    @Override
    public void onItemClick(View view, Object data) {
        NoticeDetailDataTObject dataTObject = (NoticeDetailDataTObject) data;

        Bundle bundle = new Bundle();
        bundle.putString("id", dataTObject.getId());
        startActivity(this, NoticeDetailActivity.class, bundle);
    }
}
