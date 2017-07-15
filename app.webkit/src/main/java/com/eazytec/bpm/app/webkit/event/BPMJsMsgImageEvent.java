package com.eazytec.bpm.app.webkit.event;

import android.graphics.drawable.Drawable;

/**
 * @version Id: BPMJsMsgImageEvent, v 0.1 2017-7-14 8:01 16735 Exp $$
 * @author 16735
 */
public class BPMJsMsgImageEvent extends BPMJsMsgEvent {

    private Drawable image;

    public BPMJsMsgImageEvent(@BPM_JS_ID String id, Drawable img) {
        setId(id);
        this.image = img;
    }

    public BPMJsMsgImageEvent(@BPM_JS_ID String id, String message, Drawable img) {
        setId(id);
        setMessage(message);
        this.image = img;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
