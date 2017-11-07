package com.eazytec.bpm.lib.common.authentication;

import android.content.ContentValues;
import android.database.Cursor;

import com.eazytec.bpm.appstub.db.DB;
import com.eazytec.bpm.appstub.db.DBLoginUser;
import com.eazytec.bpm.lib.common.message.commonparams.CommonParams;
import com.eazytec.bpm.lib.utils.SPUtils;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.squareup.sqlbrite.BriteDatabase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * @author ConDey
 * @version Id: SharePrefsUserRepository, v 0.1 2017/5/26 下午4:01 ConDey Exp $$
 */
public class SharePrefsUserRepository implements UserRepository {

    private static final String USER_REPOSITORY = "USER_REPOSITORY";

    private static final String PARAM_USER_NAME = "PARAM_USERNAME";
    private static final String PARAM_PASSWORD = "PARAM_PASSWORD";

    private SPUtils sharePrefsUtil;

    private BriteDatabase mDatabase;

    public SharePrefsUserRepository(BriteDatabase mDatabase) {
        super();
        this.sharePrefsUtil = SPUtils.getInstance(USER_REPOSITORY);
        this.mDatabase = mDatabase;
    }

    @Override public void saveUsername(String username) {
        if (!StringUtils.isSpace(username)) {
            sharePrefsUtil.put(PARAM_USER_NAME, username);
        }
    }

    @Override public void savePassword(String password) {
        if (!StringUtils.isSpace(password)) {
            sharePrefsUtil.put(PARAM_PASSWORD, password);
        }
    }

    @Override public void clearUsername() {
        sharePrefsUtil.remove(PARAM_USER_NAME);
    }

    @Override public void clearPassword() {
        sharePrefsUtil.remove(PARAM_PASSWORD);
    }

    @Override public void clearAll() {
        sharePrefsUtil.clear();
    }

    @Override public String getUserName() {
        return sharePrefsUtil.getString(PARAM_USER_NAME);
    }

    @Override public String getPassword() {
        return sharePrefsUtil.getString(PARAM_PASSWORD);
    }

    @Override
    public void saveUserDetails(UserDetails details) {
        if (details == null) {
            return;
        }
        if (getUserDetailsByUsername(details.getUsername()) == null) {
            ContentValues loginUser = new DBLoginUser.Builder()
                    .username(details.getUsername())
                    .password(details.getPassword())
                    .fullname(details.getFullName())
                    .orgfullname(details.getDepartmentName())
                    .position(details.getPosition())
                    .email(details.getEmail())
                    .mobile(details.getMobile())
                  //  .photoserviceurl(details.getPhoto())
                    .createtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(TimeUtils.getNowMills())))
                    .build();
            mDatabase.insert(DBLoginUser.TABLE_LOGIN_USER, loginUser);
        } else {
            ContentValues loginUser = new DBLoginUser.Builder()
                    .username(details.getUsername())
                    .password(details.getPassword())
                    .fullname(details.getFullName())
                    .orgfullname(details.getDepartmentName())
                    .position(details.getPosition())
                    .email(details.getEmail())
                    .mobile(details.getMobile())
                  //  .photoserviceurl(details.getPhoto())
                    .build();
            mDatabase.update(DBLoginUser.TABLE_LOGIN_USER, loginUser, DBLoginUser.COLUMN_USERNAME + " == ?", details.getUsername());
        }
    }

    @Override public UserDetails getUserDetailsByUsername(String username) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBLoginUser.TABLE_LOGIN_USER + " where " + DBLoginUser.COLUMN_USERNAME + " == '" + username + "'", null);
        return convert(cursor);
    }
    @Override
    public void setLastRequestTimeByUsername(String username, String lastRequestTime) {
        ContentValues values = new DBLoginUser.Builder().lastrequesttime(lastRequestTime).build();
        mDatabase.update(DBLoginUser.TABLE_LOGIN_USER, values, DBLoginUser.COLUMN_USERNAME + " == ?", username);
    }

    @Override
    public String getLastRequestTimeByUsername(boolean isDateFormat, String username) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select " + DBLoginUser.COLUMN_LASTREQUESTTIME + " from " + DBLoginUser.TABLE_LOGIN_USER + " where " + DBLoginUser.COLUMN_USERNAME + " == '" + username +"'", null);
        try {
            while (cursor.moveToNext()) {
                String lastRequestTime = DB.getString(cursor, DBLoginUser.COLUMN_LASTREQUESTTIME);
                if (!StringUtils.isEmpty(lastRequestTime)) {
                    if (isDateFormat) {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(lastRequestTime)));
                    }else {
                        return lastRequestTime;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return CommonParams.getCommonParams().getFiveDaysAgoTime(isDateFormat);
    }

    /**
     * 数据转换逻辑,只取第一条数据，原则上没有更多的数据了
     * <p>
     * 就算有的话也没关系，数据都会一致更新的
     *
     * @param cursor
     * @return
     */
    private UserDetails convert(Cursor cursor) {
        if (null == cursor) {
            return null;
        }
        try {
            while (cursor.moveToNext()) {
                UserDetails userDetails = new UserDetails();
                userDetails.setUsername(DB.getString(cursor, DBLoginUser.COLUMN_USERNAME));
                userDetails.setPassword(DB.getString(cursor, DBLoginUser.COLUMN_PASSWORD));
                userDetails.setFullName(DB.getString(cursor, DBLoginUser.COLUMN_FULLNAME));
                userDetails.setEmail(DB.getString(cursor, DBLoginUser.COLUMN_EMAIL));
                userDetails.setMobile(DB.getString(cursor, DBLoginUser.COLUMN_MOBILE));
                userDetails.setDepartmentName(DB.getString(cursor, DBLoginUser.COLUMN_ORGFULLNAME));
              //  userDetails.setPhoto(DB.getString(cursor, DBLoginUser.COLUMN_PHOTOSERVICEURL));
                userDetails.setPosition(DB.getString(cursor, DBLoginUser.COLUMN_POSITION));
                return userDetails;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }
}
