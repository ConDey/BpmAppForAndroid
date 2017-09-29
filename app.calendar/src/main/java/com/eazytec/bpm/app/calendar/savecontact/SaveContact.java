package com.eazytec.bpm.app.calendar.savecontact;


import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventTypeObject;
import com.eazytec.bpm.lib.common.CommonContract;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * Created by Vivi on 2017/9/28.
 */

public interface SaveContact {
    interface View extends CommonContract.CommonView{

        //修改工作
        void loadSuccess(EventDetailDataObject eventDetailDataObject);
        void postSuccess();
        //下拉列表
        void loadEventType(EventTypeObject eventTypeObject);
    }
    interface Presenter<T> extends CommonContract.CommonPresenter<T>{
        //保存
        void editDetail(String startTime,String startDate,String endTime,String endDate,String description,String location,String eventName,String eventType,String id);
        //从接口获取数据
        void loadSaveDetails(String eventId);
        //下拉列表类型
        void loadEventType();

    }
}
