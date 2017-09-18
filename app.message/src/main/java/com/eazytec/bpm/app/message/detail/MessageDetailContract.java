package com.eazytec.bpm.app.message.detail;

import com.eazytec.bpm.lib.common.CommonContract;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;

import java.util.List;

/**
 * @author Beckett_W
 * @version Id: MessageDetailContract, v 0.1 2017/9/18 15:00 Beckett_W Exp $$
 */
public interface MessageDetailContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(List<MessageDataTObject> messages);

        //完成加载
        void completeLoading();
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {
        void loadMessages(String topicId, int pageNo, int pageSize);
    }
}
