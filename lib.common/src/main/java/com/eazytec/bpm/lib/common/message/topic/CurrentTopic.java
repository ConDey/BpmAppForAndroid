package com.eazytec.bpm.lib.common.message.topic;


import com.eazytec.bpm.appstub.db.DBConstants;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 16735
 * @version Id: CurrentTopic, v 0.1 2017-8-25 8:25 16735 Exp $$
 */
public class CurrentTopic {

    private static CurrentTopic currentTopic;

    private Build build;

    private CurrentTopic(Build build) {
        this.build = build;
    }

    /**
     * 获得单例对象
     */
    public static CurrentTopic getCurrentTopic() {
        if (currentTopic == null) {
            currentTopic = new CurrentTopic.Build().topicRepository(new DefaultTopicRepository(DBConstants.getBriteDatabase())).build();
        }
        return currentTopic;
    }

    /**
     * 保存Topic到数据库中
     *
     * @param topics
     */
    public void saveTopicIntoDB(List<MessageTopicDataTObject> topics) {
        if (this.build.topicRepository != null) {
            this.build.topicRepository.insertTopicIntoDB(topics);
        }
    }

    /**
     * 获取所有topic
     *
     * @return
     */
    public List<MessageTopicDataTObject> getTopicFromDB() {
        if (this.build.topicRepository != null) {
            return this.build.topicRepository.selectTopicFromDB();
        }
        // 返还一个空数组
        return new ArrayList<>();
    }

    /**
     * 更新topic里最新的时间
     *
     * @param topicId
     * @param latestTime
     */
    public void updateLatestTime(String topicId, String latestTime) {
        if (this.build.topicRepository != null) {
            this.build.topicRepository.updateLatestMessageTime(topicId, latestTime);
        }
    }

    public static class Build {

        private TopicRepository topicRepository;

        public Build topicRepository(TopicRepository topicRepository){
            this.topicRepository = topicRepository;
            return this;
        }

        public CurrentTopic build() {
            return new CurrentTopic(this);
        }

    }
}
