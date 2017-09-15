package com.eazytec.bpm.appstub.db;

import android.content.ContentValues;

/**
 * DBLoginUser数据库模型
 *
 * @author ConDey
 * @version Id: DBLoginUser, v 0.1 2017/8/4 下午4:07 ConDey Exp $$
 */
public class DBLoginUser {

    public static String TABLE_LOGIN_USER = "EWORK_LOGIN_USER";

    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_ORGFULLNAME = "orgfullname";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_PHOTOSERVICEURL = "photoserviceurl";
    public static final String COLUMN_PHOTOFILEURL = "photofileurl";
    public static final String COLUMN_LASTREQUESTTIME = "lastrequesttime";


    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(DBConstants.COLUMN_ID, id);
            return this;
        }

        public Builder username(String username) {
            values.put(COLUMN_USERNAME, username);
            return this;
        }

        public Builder password(String password) {
            values.put(COLUMN_PASSWORD, password);
            return this;
        }

        public Builder fullname(String fullname) {
            values.put(COLUMN_FULLNAME, fullname);
            return this;
        }

        public Builder orgfullname(String orgfullname) {
            values.put(COLUMN_ORGFULLNAME, orgfullname);
            return this;
        }

        public Builder position(String position) {
            values.put(COLUMN_POSITION, position);
            return this;
        }

        public Builder email(String email) {
            values.put(COLUMN_EMAIL, email);
            return this;
        }

        public Builder mobile(String mobile) {
            values.put(COLUMN_MOBILE, mobile);
            return this;
        }

        public Builder photoserviceurl(String photoserviceurl) {
            values.put(COLUMN_PHOTOSERVICEURL, photoserviceurl);
            return this;
        }

        public Builder photofileurl(String photofileurl) {
            values.put(COLUMN_PHOTOFILEURL, photofileurl);
            return this;
        }

        public Builder lastrequesttime(String lastrequesttime) {
            values.put(COLUMN_LASTREQUESTTIME, lastrequesttime);
            return this;
        }

        public Builder createtime(String createtime) {
            values.put(DBConstants.COLUMN_CREATE_TIME, createtime);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
