package com.eazytec.bpm.app.home.data.authenication;

import com.eazytec.bpm.lib.common.authentication.Token;
import com.eazytec.bpm.lib.common.authentication.UserAuthority;
import com.eazytec.bpm.lib.common.authentication.UserDetails;
import com.eazytec.bpm.lib.utils.StringUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 用户身份信息帮助类
 *
 * @author ConDey
 * @version Id: AuthenicationDataHelper, v 0.1 2017/6/3 下午1:32 ConDey Exp $$
 */
public abstract class AuthenicationDataHelper {

    /**
     * 根据用户身份返回信息创建用户详细信息
     *
     * @param dataTObject
     * @return
     */
    public static UserDetails createUserDetailsByDTO(AuthenticationDataTObject dataTObject) {

        UserDetails userDetails = new UserDetails();

        if (dataTObject != null) {
            userDetails.setUsername(dataTObject.getUsername());
            userDetails.setPassword(dataTObject.getPassword());
            userDetails.setFullName(dataTObject.getFullName());
            userDetails.setDepartmentName(dataTObject.getDepartmentName());
            userDetails.setEmail(dataTObject.getEmail());
            userDetails.setMobile(dataTObject.getMobile());
            userDetails.setPosition(dataTObject.getPosition());
        }
        return userDetails;
    }

    /**
     * 根据用户身份返回信息创建用户权限
     *
     * @param dataTObject
     * @return
     */
    public static UserAuthority createUserAuthorityByDTO(AuthenticationDataTObject dataTObject) {

        UserAuthority userAuthority = new UserAuthority();

        if (dataTObject != null && !StringUtils.isSpace(dataTObject.getRoles())) {
            String[] roleArrays = dataTObject.getRoles().split(",");
            userAuthority.setAuthorities(new HashSet(Arrays.asList(roleArrays)));
        }
        return userAuthority;
    }

    /**
     * 根据用户身份返回信息创建用户Token
     *
     * @param dataTObject
     * @return
     */
    public static Token createTokenByDTO(AuthenticationDataTObject dataTObject) {
        return new Token(dataTObject.getToken());
    }
}
