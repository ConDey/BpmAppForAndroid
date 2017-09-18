package com.eazytec.bpm.lib.common.message.topic;

import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;

import java.util.List;

/**
 * @author 16735
 * @version Id: TopicRepository, v 0.1 2017-8-25 8:26 16735 Exp $$
 */
public interface TopicRepository {

    /**
     * 存入topic到数据库中
     *
     * @param topics
     */
    public void insertTopicIntoDB(List<MessageTopicDataTObject> topics);

    /**
     * 获取所有主题
     *
     * @return
     */
    public List<MessageTopicDataTObject> selectTopicFromDB();

    /**
     * 更新topic里收到最新message的时间
     *
     * @param latestTime
     */
    public void updateLatestMessageTime(String topicId, String latestTime);
}
