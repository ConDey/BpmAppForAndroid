package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author Administrator
 * @version Id: TopDepartmentContract, v 0.1 2017/7/24 13:08 Administrator Exp $$
 */
public interface TopDepartmentContract {
    interface View extends CommonContract.CommonView {

        void loadDepSuccess(DepartmentDataTObject departmentDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadDepSuccess();
    }
}
