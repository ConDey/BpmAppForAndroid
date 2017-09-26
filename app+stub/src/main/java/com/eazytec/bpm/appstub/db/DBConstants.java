package com.eazytec.bpm.appstub.db;

import com.eazytec.bpm.appstub.Config;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;


/**
 * 数据库相关的常量
 *
 * @author ConDey
 * @version Id: DBConstants, v 0.1 2017/8/4 下午2:22 ConDey Exp $$
 */
public abstract class DBConstants {

    protected static BriteDatabase briteDatabase;

    protected static final String DBNAME = Config.DB_NAME;
    protected static final int CURRENTVERSION = 15;

    protected static String createLoginUserTable() {
        return new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(DBLoginUser.TABLE_LOGIN_USER).append("(")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(DBLoginUser.COLUMN_USERNAME).append(" TEXT NOT NULL,")
                .append(DBLoginUser.COLUMN_PASSWORD).append(" TEXT,")
                .append(DBLoginUser.COLUMN_FULLNAME).append(" TEXT,")
                .append(DBLoginUser.COLUMN_ORGFULLNAME).append(" TEXT,")
                .append(DBLoginUser.COLUMN_POSITION).append(" TEXT,")
                .append(DBLoginUser.COLUMN_EMAIL).append(" TEXT,")
                .append(DBLoginUser.COLUMN_MOBILE).append(" TEXT,")
                .append(DBLoginUser.COLUMN_PHOTOSERVICEURL).append(" TEXT,")
                .append(DBLoginUser.COLUMN_PHOTOFILEURL).append(" TEXT,")
                .append(DBLoginUser.COLUMN_LASTREQUESTTIME).append(" TEXT,")
                .append(COLUMN_CREATE_TIME).append(" TEXT NOT NULL")
                .append(")").toString();
    }

    /**
     * 创建Topic表
     */
    protected static String createTopicTable() {
        return new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(DBTopic.TABLE_TOPIC).append("(")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(DBTopic.COLUMN_TOPIC_ID).append(" TEXT,")
                .append(DBTopic.COLUMN_NAME).append(" TEXT,")
                .append(DBTopic.COLUMN_ICON).append(" TEXT,")
                .append(DBTopic.COLUMN_TOPIC).append(" TEXT,")
                .append(DBTopic.COLUMN_UPDATE_TIME).append(" TEXT")
                .append(")").toString();
    }

    /**
     * 创建Message表
     */
    protected static String createMessageTable() {
        return new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(DBMessage.TABLE_MESSAGE).append("(")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(DBMessage.COLUMN_MESSAGE_ID).append(" TEXT,")
                .append(DBMessage.COLUMN_CONTENT).append(" TEXT,")
                .append(DBMessage.COLUMN_CLICKURL).append(" TEXT,")
                .append(DBMessage.COLUMN_GMTCREATE).append(" TEXT,")
                .append(DBMessage.COLUMN_TITLE).append(" TEXT,")
                .append(DBMessage.COLUMN_TOPIC).append(" TEXT,")
                .append(DBMessage.COLUMN_NEEDPUSH).append(" TEXT,")
                .append(DBMessage.COLUMN_TOEMP).append(" TEXT,")
                .append(DBMessage.COLUMN_PUSHED).append(" TEXT,")
                .append(DBMessage.COLUMN_CANCLICK).append(" TEXT,")
                .append(DBMessage.COLUMN_ISREAD).append(" TEXT,")
                .append(DBMessage.COLUMN_UPDATETIME).append(" TEXT,")
                .append(DBMessage.COLUMN_USERNAME).append(" TEXT,")
                .append(DBMessage.COLUMN_TOPIC_ICON).append(" TEXT,")
                .append(DBMessage.COLUMN_UPDATE_MSGID).append(" TEXT")
                .append(")").toString();
    }

    /**
     * 创建通用参数表
     */
    protected static String createCommonParamsTable() {
        return new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(DBCommonParams.TABLE_COMMON_PARAMS).append("(")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(DBCommonParams.COLUMN_LAST_REQUEST_TIME).append(" VAR(200),")
                .append(DBCommonParams.COLUMN_IS_REFRESH).append(" TEXT")
                .append(")").toString();
    }

    /**
     * 创建推送参数表
     *
     * @return
     */
    protected static String createPushParamsTable() {
        return new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(DBPushParams.TABLE_PUSH_PARAMS).append("(")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(DBPushParams.COLUMN_DEVICE_TOKEN).append(" TEXT,")
                .append(DBPushParams.COLUMN_ALIAS).append(" TEXT,")
                .append(COLUMN_CREATE_TIME).append(" TEXT")
                .append(")").toString();
    }

    protected static String COLUMN_ID = "id";
    protected static String COLUMN_CREATE_TIME = "createtime";

    /**
     * 创建BriteDatabase
     *
     * @param dbHelper
     * @return
     */
    public synchronized static BriteDatabase createBriteDatabase(DBHelper dbHelper) {
        if (briteDatabase == null) {
            SqlBrite sqlBrite = new SqlBrite.Builder().build();
            BriteDatabase db = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
            db.setLoggingEnabled(true);

            briteDatabase = db;
        }
        return briteDatabase;
    }

    public static BriteDatabase getBriteDatabase() {
        return briteDatabase;
    }
}
