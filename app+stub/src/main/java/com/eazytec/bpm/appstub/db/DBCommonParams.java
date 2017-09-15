package com.eazytec.bpm.appstub.db;

import android.content.ContentValues;

/**
 * 通用参数数据库模型
 *
 * @author 16735
 * @version Id: DBCommonParams, v 0.1 2017-8-24 14:26 16735 Exp $$
 */
public class DBCommonParams {

    public static String TABLE_COMMON_PARAMS = "BPM_COMMON_PARAMS";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAST_REQUEST_TIME = "last_request_time";
    public static final String COLUMN_IS_REFRESH = "is_refresh";

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(COLUMN_ID, id);
            return this;
        }

        public Builder lastRequestTime(long lastRequestTime) {
            values.put(COLUMN_LAST_REQUEST_TIME, lastRequestTime);
            return this;
        }

        public Builder isRefresh(String isRefresh) {
            values.put(COLUMN_IS_REFRESH, isRefresh);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }

}
