package com.eazytec.bpm.app.home.data.authenication;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * 普通登录数据传输协议
 *
 * @author ConDey
 * @version Id: AuthenticationDataTObject, v 0.1 2017/6/2 下午1:53 ConDey Exp $$
 */
public class AuthenticationDataTObject extends WebDataTObject {

    private String username;

    private String password;

    private String token;

    private String email;

    private String mobile;

    private String fullName;

    private String departmentName;

    private String roles;

    private String position;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
