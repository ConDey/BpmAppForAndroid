package com.eazytec.bpm.lib.common.message;

import android.content.ContentValues;
import android.database.Cursor;

import com.eazytec.bpm.appstub.db.DB;
import com.eazytec.bpm.appstub.db.DBMessage;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.common.message.topic.CurrentTopic;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 16735
 * @version Id: DefaultMessageRepository, v 0.1 2017-8-25 9:41 16735 Exp $$
 */
public class DefaultMessageRepository implements MessageRepository {

    private BriteDatabase mDatabase;

    public DefaultMessageRepository(BriteDatabase database) {
        this.mDatabase = database;
    }

    @Override
    public void insertMessageIntoDB(List<MessageDataTObject> messages) {
        for (int i = 0; i < messages.size(); i++) {
            MessageDataTObject message = messages.get(i);
            ContentValues values = new DBMessage.Builder()
                    .messageId(message.getId())
                    .content(message.getContent())
                    .clickUrl(message.getClickUrl())
                    .gmtCreate(message.getCreatedTime())
                    .title(message.getTitle())
                    .topic(message.getTopicId())
                    .topicicon(message.getTopicIcon())
                    .needPush(transBool2Str(message.isNeedPush()))
                    .updateTime(String.valueOf(TimeUtils.getNowMills()))
                    .pushed(transBool2Str(message.isPushed()))
                    .canClick(transBool2Str(message.isCanClick()))
                    .isRead(transBool2Str(message.getIsRead()))
                    .username(CurrentUser.getCurrentUser().getUserDetails().getUsername())
                    .updateid(message.getInternalMsgId())
                    .build();

            if (getMessageById(message.getId()) != null) {
                if (getMessageById(message.getId()).size() <= 0) {
                    mDatabase.insert(DBMessage.TABLE_MESSAGE, values);
                    CurrentTopic.getCurrentTopic().updateLatestTime(String.valueOf(message.getTopicId()), message.getCreatedTime());
                }
            } else {
                mDatabase.insert(DBMessage.TABLE_MESSAGE, values);
                CurrentTopic.getCurrentTopic().updateLatestTime(String.valueOf(message.getTopicId()), message.getCreatedTime());
            }

        }
    }

    @Override
    public List<MessageDataTObject> selectMessageFromDB(String topicId) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBMessage.TABLE_MESSAGE + " where " + DBMessage.COLUMN_TOPIC + " == '" + topicId + "' and " + DBMessage.COLUMN_USERNAME + " == '" + CurrentUser.getCurrentUser().getUserDetails().getUsername() + "' order by " + DBMessage.COLUMN_GMTCREATE + " desc ", null);
        return convert(cursor);
    }

    @Override
    public void updateMessageIsReadState(String topicId , String id) {
        ContentValues values = new DBMessage.Builder().isRead("1").build();
        mDatabase.update(DBMessage.TABLE_MESSAGE, values, DBMessage.COLUMN_USERNAME + " == ? and " + DBMessage.COLUMN_MESSAGE_ID + " == ? and "  + DBMessage.COLUMN_TOPIC + " == ?", CurrentUser.getCurrentUser().getUserDetails().getUsername(),id ,String.valueOf(topicId));
    }

    // 分页查询
    @Override
    public List<MessageDataTObject> selectMessageByPage(String isRead, int pageIndex, int pageSize) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBMessage.TABLE_MESSAGE + " where " + DBMessage.COLUMN_ISREAD + " == '" + isRead + "' and " + DBMessage.COLUMN_USERNAME + " == '" + CurrentUser.getCurrentUser().getUserDetails().getUsername() + "' order by " + DBMessage.COLUMN_GMTCREATE + " desc limit " + pageIndex + " , " + pageSize, null);
        return convert(cursor);
    }


    /**
     * 根据当前messageId来查询message
     *
     * @param messageId
     * @return
     */
    private List<MessageDataTObject> getMessageById(String messageId) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBMessage.TABLE_MESSAGE + " where " + DBMessage.COLUMN_MESSAGE_ID + " == '" + messageId + "' and " + DBMessage.COLUMN_USERNAME + " == '" + CurrentUser.getCurrentUser().getUserDetails().getUsername() + "'", null);
        return convert(cursor);
    }


    /**
     * 处理查询结果
     *
     * @param cursor
     * @return
     */
    private List<MessageDataTObject> convert(Cursor cursor) {
        List<MessageDataTObject> messages = new ArrayList<>();
        if (cursor == null) {
            return null;
        }
        try {
            while (cursor.moveToNext()) {
                MessageDataTObject message = new MessageDataTObject();
                message.setId(DB.getString(cursor, DBMessage.COLUMN_MESSAGE_ID));
                message.setContent(DB.getString(cursor, DBMessage.COLUMN_CONTENT));
                message.setClickUrl(DB.getString(cursor, DBMessage.COLUMN_CLICKURL));
                message.setCreatedTime(DB.getString(cursor, DBMessage.COLUMN_GMTCREATE));
                message.setTitle(DB.getString(cursor, DBMessage.COLUMN_TITLE));
                message.setTopicId(DB.getString(cursor, DBMessage.COLUMN_TOPIC));
                message.setTopicIcon(DB.getString(cursor,DBMessage.COLUMN_TOPIC_ICON));
                message.setNeedPush(transStr2Bool(DB.getString(cursor, DBMessage.COLUMN_NEEDPUSH)));
                message.setPushed(transStr2Bool(DB.getString(cursor, DBMessage.COLUMN_PUSHED)));
                message.setCanClick(transStr2Bool(DB.getString(cursor, DBMessage.COLUMN_CANCLICK)));
                message.setIsRead(transStr2Bool(DB.getString(cursor, DBMessage.COLUMN_ISREAD)));
                message.setUsername(DB.getString(cursor, DBMessage.COLUMN_USERNAME));
                message.setInternalMsgId(DB.getString(cursor,DBMessage.COLUMN_UPDATE_MSGID));
                messages.add(message);
            }
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * 工具方法
     *
     * @return
     */
    private String transBool2Str(boolean mBool) {
        if (mBool) {
            return "1";
        } else {
            return "0";
        }
    }

    private boolean transStr2Bool(String s) {
        if (s.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

}
