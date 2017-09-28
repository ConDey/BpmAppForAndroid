package com.eazytec.bpm.app.calendar.contact;

import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * Created by Vivi on 2017/9/27.
 */

public class ItemListContract {
    public interface View extends CommonContract.CommonView {

        void loadItemList(EventListDataObject eventListDataObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadItemById(String datas);
    }
}
