package com.eazytec.bpm.lib.common.message;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;

import java.util.List;

/**
 * @author 16735
 * @version Id: MessageRepository, v 0.1 2017-8-25 9:40 16735 Exp $$
 */
public interface MessageRepository {

    /**
     * 存入消息到数据库中
     *
     * @param messages
     */
    public void insertMessageIntoDB(List<MessageDataTObject> messages);

    /**
     * 获取所有消息
     *
     * @return
     */
    public List<MessageDataTObject> selectMessageFromDB(String topicId);

    /**
     * 更新消息已读状态
     *
     * @param topicId
     */
    public void updateMessageIsReadState(String topicId, String id);

    /**
     * 分页查询消息
     *
     */
    public List<MessageDataTObject> selectMessageByPage(String isRead, int pageIndex, int pageSize);

    /**
     * 得到总条数
     */
      public  long getCount(String isRead);

    /**
     * 删除某一条消息
     *
     * @param topicId
     */
    public int deleteMessageById(String topicId, String id);
}
