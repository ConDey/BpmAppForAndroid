package com.eazytec.bpm.lib.common.message.dataobject;

/**
 * @author 16735
 * @version Id: MessageDataTObject, v 0.1 2017-8-24 8:46 16735 Exp $$
 */
public class MessageDataTObject {

    private String id;
    private String content;
    private String clickUrl;
    private long createdTime;
    private String title;
    private boolean needPush;
    private boolean canClick;
    private boolean pushed;

    private String topicIcon;
    private String topicId;
    private String internalMsgId;
    private String topicName;

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

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isNeedPush() {
        return needPush;
    }

    public void setNeedPush(boolean needPush) {
        this.needPush = needPush;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public boolean isPushed() {
        return pushed;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

    public String getTopicIcon() {
        return topicIcon;
    }

    public void setTopicIcon(String topicIcon) {
        this.topicIcon = topicIcon;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getInternalMsgId() {
        return internalMsgId;
    }

    public void setInternalMsgId(String internalMsgId) {
        this.internalMsgId = internalMsgId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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
