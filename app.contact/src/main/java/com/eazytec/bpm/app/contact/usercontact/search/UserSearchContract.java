package com.eazytec.bpm.app.contact.usercontact.search;

import com.eazytec.bpm.app.contact.data.UsersDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * Created by Administrator on 2017/7/4.
 */

public class UserSearchContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(UsersDataTObject usersDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadUserDetail(String id);
    }
}
