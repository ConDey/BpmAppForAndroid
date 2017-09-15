package com.eazytec.bpm.appstub.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建帮助类
 *
 * @author ConDey
 * @version Id: DBHelper, v 0.1 2017/8/4 下午2:20 ConDey Exp $$
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstants.DBNAME, null, DBConstants.CURRENTVERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        // 创建登录用户信息表
        db.execSQL(DBConstants.createLoginUserTable());
        // 创建通用参数表
        db.execSQL(DBConstants.createCommonParamsTable());
        // 创建Topic表
        db.execSQL(DBConstants.createTopicTable());
        // 创建message表
        db.execSQL(DBConstants.createMessageTable());
        // 创建推送参数表
        db.execSQL(DBConstants.createPushParamsTable());

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        if (oldVersion < 13) {

            /**
             *  升级方案。
             */
            db.execSQL("drop table if exists " + DBLoginUser.TABLE_LOGIN_USER);
            db.execSQL("drop table if exists " + DBPushParams.TABLE_PUSH_PARAMS);
            db.execSQL("drop table if exists " + DBTopic.TABLE_TOPIC);
            db.execSQL("drop table if exists " + DBMessage.TABLE_MESSAGE);

            // 创建登录用户信息表
            db.execSQL(DBConstants.createLoginUserTable());
            // 创建通用参数表
            db.execSQL(DBConstants.createCommonParamsTable());
            // 创建Topic表
            db.execSQL(DBConstants.createTopicTable());
            // 创建message表
            db.execSQL(DBConstants.createMessageTable());
            // 创建推送参数表
            db.execSQL(DBConstants.createPushParamsTable());
        }

        db.execSQL("CREATE INDEX INDEX_USERNAME_TOPIC ON " + DBMessage.TABLE_MESSAGE + "(" + DBMessage.COLUMN_USERNAME + "," + DBMessage.COLUMN_TOPIC + ")");
    }
}
