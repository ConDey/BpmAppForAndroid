package com.eazytec.bpm.lib.common.commonparams;

import android.content.ContentValues;
import android.database.Cursor;

import com.eazytec.bpm.appstub.db.DBCommonParams;
import com.squareup.sqlbrite.BriteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 16735
 * @version Id: DefaultCommonParamsRepository, v 0.1 2017-8-24 15:19 16735 Exp $$
 */
public class DefaultCommonParamsRepository implements CommonParamsRepository {

    private BriteDatabase mDatabase;

    public DefaultCommonParamsRepository(BriteDatabase database) {
        this.mDatabase = database;
    }

    @Deprecated
    @Override
    public void updateLastRequestTime(long requestTime) {
        ContentValues values = new DBCommonParams.Builder()
                .lastRequestTime(requestTime)
                .build();
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from "+ DBCommonParams.TABLE_COMMON_PARAMS, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBCommonParams.COLUMN_ID));
                    mDatabase.update(DBCommonParams.TABLE_COMMON_PARAMS, values, DBCommonParams.COLUMN_ID + " == ?", String.valueOf(id));
                    return;
                }
            } else {
                mDatabase.insert(DBCommonParams.TABLE_COMMON_PARAMS, values);
                return;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }

    }

    @Deprecated
    @Override
    public String getLastRequestTime(boolean isDateFormat) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from "+ DBCommonParams.TABLE_COMMON_PARAMS, null);
        try {
                while (cursor.moveToNext()) {
                    long lastRequestTime = cursor.getLong(cursor.getColumnIndexOrThrow(DBCommonParams.COLUMN_LAST_REQUEST_TIME));
                    if (lastRequestTime != 0) {
                        if (isDateFormat) {
                            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(lastRequestTime));
                        }else {
                            return String.valueOf(lastRequestTime);
                        }
                    }
                    // 如果上次请求时间为0 或 不存在，则默认取五天前的时间
                    return CommonParams.getCommonParams().getFiveDaysAgoTime(isDateFormat);
                }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return CommonParams.getCommonParams().getFiveDaysAgoTime(isDateFormat);
    }

    @Override
    public void updateIsRefresh(String isRefresh) {
        ContentValues values = new DBCommonParams.Builder()
                .isRefresh(isRefresh)
                .build();
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from "+ DBCommonParams.TABLE_COMMON_PARAMS, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBCommonParams.COLUMN_ID));
                    mDatabase.update(DBCommonParams.TABLE_COMMON_PARAMS, values, DBCommonParams.COLUMN_ID + " == ?", String.valueOf(id));
                }
            } else {
                mDatabase.insert(DBCommonParams.TABLE_COMMON_PARAMS, values);
                return;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
    }

    @Override
    public String getIsRefresh() {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from "+ DBCommonParams.TABLE_COMMON_PARAMS, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String isRefresh = cursor.getString(cursor.getColumnIndexOrThrow(DBCommonParams.COLUMN_IS_REFRESH));
                    if (isRefresh != null) {
                        return isRefresh;
                    }
                    return "0";
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return "0";
    }
}
