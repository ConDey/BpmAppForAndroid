package com.eazytec.bpm.lib.common.webservice.progress;

/**
 * @author Administrator
 * @version Id: ProgressListener, v 0.1 2017/7/10 16:49 Administrator Exp $$
 */
public interface ProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
