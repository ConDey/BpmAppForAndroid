package com.eazytec.bpm.lib.common.webservice.progress;

/**
 * @author Administrator
 * @version Id: ProgressBean, v 0.1 2017/7/10 16:47 Administrator Exp $$
 */
public class ProgressBean {

    private long bytesRead;
    private long contentLength;
    private boolean done;

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
