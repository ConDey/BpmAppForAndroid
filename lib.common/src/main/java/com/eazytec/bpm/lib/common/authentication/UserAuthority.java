package com.eazytec.bpm.lib.common.authentication;

import com.eazytec.bpm.lib.utils.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用户权限模型
 * <p>
 * 代表BPM的用户权限
 *
 * @author ConDey
 * @version Id: UserAuthority, v 0.1 2017/5/26 下午3:09 ConDey Exp $$
 */
public class UserAuthority {

    private Set<String> authorities;

    /**
     * 默认构造函数
     */
    public UserAuthority() {
        this.authorities = new LinkedHashSet<>();
    }

    /**
     * 设置用户权限模型
     *
     * @param authorities
     */
    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }


    /**
     * 新增权限
     *
     * @param authority
     */
    public void addAuthority(String authority) {
        this.authorities.add(authority);
    }

    /**
     * 判断用户是否拥有否权限
     *
     * @param authority
     * @return
     */
    public boolean hasAuthority(String authority) {
        if (StringUtils.isSpace(authority)) {
            return false;
        }
        return authorities != null && authority.contains(authority) ? true : false;
    }

}
