package com.eazytec.bpm.app.notice.notice.list;

import com.eazytec.bpm.app.notice.data.NoticeListDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author Administrator
 * @version Id: NoticeListContract, v 0.1 2017/7/10 9:36 Administrator Exp $$
 */
public interface NoticeListContract {
    interface View extends CommonContract.CommonView {

        void loadSuccess(NoticeListDataTObject dataTObject);
        //完成加载
        void completeLoading();

    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadNoticeList(int pageNo, int pageSize, String title);

    }
}
