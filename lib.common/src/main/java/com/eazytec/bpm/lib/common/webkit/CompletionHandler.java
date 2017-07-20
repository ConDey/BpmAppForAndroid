package com.eazytec.bpm.lib.common.webkit;

/**
 * jswebview的行为接口定义
 */
public interface CompletionHandler {

    void complete(String retValue);
    void complete();
    void setProgressData(String value);
}
