package com.eazytec.bpm.app.message.main;

import com.eazytec.bpm.lib.common.CommonContract;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;

import java.util.List;

/**
 * @author Beckett_W
 * @version Id: MessageMainContract, v 0.1 2017/9/18 13:07 Beckett_W Exp $$
 */
public interface MessageMainContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(List<MessageTopicDataTObject> data);

       // void loadSuccessFromDB(List<MessageTopicDataTObject> data);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {
        void loadTopics();

       // void loadTopicsByDB();
    }
}
