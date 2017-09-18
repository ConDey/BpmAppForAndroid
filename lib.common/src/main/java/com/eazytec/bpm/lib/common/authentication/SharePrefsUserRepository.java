package com.eazytec.bpm.lib.common.authentication;

import com.eazytec.bpm.lib.utils.SPUtils;
import com.eazytec.bpm.lib.utils.StringUtils;

/**
 * @author ConDey
 * @version Id: SharePrefsUserRepository, v 0.1 2017/5/26 下午4:01 ConDey Exp $$
 */
public class SharePrefsUserRepository implements UserRepository {

    private static final String USER_REPOSITORY = "USER_REPOSITORY";

    private static final String PARAM_USER_NAME = "PARAM_USERNAME";
    private static final String PARAM_PASSWORD = "PARAM_PASSWORD";

    private SPUtils sharePrefsUtil;

    public SharePrefsUserRepository() {
        super();
        this.sharePrefsUtil = SPUtils.getInstance(USER_REPOSITORY);
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
    public void setLastRequestTimeByUsername(String username, String lastRequestTime) {

    }

    @Override
    public String getLastRequestTimeByUsername(boolean isDateFormat, String username) {
        return null;
    }
}
