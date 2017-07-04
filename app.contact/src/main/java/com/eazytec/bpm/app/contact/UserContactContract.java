package com.eazytec.bpm.app.contact;

import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * Created by Administrator on 2017/7/3.
 */

public interface UserContactContract {

    interface View extends CommonContract.CommonView {

        void loadDepSuccess(DepartmentDataTObject departmentDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadDepSuccess();
    }

}
