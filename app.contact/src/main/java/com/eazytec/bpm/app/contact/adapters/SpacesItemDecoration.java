package com.eazytec.bpm.app.contact.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 用来设置横向的recyclerview的item间距的
 * @author Beckett_W
 * @version Id: SpacesItemDecoration, v 0.1 2017/7/25 13:35 Administrator Exp $$
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        /** 这个会让第一个item下沉
        if(parent.getChildPosition(view) == 0)
            outRect.top = space;
         **/
    }
}

