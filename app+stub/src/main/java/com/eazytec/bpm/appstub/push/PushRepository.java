package com.eazytec.bpm.appstub.push;

/**
 * 推送信息仓储器
 *
 * @author 16735
 * @version Id: PushRepository, v 0.1 2017-8-22 18:20 16735 Exp $$
 */
public interface PushRepository {

    /**
     * 保存deviceToken
     * @param deviceToken
     */
    public void savePushDeviceToken(String deviceToken);

    /**
     * 获得deviceToken
     * @return
     */
    public String getPushDeviceToken();

    /**
     * 保存Alias
     * @param alias
     */
    public void savePushAlias(String alias);

    /**
     * 判断当前deviceToken是否存在
     * @param deviceToken
     * @return
     */
    public boolean isDeviceTokenExist(String deviceToken);
}
