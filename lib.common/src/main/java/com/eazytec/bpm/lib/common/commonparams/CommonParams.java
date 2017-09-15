package com.eazytec.bpm.lib.common.commonparams;

import com.eazytec.bpm.appstub.db.DBConstants;
import com.eazytec.bpm.lib.utils.TimeUtils;
import com.eazytec.bpm.lib.utils.constant.TimeConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 代表当前的通用参数对象 包括上次请求时间等信息
 *
 * @author 16735
 * @version Id: CommonParams, v 0.1 2017-8-24 14:56 16735 Exp $$
 */
public class CommonParams {

    public static final String IS_REFRESH_TRUE = "1";
    public static final String IS_REFRESH_FALSE = "0";

    private static final long FIVE_DAYS_TIMESTAMP = 5;

    private static CommonParams commonParams;

    private CommonParams(Build build) {
        this.build = build;
    }

    /**
     * 获得单例对象
     */
    public static CommonParams getCommonParams() {
        if (commonParams == null) {
            commonParams = new CommonParams.Build().commonParamsRepository(new DefaultCommonParamsRepository(DBConstants.getBriteDatabase())).build();
        }
        return commonParams;
    }

    /**
     * 存入当前请求时间
     *

     public void updateLastRequestTime(){
     // 获取当前时间
     long lastRequestTime = System.currentTimeMillis();

     // 更新
     if (this.build.commonParamsRepository != null) {
     this.build.commonParamsRepository.updateLastRequestTime(lastRequestTime);
     }
     }
     */

    /**
     * 获得上次请求时间
     *
     public String getLastRequestTime(boolean isDateFormat) {

     if (this.build.commonParamsRepository != null) {
     return this.build.commonParamsRepository.getLastRequestTime(isDateFormat);
     }
     return getFiveDaysAgoTime(isDateFormat);
     }
     */

    /**
     * 存入是否需要刷新标识
     */
    public void updateIsRefresh(String isRefresh) {
        if (this.build.commonParamsRepository != null) {
            this.build.commonParamsRepository.updateIsRefresh(isRefresh);
        }
    }

    /**
     * 获得是否需要刷新标识
     */
    public String getIsRefresh() {
        if (this.build.commonParamsRepository != null) {
            return this.build.commonParamsRepository.getIsRefresh();
        }
        return "0"; // 默认返回0，代表不需要刷新
    }

    private Build build;

    public static class Build {

        // 通用参数仓储
        private CommonParamsRepository commonParamsRepository;

        public Build commonParamsRepository(CommonParamsRepository commonParamsRepository) {
            this.commonParamsRepository = commonParamsRepository;
            return this;
        }

        public CommonParams build() {
            return new CommonParams(this);
        }
    }

    /**
     * 获取距离今天五天前的时间
     *
     * @return
     */
    public String getFiveDaysAgoTime(boolean isDateFormat) {
        if (isDateFormat) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(TimeUtils.getNowMills() - (FIVE_DAYS_TIMESTAMP * TimeConstants.DAY)));
        }
        return String.valueOf(TimeUtils.getNowMills() - (FIVE_DAYS_TIMESTAMP * TimeConstants.DAY));
    }


}
