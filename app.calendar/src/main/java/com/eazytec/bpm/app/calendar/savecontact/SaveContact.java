package com.eazytec.bpm.app.calendar.savecontact;


import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventTypeObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * Created by Vivi on 2017/9/28.
 */

public interface SaveContact {
    interface View extends CommonContract.CommonView{

        //修改工作
        void loadSuccess(EventDetailDataObject eventDetailDataObject);
        void postSuccess();
    }
    interface Presenter<T> extends CommonContract.CommonPresenter<T>{
        void editDetail(String startTime,String startDate,String endTime,String endDate,String description,String location,String eventName,String eventType,String id);

        void loadSaveDetails(String eventId);

    }
}
