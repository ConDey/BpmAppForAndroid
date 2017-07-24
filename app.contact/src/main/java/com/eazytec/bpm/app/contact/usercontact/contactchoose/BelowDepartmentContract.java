package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author Administrator
 * @version Id: BelowDepartmentContract, v 0.1 2017/7/24 13:09 Administrator Exp $$
 */
public interface BelowDepartmentContract {
    interface View extends CommonContract.CommonView {

        void loadDepSuccess(DepartmentDataTObject departmentDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadDep(String id);
    }
}
