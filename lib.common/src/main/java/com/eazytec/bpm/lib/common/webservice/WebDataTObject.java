package com.eazytec.bpm.lib.common.webservice;

/**
 * 标准通用的web通信数据交换DTO
 *
 * @author ConDey
 * @version Id: WebDataTObject, v 0.1 2017/6/2 上午11:10 ConDey Exp $$
 */
public class WebDataTObject {

    private boolean success;

    private String errorMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
