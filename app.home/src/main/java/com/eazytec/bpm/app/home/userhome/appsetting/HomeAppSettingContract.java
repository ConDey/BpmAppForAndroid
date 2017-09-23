package com.eazytec.bpm.app.home.userhome.appsetting;

import com.eazytec.bpm.app.home.data.app.tobject.AppsDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;

/**
 * @author Beckett_W
 * @version Id: HomeAppSettingContract, v 0.1 2017/9/21 10:01 Beckett_W Exp $$
 */
public interface HomeAppSettingContract {

    interface View extends CommonContract.CommonView {

        void loadAppsSuccess(AppsDataTObject appsDataTObject);

        void loadAllAppsSuccess(AppsDataTObject appsDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {
        void loadApps();

        void loadAllApps();

        void orderMenu(String id,String id2);

        void setCommonUse(String id);

        void cancelCommonUse(String id);
    }
}
