package com.eazytec.bpm.lib.common.message.dataobject;

/**
 * @author 16735
 * @version Id: MessageTopicDataTObject, v 0.1 2017-8-24 8:43 16735 Exp $$
 */
public class MessageTopicDataTObject {

    private String name;
    private int id;
    private String icon;
    private String topic;

    // 给数据库用
    private int unReadMessages;
    private String latestMessage;
    private String latestUpdateTime;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnReadMessages() {
        return unReadMessages;
    }

    public void setUnReadMessages(int unReadMessages) {
        this.unReadMessages = unReadMessages;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(String latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }
}
