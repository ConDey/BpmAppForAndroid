package com.eazytec.bpm.appstub.db;

import android.content.ContentValues;

/**
 * 消息数据库模型
 *
 * @author 16735
 * @version Id: DBMessage, v 0.1 2017-8-24 10:12 16735 Exp $$
 */
public class DBMessage {

    public static String TABLE_MESSAGE = "EWORK_MESSAGE";

    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CLICKURL = "clickUrl";
    public static final String COLUMN_GMTCREATE = "gmtCreate";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_NEEDPUSH = "needPush";
    public static final String COLUMN_TOEMP = "toemp";
    public static final String COLUMN_PUSHED = "pushed";
    public static final String COLUMN_CANCLICK = "canClick";
    public static final String COLUMN_ISREAD = "isread";
    public static final String COLUMN_UPDATETIME = "updatetime";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_UPDATE_MSGID = "internalMsgId";
    public static final String COLUMN_TOPIC_ICON = "topicIcon";

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(DBConstants.COLUMN_ID, id);
            return this;
        }

        public Builder messageId(String messageId) {
            values.put(COLUMN_MESSAGE_ID, messageId);
            return this;
        }

        public Builder content(String content) {
            values.put(COLUMN_CONTENT, content);
            return this;
        }

        public Builder clickUrl(String clickUrl) {
            values.put(COLUMN_CLICKURL, clickUrl);
            return this;
        }

        public Builder gmtCreate(String gmtCreate) {
            values.put(COLUMN_GMTCREATE, gmtCreate);
            return this;
        }

        public Builder title(String title) {
            values.put(COLUMN_TITLE, title);
            return this;
        }

        public Builder topic(String topic) {
            values.put(COLUMN_TOPIC, topic);
            return this;
        }

        public Builder needPush(String needPush) {
            values.put(COLUMN_NEEDPUSH, needPush);
            return this;
        }

        public Builder toemp(String toemp) {
            values.put(COLUMN_TOEMP, toemp);
            return this;
        }

        public Builder pushed(String pushed) {
            values.put(COLUMN_PUSHED, pushed);
            return this;
        }

        public Builder canClick(String canClick) {
            values.put(COLUMN_CANCLICK, canClick);
            return this;
        }

        public Builder isRead(String isRead) {
            values.put(COLUMN_ISREAD, isRead);
            return this;
        }

        public Builder updateTime(String isRead) {
            values.put(COLUMN_UPDATETIME, isRead);
            return this;
        }

        public Builder username(String username) {
            values.put(COLUMN_USERNAME, username);
            return this;
        }

        public Builder updateid(String updateid) {
            values.put(COLUMN_UPDATE_MSGID, updateid);
            return this;
        }

        public Builder topicicon(String topicIcon) {
            values.put(COLUMN_TOPIC_ICON, topicIcon);
            return this;
        }

        public ContentValues build() {
            return values;
        }

    }

}
