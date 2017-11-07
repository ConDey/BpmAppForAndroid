package com.eazytec.bpm.lib.common.authentication;

/**
 * 用户信息仓储器
 *
 * @author ConDey
 * @version Id: UserRepository, v 0.1 2017/5/26 下午2:37 ConDey Exp $$
 */
public interface UserRepository {

    /**
     * 设置用户名
     *
     * @param username
     */
    public void saveUsername(String username);

    /**
     * 设置用户密码
     *
     * @param password
     */
    public void savePassword(String password);

    /**
     * 清空用户名
     */
    public void clearUsername();

    /**
     * 清空密码
     */
    public void clearPassword();

    /***
     * 清空所有的内容
     */
    public void clearAll();

    /**
     * 获得用户名
     *
     * @return
     */
    public String getUserName();

    /**
     * 获得密码
     *
     * @return
     */
    public String getPassword();

    /**
     * 设置用户名
     *
     * @param details
     */
    public void saveUserDetails(UserDetails details);

    /**
     * 根据UserName获取登录信息
     * <p>
     * 观察者模式, 异步使用
     *
     * @param username
     * @return
     */
    public UserDetails getUserDetailsByUsername(String username);

    /**
     * 设置当前用户上次请求时间
     *
     * @param username
     */
    public void setLastRequestTimeByUsername(String username, String lastRequestTime);

    /**
     * 获取当前用户上次请求时间
     *
     * @param username
     */
    public String getLastRequestTimeByUsername(boolean isDateFormat, String username);

}
