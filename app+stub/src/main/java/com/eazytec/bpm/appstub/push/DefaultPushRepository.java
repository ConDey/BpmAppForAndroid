package com.eazytec.bpm.appstub.push;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.eazytec.bpm.appstub.db.DBPushParams;
import com.squareup.sqlbrite.BriteDatabase;

/**
 * @author 16735
 * @version Id: DefaultPushRepository, v 0.1 2017-8-22 18:28 16735 Exp $$
 */
public class DefaultPushRepository implements PushRepository {

    private BriteDatabase mDatabase;

    public DefaultPushRepository(BriteDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    @Override
    public void savePushDeviceToken(String deviceToken) {
        ContentValues values = new DBPushParams.Builder().deviceToken(deviceToken).alias("").build();
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBPushParams.TABLE_PUSH_PARAMS, null);
        try {
            if (cursor.getCount() > 0) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBPushParams.COLUMN_ID));
                mDatabase.update(DBPushParams.TABLE_PUSH_PARAMS, values, DBPushParams.COLUMN_ID + " == ?", String.valueOf(id));
            }else {
                mDatabase.insert(DBPushParams.TABLE_PUSH_PARAMS, values);
                return;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
    }

    @Override
    public String getPushDeviceToken() {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from "+DBPushParams.TABLE_PUSH_PARAMS, null);
        String deviceToken = null;
        try {
            while (cursor.moveToNext()) {
                deviceToken = cursor.getString(cursor.getColumnIndexOrThrow(DBPushParams.COLUMN_DEVICE_TOKEN));
                Log.d("DEVICE_TOKEN", "_ _ _"+deviceToken);
                return deviceToken;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return deviceToken;
    }

    @Override
    public void savePushAlias(String alias) {

    }

    @Override
    public boolean isDeviceTokenExist(String deviceToken) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBPushParams.TABLE_PUSH_PARAMS + " where " + DBPushParams.COLUMN_DEVICE_TOKEN + " == '" + deviceToken + "'", null);
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
}
