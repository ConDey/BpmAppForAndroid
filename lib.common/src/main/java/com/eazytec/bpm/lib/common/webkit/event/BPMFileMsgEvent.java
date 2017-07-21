package com.eazytec.bpm.lib.common.webkit.event;

import android.support.annotation.StringDef;

import java.util.ArrayList;

/**
 * @author 16735
 * @version Id: BPMFileMsgEvent, v 0.1 2017-7-20 19:30 16735 Exp $$
 */
public class BPMFileMsgEvent {

    public static final String FILE_SELECT = "FILE_SELECT";

    // 自定义一个注解MyState
    @StringDef({FILE_SELECT})
    public @interface BPM_JS_ID {
    }

    private String id;

    private ArrayList<String> message;

    public BPMFileMsgEvent() {

    }

    /**
     * 默认的构造函数
     *
     * @param id
     * @param message
     */
    public BPMFileMsgEvent(@BPM_JS_ID String id, ArrayList<String> message) {
        this.id = id;
        this.message = message;
    }

    @BPM_JS_ID
    public String getId() {
        return id;
    }

    public void setId(@BPM_JS_ID String id) {
        this.id = id;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

}
