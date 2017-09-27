package com.eazytec.bpm.lib.common.message;

import com.eazytec.bpm.appstub.db.DBConstants;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 16735
 * @version Id: CurrentMessage, v 0.1 2017-8-25 9:40 16735 Exp $$
 */
public class CurrentMessage {

    private static CurrentMessage currentMessage;

    private Build build;

    private CurrentMessage(Build build) {
        this.build = build;
    }

    /**
     *  获取单例对象
     */
    public static CurrentMessage getCurrentMessage() {
        if (currentMessage == null) {
            currentMessage = new CurrentMessage.Build().messageRepository(new DefaultMessageRepository(DBConstants.getBriteDatabase())).build();
        }
        return currentMessage;
    }

    /**
     *  存入新的message到数据库中
     *
     * @param messages
     */
    public void saveMessagesIntoDB(List<MessageDataTObject> messages) {
        if (this.build.messageRepository != null) {
            this.build.messageRepository.insertMessageIntoDB(messages);
        }
    }

    public List<MessageDataTObject> getMessagesFromDB(String topicId) {
        if (this.build.messageRepository != null) {
            return this.build.messageRepository.selectMessageFromDB(topicId);
        }
        // 返还一个空数组
        return new ArrayList<>();
    }

    public List<MessageDataTObject> getMessagesByPage(String  topicId, int pageIndex, int pageSize) {
        if (this.build.messageRepository != null) {
            return this.build.messageRepository.selectMessageByPage(topicId, pageIndex, pageSize);
        }
        // 返还一个空数组
        return new ArrayList<>();
    }

    /**
     * 更新消息的已读状态信息
     *
     * @param topicId
     */
    public void upDateMessageIsReadState(String topicId ,String id) {
        if (this.build.messageRepository != null) {
            this.build.messageRepository.updateMessageIsReadState(topicId ,id);
        }
    }

    public static class Build {

        private MessageRepository messageRepository;

        public Build messageRepository(MessageRepository messageRepository) {
            this.messageRepository = messageRepository;
            return this;
        }

        public CurrentMessage build() {
            return new CurrentMessage(this);
        }
    }
}
