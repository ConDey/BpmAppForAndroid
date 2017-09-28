package com.eazytec.bpm.app.message.fragmet;

import com.eazytec.bpm.lib.common.CommonContract;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;

import java.util.List;

/**
 * @author Beckett_W
 * @version Id: MessageContract, v 0.1 2017/9/26 13:42 Beckett_W Exp $$
 */
public interface MessageContract {
    interface View extends CommonContract.CommonView {

        void loadTopicSuccess(List<MessageTopicDataTObject> data);

        void loadSuccess(List<MessageDataTObject> messages);

        void refreshSuccess(List<MessageDataTObject> messages);

        //完成加载
        void completeLoading();
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadTopics();

        void loadMessages(String isRead, int pageNo, int pageSize);

        void refreshMessages(String isRead, int pageNo, int pageSize);

        void setReaded(String id);
    }
}
