package com.eazytec.bpm.app.message.detail;


import com.eazytec.bpm.lib.common.CommonContract;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;

import java.util.List;

/**
 * @author 16735
 * @version Id: MessageDetailContract, v 0.1 2017-8-25 16:34 16735 Exp $$
 */
public class MessageDetailContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(List<MessageDataTObject> messages);

        //完成加载
        void completeLoading();
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadMessages(String topicId, int pageNo, int pageSize);

        void setReaded(String id);
    }
}
