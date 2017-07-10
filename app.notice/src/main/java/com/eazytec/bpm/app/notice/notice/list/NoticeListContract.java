package com.eazytec.bpm.app.notice.notice.list;

import com.eazytec.bpm.app.notice.data.NoticeListDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author Administrator
 * @version Id: NoticeListContract, v 0.1 2017/7/10 9:36 Administrator Exp $$
 */
public class NoticeListContract {
    interface View extends CommonContract.CommonView {

        void loadSuccess(NoticeListDataTObject dataTObject);

        /**
         * 完成ListView的Loading动作，有时候因为一些奇怪的原因导致加载数据失败，这时候Loading动作
         * 应该会被正常的终止
         */
        void completeLoading();
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadNoticeList(int pageNo, int pageSize, String title);
    }
}
