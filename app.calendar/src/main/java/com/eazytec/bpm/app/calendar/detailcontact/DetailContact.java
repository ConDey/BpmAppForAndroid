package com.eazytec.bpm.app.calendar.detailcontact;

import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * Created by Vivi on 2017/9/28.
 */

public interface DetailContact {

    interface View extends CommonContract.CommonView{
        void loadSuccess(EventDetailDataObject eventDetailDataObject);

        void deleteSuccess();
    }
    interface Presenter<T> extends CommonContract.CommonPresenter<T> {
        void loadDetail(String deId);

        void deleteDetail(String deId);
    }
}
