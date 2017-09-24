package com.eazytec.bpm.app.webkit.event;

import android.graphics.drawable.Drawable;

import com.eazytec.bpm.lib.common.webkit.CompletionHandler;

/**
 * @version Id: BPMJsMsgImageEvent, v 0.1 2017-7-14 8:01 16735 Exp $$
 * @author 16735
 */
public class BPMJsMsgImageEvent extends BPMJsMsgEvent {

    private Drawable image;
    public BPMJsMsgImageEvent(){
    }

    public BPMJsMsgImageEvent(@BPM_JS_ID String id, Drawable img) {
        setId(id);
        this.image = img;
    }

    public BPMJsMsgImageEvent(@BPM_JS_ID String id, CompletionHandler handler, Drawable img) {
        setId(id);
        setHandler(handler);
        this.image = img;
    }

    public BPMJsMsgImageEvent(@BPM_JS_ID String id, CompletionHandler handler, Drawable img,String message) {
        setId(id);
        setHandler(handler);
        this.image = img;
        setMessage(message);
    }

    public BPMJsMsgImageEvent(@BPM_JS_ID String id, Drawable img,String message) {
        setId(id);
        this.image = img;
        setMessage(message);
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }


}
