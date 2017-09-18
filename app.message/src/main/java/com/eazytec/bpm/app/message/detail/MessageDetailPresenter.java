package com.eazytec.bpm.app.message.detail;

import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.message.CurrentMessage;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;

import java.util.List;

/**
 * @author Beckett_W
 * @version Id: MessageDetailPresenter, v 0.1 2017/9/18 15:00 Beckett_W Exp $$
 */
public class MessageDetailPresenter extends RxPresenter<MessageDetailContract.View> implements MessageDetailContract.Presenter<MessageDetailContract.View> {

    /**
     * 从数据库中取出message
     */
    @Override
    public void loadMessages(String topicId, int pageNo, int pageSize) {
        List<MessageDataTObject> messages = CurrentMessage.getCurrentMessage().getMessagesByPage(topicId, pageNo, pageSize);
        if (messages != null) {
            mView.loadSuccess(messages);
        }
        mView.completeLoading();
    }
}
