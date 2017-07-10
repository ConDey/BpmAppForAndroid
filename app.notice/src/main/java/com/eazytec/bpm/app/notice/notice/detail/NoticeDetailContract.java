package com.eazytec.bpm.app.notice.notice.detail;

import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author Administrator
 * @version Id: NoticeDetailContract, v 0.1 2017/7/10 14:04 Administrator Exp $$
 */
public interface NoticeDetailContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(NoticeDetailDataTObject dataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadNoticeDetail(String id);
    }
}
