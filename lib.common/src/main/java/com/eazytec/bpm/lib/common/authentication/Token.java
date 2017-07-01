package com.eazytec.bpm.lib.common.authentication;

import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用户连接标识
 *
 * @author ConDey
 * @version Id: Token, v 0.1 2017/5/26 下午4:11 ConDey Exp $$
 */
public class Token {

    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return getToken();
    }

    /**
     * 创建默认的系统标记Token
     *
     * @return
     */
    public static Token createDefaultSysToken() {
        try {
            return new Token(tokenCrypt(Config.DEFAULT_TOKEN));
        } catch (NoSuchAlgorithmException exception) {

        }
        return new Token(StringUtils.space());
    }

    /**
     * 默认Token的加密方法
     *
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String tokenCrypt(String str) throws NoSuchAlgorithmException {
        if (str == null || str.length() == 0)
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte hash[] = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++)
            if ((255 & hash[i]) < 16)
                hexString.append((new StringBuilder("0")).append(Integer.toHexString(255 & hash[i])).toString());
            else
                hexString.append(Integer.toHexString(255 & hash[i]));
        return hexString.toString().substring(0, 8);
    }
}
