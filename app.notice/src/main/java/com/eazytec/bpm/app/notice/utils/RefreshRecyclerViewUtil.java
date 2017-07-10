package com.eazytec.bpm.app.notice.utils;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;

import com.eazytec.bpm.app.notice.R;
import com.eazytec.bpm.lib.utils.AppUtils;

/**
 * @author Administrator
 * @version Id: RefreshRecyclerViewUtil, v 0.1 2017/7/10 10:16 Administrator Exp $$
 */
public class RefreshRecyclerViewUtil {
    @TargetApi(23)
    public static void initRefreshViewColorSchemeColors(SwipeRefreshLayout refreshRecyclerView, Resources resources) {
        if (AppUtils.getVersionSDK() >= 23) {
            refreshRecyclerView.setColorSchemeColors(resources.getColor(R.color.light_blue_100, null),
                    resources.getColor(R.color.light_blue_400, null),
                    resources.getColor(R.color.color_primary, null));
        } else {
            refreshRecyclerView.setColorSchemeColors(resources.getColor(R.color.light_blue_100),
                    resources.getColor(R.color.light_blue_400),
                    resources.getColor(R.color.color_primary));
        }
    }
}
