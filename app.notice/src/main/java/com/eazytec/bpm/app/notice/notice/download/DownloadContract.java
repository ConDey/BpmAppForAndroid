package com.eazytec.bpm.app.notice.notice.download;

import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author Administrator
 * @version Id: DownloadContract, v 0.1 2017/7/12 12:25 Administrator Exp $$
 */
public interface DownloadContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(NoticeDetailDataTObject dataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadNoticeDetail(String id);
    }
}
