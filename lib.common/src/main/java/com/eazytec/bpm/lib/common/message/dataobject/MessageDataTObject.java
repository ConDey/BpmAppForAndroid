package com.eazytec.bpm.lib.common.message.dataobject;

/**
 * @author 16735
 * @version Id: MessageDataTObject, v 0.1 2017-8-24 8:46 16735 Exp $$
 */
public class MessageDataTObject {

    private String id;
    private String content;
    private String clickUrl;
    private long gmtCreate;
    private String title;
    private int topic;
    private boolean needPush;
    private String toemp;
    private boolean pushed;
    private boolean canClick;
    private MessageTopicDataTObject topicObject;

    // 给数据库使用 记录是否已读
    private boolean isRead;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public boolean isNeedPush() {
        return needPush;
    }

    public void setNeedPush(boolean needPush) {
        this.needPush = needPush;
    }

    public String getToemp() {
        return toemp;
    }

    public void setToemp(String toemp) {
        this.toemp = toemp;
    }

    public boolean isPushed() {
        return pushed;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public MessageTopicDataTObject getTopicObject() {
        return topicObject;
    }

    public void setTopicObject(MessageTopicDataTObject topicObject) {
        this.topicObject = topicObject;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
