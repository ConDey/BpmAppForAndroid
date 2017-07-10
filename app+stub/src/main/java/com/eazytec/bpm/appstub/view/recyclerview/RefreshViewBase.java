package com.eazytec.bpm.appstub.view.recyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Administrator
 * @version Id: RefreshViewBase, v 0.1 2017/7/10 9:04 Administrator Exp $$
 */
public abstract class RefreshViewBase<T extends View> extends SwipeRefreshLayout {

    /* 下拉刷新是否有用*/
    private boolean isRefreshEnable = true;
    protected T mRefreshView;

    public RefreshViewBase(Context context) {
        super(context);
        initView();
    }

    public RefreshViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mRefreshView = getRefreshView();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mRefreshView, params);
    }

    public void setRefreshEnable(boolean isRefreshEnable) {
        this.isRefreshEnable = isRefreshEnable;
        setEnabled(isRefreshEnable);
    }

    public void refreshComplete() {
        setRefreshing(false);
    }

    protected abstract T getRefreshView();
}

