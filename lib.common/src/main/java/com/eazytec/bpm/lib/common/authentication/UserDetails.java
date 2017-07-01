package com.eazytec.bpm.lib.common.authentication;

/**
 * @author ConDey
 * @version Id: UserDetails, v 0.1 2017/5/26 下午4:09 ConDey Exp $$
 */
public class UserDetails {

    // 用户名
    private String username;

    // 密码
    private String password;

    // 用户全名
    private String fullName;

    // 部门名称
    private String departmentName;

    // 职位名称
    private String position;

    // 电子邮件地址
    private String email;

    // 手机号码
    private String mobile;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
}
