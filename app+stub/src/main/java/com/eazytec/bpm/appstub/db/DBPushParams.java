package com.eazytec.bpm.appstub.db;

import android.content.ContentValues;

/**
 * 推送参数模型。包括Alias, DeviceToken
 *
 * @author 16735
 * @version Id: DBPushParams, v 0.1 2017-8-22 16:19 16735 Exp $$
 */
public class DBPushParams {

    public static String TABLE_PUSH_PARAMS = "EWORK_PUSH_PARAMS";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DEVICE_TOKEN = "devicetoken";
    public static final String COLUMN_ALIAS = "alias";

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(COLUMN_ID, id);
            return this;
        }

        public Builder deviceToken(String deviceToken) {
            values.put(COLUMN_DEVICE_TOKEN, deviceToken);
            return this;
        }

        public Builder alias(String alias) {
            values.put(COLUMN_ALIAS, alias);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
