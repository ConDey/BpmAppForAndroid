package com.eazytec.bpm.appstub.push;


import com.eazytec.bpm.appstub.db.DBConstants;

/**
 * 代表当前推送的参数对象
 *
 * @author 16735
 * @version Id: CurrentPushParams, v 0.1 2017-8-22 19:33 16735 Exp $$
 */
public final class CurrentPushParams {

    private static CurrentPushParams currentPushParams;
    private Build build;
    private String currentDeviceToken;

    private CurrentPushParams(Build build) {
        this.build = build;
    }

    /**
     *  获取单例对象
     */
    public static CurrentPushParams getCurrentPushParams() {
        if (currentPushParams == null) {
            currentPushParams = new CurrentPushParams.Build().pushRepository(new DefaultPushRepository(DBConstants.getBriteDatabase())).build();
        }
        return currentPushParams;
    }

    /**
     *  更新当前推送参数
     */
    public void updateCurrentPushParams(String deviceToken) {

        this.currentDeviceToken = deviceToken;
        if (this.build.pushRepository != null) {
            this.build.pushRepository.savePushDeviceToken(deviceToken);
        }
    }

    /**
     *  获得当前deviceToken
     */
    public String getDeviceToken() {
        if (this.build.pushRepository != null) {
           return this.build.pushRepository.getPushDeviceToken();
        }
        return null;
    }


    // 参数对象 Build
    public static class Build {

        private PushRepository pushRepository;

        public Build pushRepository(PushRepository pushRepository) {
            this.pushRepository = pushRepository;
            return this;
        }

        public CurrentPushParams build() {
            return new CurrentPushParams(this);
        }
    }

}
