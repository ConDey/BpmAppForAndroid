package com.eazytec.bpm.app.webkit;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.eazytec.bpm.app.webkit.event.BPMJsMsgEvent;
import com.eazytec.bpm.app.webkit.event.BPMJsMsgImageEvent;
import com.eazytec.bpm.lib.common.webkit.CompletionHandler;
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
    public void setTitlebarVisible(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_VISIBLE, jsonObject.toString(), handler));
    }

    /**
     * 设置titlebar右面按钮
     * @param jsonObject
     */

    @JavascriptInterface
    public void setTitlebarRightBtn(JSONObject jsonObject, CompletionHandler handler) {
        String imgUrl = "";
        try {
            imgUrl = jsonObject.getString(URL);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(imgUrl)) {
            try {
                try {
                    Drawable image = Drawable.createFromStream(new URL(imgUrl).openStream(), "image");
                    EventBus.getDefault().post(new BPMJsMsgImageEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_RIGHT_IV_BGIMAGE, handler, image));
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
    public void setTitlebarBgColor(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_BGCOLOR, jsonObject.toString(), handler));
    }

    /**
     * 设置titlebar背景图片
     */
    public static final String API_PARAM_TITLEBAR_BGIMAGE = "bgImgUrl";

    @JavascriptInterface
    public void setTitlebarBgImage(JSONObject jsonObject, CompletionHandler handler) {
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
                    EventBus.getDefault().post(new BPMJsMsgImageEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_BGIMAGE, handler, image));
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
    public void setTitlebarTextView(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLE, jsonObject.toString(), handler));
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
    public static final String API_PARAM_AUTO_OPEN = "autoOpen";

    @JavascriptInterface
    public void downloadFile(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_DOWNLOAD_FILE, jsonObject.toString(), handler));
    }

    /**
     * 文件上传
     *
     * @param jsonObject
     */
    public static final String API_PARAM_FILE_PATH = "filePath";

    @JavascriptInterface
    public void uploadFile(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_UPLOAD_FILE, jsonObject.toString(), handler));
    }

    /**
     * 获得当前用户信息
     */
    @JavascriptInterface
    public String getUser(JSONObject jsonObject) {
            return activity.getUser();
    }

    /**
     * 获得Token
     */
    @JavascriptInterface
    public String getToken(JSONObject jsonObject) {
            return activity.getToken();
    }

    /**
     * 选择本地图片
     */
    protected static final String API_PARAM_IMAGE_SELECTOR_NUM = "selectNum";
    protected static final String API_PARAM_IMAGE_CHOOSE_MODE = "chooseMode";

    @JavascriptInterface
    public void getImage(JSONObject jsonObject, CompletionHandler handler) {
        try {
            int selectNum = jsonObject.getInt(API_PARAM_IMAGE_SELECTOR_NUM);
            int chooseMode = jsonObject.getInt(API_PARAM_IMAGE_CHOOSE_MODE);

            if (selectNum > 9) {
                selectNum = 9; // 最多选择9张
            }

            activity.getImage(chooseMode, selectNum);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
