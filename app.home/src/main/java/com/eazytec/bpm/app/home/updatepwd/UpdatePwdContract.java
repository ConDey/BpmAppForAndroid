package com.eazytec.bpm.app.home.updatepwd;

import com.eazytec.bpm.lib.common.CommonContract;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * @author Administrator
 * @version Id: UpdatePwdContract, v 0.1 2017/7/13 15:57 Administrator Exp $$
 */
public interface UpdatePwdContract {

    interface View extends CommonContract.CommonView {

        void updatePwdSuccess(WebDataTObject updateData);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void updatePwd(String originPwd, String newPwd, String confirmPwd);
    }
}
