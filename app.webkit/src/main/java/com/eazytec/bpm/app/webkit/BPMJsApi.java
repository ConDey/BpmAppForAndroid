package com.eazytec.bpm.app.webkit;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.eazytec.bpm.app.webkit.event.BPMJsMsgEvent;
import com.eazytec.bpm.app.webkit.event.BPMJsMsgImageEvent;
import com.eazytec.bpm.lib.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 通用的JsApi, 所有插件如果要扩展JsApi必须继承此类
 *
 * @author ConDey
 * @version Id: BPMJsApi, v 0.1 2017/7/3 下午1:40 ConDey Exp $$
 */
public class BPMJsApi {

    protected static final String CALL_BACK = "callback";
    protected static final String URL = "url";

    private BPMWebViewActivity activity;

    /**
     * 必须注入Activity
     *
     * @param activity
     */
    public BPMJsApi(BPMWebViewActivity activity) {
        this.activity = activity;
    }

    /**
     * 关闭此页面
     */
    @JavascriptInterface
    public void close(JSONObject jsonObject) {
        if (activity != null) {
            activity.finish();
        }
    }



    /**
     * 设置titleBar的隐藏与显示
     */
    public static final String API_PARAM_TITLEBAR_VISIBLE = "visible";

    @JavascriptInterface
    public void setTitlebarVisible(JSONObject jsonObject) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_VISIBLE, jsonObject.toString()));
    }

    /**
     * 设置titlebar右面按钮
     * @param jsonObject
     */

    @JavascriptInterface
    public void setTitlebarRightBtn(JSONObject jsonObject) {
        String imgUrl = "";
        String callback = "";
        try {
            imgUrl = jsonObject.getString(URL);
            callback = jsonObject.getString(CALL_BACK);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(imgUrl)) {
            try {
                try {
                    Drawable image = Drawable.createFromStream(new URL(imgUrl).openStream(), "image");
                    EventBus.getDefault().post(new BPMJsMsgImageEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_RIGHT_IV_BGIMAGE, callback, image));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "resolve image url failed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置titlebar背景颜色
     */
    public static final String API_PARAM_TITLEBAR_BGCOLOR = "bgColor";

    @JavascriptInterface
    public void setTitlebarBgColor(JSONObject jsonObject) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_BGCOLOR, jsonObject.toString()));
    }

    /**
     * 设置titlebar背景图片
     */
    public static final String API_PARAM_TITLEBAR_BGIMAGE = "bgImgUrl";

    @JavascriptInterface
    public void setTitlebarBgImage(JSONObject jsonObject) {
        String imgUrl = "";
        try {
            imgUrl = jsonObject.getString(API_PARAM_TITLEBAR_BGIMAGE);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(imgUrl)) {
            try {
                try {
                    Drawable image = Drawable.createFromStream(new URL(imgUrl).openStream(), "image");
                    EventBus.getDefault().post(new BPMJsMsgImageEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_BGIMAGE, image));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "resolve image url failed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置当前页面的Toobar标题
     */
    public static final String API_PARAM_TITLE = "title";
    public static final String API_PARAM_FONT_SIZE = "fontSize";
    public static final String API_PARAM_FONT_COLOR = "fontColor";
    @JavascriptInterface
    public void setTitlebarTextView(JSONObject jsonObject) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLE, jsonObject.toString()));
    }

    /**
     * 新建WebViewActivity
     */
    public static final String API_PARAM_NEW_WEBVIEW_TITLE = "title";

    @JavascriptInterface
    public void startWindow(JSONObject jsonObject) {
        try {
            String url = jsonObject.getString(URL);
            String title = jsonObject.getString(API_PARAM_NEW_WEBVIEW_TITLE);
            if (activity != null) {
                activity.startWebViewActivity(url, title);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void skipWindow(JSONObject jsonObject) {
        try {
            String url = jsonObject.getString(URL);
            String title = jsonObject.getString(API_PARAM_NEW_WEBVIEW_TITLE);
            if (activity != null) {
                activity.skipWebViewActivity(url, title);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件下载
     *
     * @param jsonObject
     */
    public static final String API_PARAM_ATTACHMENT_ID = "attachmentId";
    public static final String API_PARAM_ATTACHMENT_NAME = "attachmentName";

    @JavascriptInterface
    public void downloadFile(JSONObject jsonObject) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_DOWNLOAD_FILE, jsonObject.toString()));
    }

}
