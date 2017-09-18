package com.eazytec.bpm.appstub.db;

import android.content.ContentValues;

/**
 * 主题数据库模型
 *
 * @author 16735
 * @version Id: DBTopic, v 0.1 2017-8-24 10:12 16735 Exp $$
 */
public class DBTopic {

    public static String TABLE_TOPIC = "EWORK_TOPIC";

    public static final String COLUMN_NAME = "username";
    public static final String COLUMN_TOPIC_ID = "topic_id";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_UPDATE_TIME = "update_time";

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(DBConstants.COLUMN_ID, id);
            return this;
        }

        public Builder topicId(String topicId) {
            values.put(COLUMN_TOPIC_ID, topicId);
            return this;
        }

        public Builder name(String name) {
            values.put(COLUMN_NAME, name);
            return this;
        }

        public Builder icon(String icon) {
            values.put(COLUMN_ICON, icon);
            return this;
        }

        public Builder topic(String topic) {
            values.put(COLUMN_TOPIC, topic);
            return this;
        }

        public Builder updatetime(String updateTime) {
            values.put(COLUMN_UPDATE_TIME, updateTime);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
