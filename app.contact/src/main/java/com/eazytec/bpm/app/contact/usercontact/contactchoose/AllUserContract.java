package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import com.eazytec.bpm.app.contact.data.UsersDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author Administrator
 * @version Id: AllUserContract, v 0.1 2017/7/25 14:51 Administrator Exp $$
 */
public interface AllUserContract {
    interface View extends CommonContract.CommonView {

        void loadSuccess(UsersDataTObject usersDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadUserDetail(String id);
    }
}
