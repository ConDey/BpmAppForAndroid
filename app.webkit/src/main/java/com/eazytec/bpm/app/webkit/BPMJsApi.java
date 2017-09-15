package com.eazytec.bpm.app.webkit;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.eazytec.bpm.app.webkit.data.TokenCallbackBean;
import com.eazytec.bpm.app.webkit.data.UserCallbackBean;
import com.eazytec.bpm.app.webkit.event.BPMJsMsgEvent;
import com.eazytec.bpm.app.webkit.event.BPMJsMsgImageEvent;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.authentication.UserDetails;
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

    /**
     * 设置titleBar的隐藏与显示
     */
    public static final String API_PARAM_TITLEBAR_VISIBLE = "visible";
    /**
     * 设置titlebar背景颜色
     */
    public static final String API_PARAM_TITLEBAR_BGCOLOR = "bgColor";
    /**
     * 设置titlebar背景图片
     */
    public static final String API_PARAM_TITLEBAR_BGIMAGE = "bgImgUrl";
    /**
     * 设置当前页面的Toobar标题
     */
    public static final String API_PARAM_TITLE = "title";
    public static final String API_PARAM_FONT_SIZE = "fontSize";
    public static final String API_PARAM_FONT_COLOR = "fontColor";
    /**
     * 新建WebViewActivity
     */
    public static final String API_PARAM_NEW_WEBVIEW_TITLE = "title";
    /**
     * 文件下载
     *
     * @param jsonObject
     */
    public static final String API_PARAM_ATTACHMENT_ID = "attachmentId";
    public static final String API_PARAM_ATTACHMENT_NAME = "attachmentName";
    public static final String API_PARAM_AUTO_OPEN = "autoOpen";
    /**
     * 文件上传
     *
     * @param jsonObject
     */
    public static final String API_PARAM_FILE_PATH = "filePath";
    /**
     * 文件选择
     *
     * @param jsonObject
     */
    public static final String API_PARAM_FILE_NUM = "selectNum";
    protected static final String CALL_BACK = "callback";
    protected static final String URL = "url";
    /**
     * 选择本地图片
     */
    protected static final String API_PARAM_IMAGE_SELECTOR_NUM = "selectNum";
    protected static final String API_PARAM_IMAGE_SELECTOR_USERS = "users";
    private BPMWebViewActivity activity;


    public static final String TOAST_INFO = "toast";

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

    @JavascriptInterface
    public void setTitlebarVisible(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_VISIBLE, jsonObject.toString(), handler));
    }

    /**
     * 设置titlebar右面按钮
     *
     * @param jsonObject
     */

    @JavascriptInterface
    public void setTitlebarRightBtn(JSONObject jsonObject, CompletionHandler handler) {
        String imgUrl = "";
        try {
            imgUrl = jsonObject.getString(URL);
        } catch (JSONException e) {
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

    @JavascriptInterface
    public void setTitlebarBgColor(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLEBAR_BGCOLOR, jsonObject.toString(), handler));
    }

    @JavascriptInterface
    public void setTitlebarBgImage(JSONObject jsonObject, CompletionHandler handler) {
        String imgUrl = "";
        try {
            imgUrl = jsonObject.getString(API_PARAM_TITLEBAR_BGIMAGE);
        } catch (JSONException e) {
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

    @JavascriptInterface
    public void setTitlebarTextView(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_SET_TITLE, jsonObject.toString(), handler));
    }

    @JavascriptInterface
    public void startWindow(JSONObject jsonObject) {
        try {
            String url = jsonObject.getString(URL);
            String title = jsonObject.getString(API_PARAM_NEW_WEBVIEW_TITLE);
            if (activity != null) {
                activity.startWebViewActivity(url, title);
            }
        } catch (JSONException e) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void downloadFile(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_DOWNLOAD_FILE, jsonObject.toString(), handler));
    }

    @JavascriptInterface
    public void uploadFile(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_UPLOAD_FILE, jsonObject.toString(), handler));
    }

    @JavascriptInterface
    public void fileSelector(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_FILE_SELECT, jsonObject.toString(), handler));
    }

    /**
     * 获得当前用户信息
     */
    @JavascriptInterface
    public void getUser(JSONObject jsonObject, CompletionHandler handler) {
        // 构造回调json数据
        UserCallbackBean callbackBean;
        UserDetails user = CurrentUser.getCurrentUser().getUserDetails();
        if (user != null) {
            callbackBean = new UserCallbackBean(true, "", user);
        } else {
            callbackBean = new UserCallbackBean(false, "当前用户不存在", null);
        }
        JSONObject jsonObj = new JSONObject(callbackBean.toJson());
        handler.complete(jsonObj.toString());
    }

    /**
     * 获得Token
     */
    @JavascriptInterface
    public void getToken(JSONObject jsonObject, CompletionHandler handler) {
        // 构造回调json数据
        TokenCallbackBean callbackBean;
        String token = CurrentUser.getCurrentUser().getToken().toString();
        if (!StringUtils.isEmpty(token)) {
            callbackBean = new TokenCallbackBean(true, "", token);
        } else {
            callbackBean = new TokenCallbackBean(false, "Token不存在", null);
        }
        JSONObject jsonObj = new JSONObject(callbackBean.toJson());
        handler.complete(jsonObj.toString());

    }

    @JavascriptInterface
    public void getImages(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_GET_IMAGES, jsonObject.toString(), handler));
    }

    /**
     * 选择本地视频
     */
    @JavascriptInterface
    public void getVideos(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_GET_VIDEOS, jsonObject.toString(), handler));
    }

    @JavascriptInterface
    public void userchoose(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_USER_CHOOSE, jsonObject.toString(), handler));
    }

    @JavascriptInterface
    public void bindBackBtn(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_BIND_BACKBTN, jsonObject.toString(), handler));
    }

    @JavascriptInterface
    public void unbindBackBtn(JSONObject jsonObject, CompletionHandler handler) {
        EventBus.getDefault().post(new BPMJsMsgEvent(BPMJsMsgEvent.JS_UNBIND_BACKBTN, jsonObject.toString(), handler));
    }


    @JavascriptInterface
    public void toastshow(JSONObject jsonObject) {
        try {
            String info = jsonObject.getString(TOAST_INFO);
            if(activity != null){
                activity.ToastInfo(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
