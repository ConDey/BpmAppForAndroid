package com.eazytec.bpm.app.home.data.app;

import android.content.Context;

/**
 * 代表可安装的插件行为
 *
 * @author ConDey
 * @version Id: Installable, v 0.1 2017/7/1 上午10:23 ConDey Exp $$
 */
public interface Installable {


    /**
     * 返回插件或应用是否被安装
     *
     * @return
     */
    public boolean installed();


    /**
     * 安装应用或插件
     */
    public void install(Context context);


    /**
     * 是否允许安装此插件
     *
     * @return
     */
    public boolean canInstall();


}
