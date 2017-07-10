package com.eazytec.bpm.appstub.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Administrator
 * @version Id: RefreshViewHolder, v 0.1 2017/7/10 9:01 Administrator Exp $$
 */
public class RefreshViewHolder extends RecyclerView.ViewHolder {

    public final int mType;
    public RecyclerView.ViewHolder mViewHolder;

    public RefreshViewHolder(View itemView, int type) {
        super(itemView);
        this.mType = type;
    }
}
