package com.eazytec.bpm.lib.common.authentication;


/**
 * 代表当前用户
 * <p>
 * <p>
 * 代表当前APP的当前登录用户
 *
 * @author ConDey
 * @version Id: CurrentUser, v 0.1 2017/5/26 下午4:16 ConDey Exp $$
 */
public final class CurrentUser {

    /**
     * 代表当前用户
     */
    private static CurrentUser currentUser;

    private CurrentUser(Build build) {
        this.build = build;
    }

    /**
     * 获得当前用户的单例对象
     *
     * @return
     */
    public static CurrentUser getCurrentUser() {
        if (currentUser == null) {
            currentUser = new CurrentUser.Build().userRepository(new SharePrefsUserRepository()).build();
        }
        return currentUser;
    }

    private boolean isLogin = false;  // 是否已经登录

    // 用户对象
    private UserDetails userDetails;

    // 用户模型
    private UserAuthority userAuthority;

    // 用户连接标识
    private Token token;

    /**
     * 设置登录
     *
     * @param userDetails
     * @param userAuthority
     * @param token
     */
    public void updateCurrentUser(UserDetails userDetails, UserAuthority userAuthority, Token token) {
        this.userDetails = userDetails;
        this.userAuthority = userAuthority;
        this.token = token;

        // 记录用户名和密码
        if (this.build.userRepository != null) {
            this.build.userRepository.saveUsername(userDetails.getUsername());
            this.build.userRepository.savePassword(userDetails.getPassword());
        }
        this.isLogin = true;
    }


    /**
     * 获得上一次的登录用户
     *
     * @return
     */
    public String getLastLoginUsername() {
        return this.build.userRepository.getUserName();
    }

    /**
     * 获得上一次的登录密码
     *
     * @return
     */
    public String getLastLoginPassword() {
        return this.build.userRepository.getPassword();
    }

    public boolean isLogin() {
        return isLogin;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public UserAuthority getUserAuthority() {
        return userAuthority;
    }

    public Token getToken() {
        return token;
    }

    // 用户生成者
    private Build build;

    public static class Build {

        // 用户仓储
        private UserRepository userRepository;

        public Build userRepository(UserRepository userRepository) {
            this.userRepository = userRepository;
            return this;
        }

        public CurrentUser build() {
            return new CurrentUser(this);
        }

    }
}
