package com.eazytec.bpm.lib.common.commonparams;

/**
 * @author 16735
 * @version Id: CommonParamsRepository, v 0.1 2017-8-24 15:13 16735 Exp $$
 */
public interface CommonParamsRepository  {

    /**
     * 更新上次请求时间
     */
    public void updateLastRequestTime(long requestTime);

    /**
     * 获取上次请求时间
     *
     * @return
     */
    public String getLastRequestTime(boolean isDateFormat);

    /**
     * 更新是否需要刷新标识
     */
    public void updateIsRefresh(String isRefresh);

    /**
     * 获取是否需要刷新标识
     */
    public String getIsRefresh();
}
