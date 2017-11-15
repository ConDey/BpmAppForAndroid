package com.eazytec.bpm.app.home.userhome;

import com.eazytec.bpm.app.home.data.NoticeListDataTObject;
import com.eazytec.bpm.app.home.data.app.AppIconDataTObject;
import com.eazytec.bpm.app.home.data.app.tobject.AppsDataTObject;
import com.eazytec.bpm.app.home.data.commonconfig.ImgDataTObject;
import com.eazytec.bpm.lib.common.CommonContract;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * @author Administrator
 * @version Id: UserHomeAppContract, v 0.1 2017/7/13 15:57 Administrator Exp $$
 */
public interface UserHomeAppContract {

    interface View extends CommonContract.CommonView {

        void loadAppsSuccess(AppsDataTObject appsDataTObject);

        void loadAllAppsSuccess(AppsDataTObject appsDataTObject);

        void getImgUrl(ImgDataTObject imgDataTObject);

        void loadSuccess(NoticeListDataTObject dataTObject);

        void loadIconSuccess(AppIconDataTObject appIconDataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadApps();

        void loadAllApps();

        void commonconfig();

        void loadNoticeList(int pageNo, int pageSize, String title);

        void loadAppIcon();
    }
}
