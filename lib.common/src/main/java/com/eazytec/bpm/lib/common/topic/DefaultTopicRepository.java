package com.eazytec.bpm.lib.common.topic;

import android.content.ContentValues;
import android.database.Cursor;

import com.eazytec.bpm.appstub.db.DB;
import com.eazytec.bpm.appstub.db.DBMessage;
import com.eazytec.bpm.appstub.db.DBTopic;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 16735
 * @version Id: DefaultTopicRepository, v 0.1 2017-8-25 8:34 16735 Exp $$
 */
public class DefaultTopicRepository implements TopicRepository {

    private BriteDatabase mDatabase;

    public DefaultTopicRepository(BriteDatabase database) {
        this.mDatabase = database;
    }

    @Override
    public void insertTopicIntoDB(List<MessageTopicDataTObject> topics) {
        for (int i = 0; i < topics.size(); i++) {
            MessageTopicDataTObject topic = topics.get(i);
            ContentValues values = new DBTopic.Builder()
                    .topicId(topic.getId())
                    .icon(topic.getIcon())
                    .name(topic.getName())
                    .topic(topic.getTopic())
                    .updatetime(String.valueOf(TimeUtils.getNowMills()))
                    .build();
            if (getTopicById(topic.getId()) != null) {
                if (getTopicById(topic.getId()).size() > 0) {
                    // 已存在， 则更新topic
                    //mDatabase.update(DBTopic.TABLE_TOPIC, values, DBTopic.COLUMN_TOPIC_ID + " == ? ", String.valueOf(topic.getId()));
                } else {
                    mDatabase.insert(DBTopic.TABLE_TOPIC, values);
                }
            } else {
                mDatabase.insert(DBTopic.TABLE_TOPIC, values);
            }
        }
    }

    @Override
    public List<MessageTopicDataTObject> selectTopicFromDB() {
        String username = CurrentUser.getCurrentUser().getUserDetails().getUsername();
        // 根据message来获取topic
        List<String> topicIds = new ArrayList<>();
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select distinct " + DBMessage.COLUMN_TOPIC + " from " + DBMessage.TABLE_MESSAGE + " where " + DBMessage.COLUMN_USERNAME + " == '" + username + "'", null);
        try {
            while (cursor.moveToNext()) {
                topicIds.add(DB.getString(cursor, DBMessage.COLUMN_TOPIC));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        if (topicIds.size() <= 0) {
            return new ArrayList<MessageTopicDataTObject>();
        }

        StringBuffer sqlTopic = new StringBuffer();
        for (int i = 0; i < topicIds.size(); i++) {
            sqlTopic.append("'");
            sqlTopic.append(topicIds.get(i));
            sqlTopic.append("',");
        }
        String sqlTopicStr = sqlTopic.substring(0, sqlTopic.length() - 1);

        Cursor newcursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBTopic.TABLE_TOPIC + " where " + DBTopic.COLUMN_TOPIC_ID + " in ( " + sqlTopicStr + " ) " + " order by " + DBTopic.COLUMN_UPDATE_TIME + " desc ", null);
        return convert(newcursor);
    }

    @Override
    public void updateLatestMessageTime(String topicId, String latestTime) {
        ContentValues values = new DBTopic.Builder().updatetime(latestTime).build();
        mDatabase.update(DBTopic.TABLE_TOPIC, values, DBTopic.COLUMN_TOPIC_ID + " == ?", topicId);
    }

    /**
     * 根据当前topicId来查询topic
     *
     * @param id
     * @return
     */
    private List<MessageTopicDataTObject> getTopicById(int id) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBTopic.TABLE_TOPIC + " where " + DBTopic.COLUMN_TOPIC_ID + " == '" + id + "'", null);
        return convert(cursor);
    }

    /**
     * 对查询结果做一些处理
     *
     * @param cursor
     * @return
     */
    private List<MessageTopicDataTObject> convert(Cursor cursor) {
        List<MessageTopicDataTObject> topics = new ArrayList<>();
        if (cursor == null) {
            return new ArrayList<>();
        }
        try {
            while (cursor.moveToNext()) {
                MessageTopicDataTObject topic = new MessageTopicDataTObject();
                topic.setId(Integer.valueOf(DB.getString(cursor, DBTopic.COLUMN_TOPIC_ID)));
                topic.setName(DB.getString(cursor, DBTopic.COLUMN_NAME));
                topic.setIcon(DB.getString(cursor, DBTopic.COLUMN_ICON));
                topic.setTopic(DB.getString(cursor, DBTopic.COLUMN_TOPIC));
                // 查询当前主题下的未读消息数
                Cursor unReadCursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBMessage.TABLE_MESSAGE + " where " + DBMessage.COLUMN_TOPIC + " == '" + topic.getId() +
                        "' and " + DBMessage.COLUMN_USERNAME + " == '" + CurrentUser.getCurrentUser().getUserDetails().getUsername() + "' and " + DBMessage.COLUMN_ISREAD + " == '0'", null);
                if (unReadCursor != null) {
                    topic.setUnReadMessages(unReadCursor.getCount());
                } else {
                    topic.setUnReadMessages(0);
                }
                // 查询最新消息内容
                Cursor latestMessageCursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBMessage.TABLE_MESSAGE + " where " + DBMessage.COLUMN_TOPIC + " == '" + topic.getId() +
                        "' and " + DBMessage.COLUMN_USERNAME + " == '" + CurrentUser.getCurrentUser().getUserDetails().getUsername() + "' order by " + DBMessage.COLUMN_GMTCREATE + " desc limit 0 , 1", null);
                if (latestMessageCursor.getCount() > 0) {
                    while (latestMessageCursor.moveToNext()) {
                        topic.setLatestMessage(DB.getString(latestMessageCursor, DBMessage.COLUMN_CONTENT));
                        topic.setLatestUpdateTime(DB.getString(latestMessageCursor, DBMessage.COLUMN_GMTCREATE));
                        // 这里再做一次更新
                        updateLatestMessageTime(String.valueOf(topic.getId()), DB.getString(latestMessageCursor, DBMessage.COLUMN_GMTCREATE));
                    }
                } else {
                    topic.setLatestMessage("暂无消息");
                }

                topics.add(topic);
            }
            return topics;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return new ArrayList<>();
    }
}
