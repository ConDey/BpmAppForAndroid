package com.eazytec.bpm.app.contact.usercontact.userdetail;

import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * Created by Administrator on 2017/7/4.
 */

public interface UserDetailContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(UserDetailDataTObject userDetailDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadUserDetail(String id);
    }
}
