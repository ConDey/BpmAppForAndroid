package com.eazytec.bpm.app.webkit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eazytec.bpm.app.webkit.event.BPMJsMsgEvent;
import com.eazytec.bpm.app.webkit.event.BPMJsMsgImageEvent;
import com.eazytec.bpm.app.webkit.data.BaseCallbackBean;
import com.eazytec.bpm.lib.common.activity.WebViewActivity;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.bundle.BundleApplication;
import com.eazytec.bpm.lib.common.webkit.JsWebView;
import com.eazytec.bpm.lib.common.webkit.WebViewUtil;
import com.eazytec.bpm.lib.common.webservice.DownloadHelper;
import com.eazytec.bpm.lib.utils.EncodeUtils;
import com.eazytec.bpm.lib.utils.StringUtils;

import net.wequick.small.Small;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * BPM自带的远程webview插件，用于加载远程web页面
 *
 * @author ConDey
 * @version Id: BPMWebViewActivity, v 0.1 2017/7/3 上午8:39 ConDey Exp $$
 */
public class BPMWebViewActivity extends WebViewActivity {


    /**
     * 这个参数用于设置bpmwebview的标题，可以为空先不设置，由网页自己调用
     * setTitle方法设置
     */
    public static final String INTENT_TITLE = "title";


    /**
     * bpmwebview跳转的url地址，这个参数不能空，不然跳转的就是空页面
     */
    public static final String INTENT_URL = "url";

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;
    private ImageView toolbarRightIv;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpmwebview);

        //DownloadHelper.download(this, "ff8080815d3aa2af015d3ed479200920", "ic_flicker_bg.png");

        toolbar = (Toolbar) findViewById(R.id.tb_common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);

        toolbarTitleTextView = (TextView) findViewById(R.id.tv_common_toolbar);
        toolbarRightIv = (ImageView) findViewById(R.id.iv_right_common_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("xxx","xx");
                if (jsWebView != null && jsWebView.canGoBack()) {
                    jsWebView.goBack();
                } else {
                    finish();
                }
            }
        });

        if (!BundleApplication.isDebug()) {
            Uri uri = Small.getUri(this);
            if (uri != null) {
                String title = uri.getQueryParameter(INTENT_TITLE);
                if (!StringUtils.isSpace(title)) {
                    toolbarTitleTextView.setText(title);
                }
                url = EncodeUtils.urlDecode(uri.getQueryParameter(INTENT_URL)).toString();
            }
        }

        // 测试模式下自动加载本地的main
        // 这个代码要删除
        if (BundleApplication.isDebug()) {
            toolbarTitleTextView.setText("JSWEB");
            this.url = WebViewUtil.getLocalHTMLUrl("jswebview.html");
        }

        // 以Intent为准
        String title = getIntent().getStringExtra(INTENT_TITLE);
        if (!StringUtils.isSpace(title)) {
            toolbarTitleTextView.setText(title);
        }

        String url = getIntent().getStringExtra(INTENT_URL);
        if (!StringUtils.isSpace(url)) {
            this.url = getIntent().getStringExtra(INTENT_URL);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        // eventBus注册事件
        EventBus.getDefault().register(this);
        initWebView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override protected JsWebView jsWebView() {
        return (JsWebView) findViewById(R.id.wv_bpmwebview);
    }

    @Override protected ProgressBar progressBar() {
        return (ProgressBar) findViewById(R.id.pb_bpmwebview);
    }

    @Override protected String url() {
        return url;
    }


    @Override protected HashMap<String, String> headers() {

        // 需要设置Token到Header里去
        HashMap<String, String> headers = new HashMap<>();
        if (CurrentUser.getCurrentUser().isLogin()) {
            headers.put("token", CurrentUser.getCurrentUser().getToken().toString());
        }
        return headers;
    }

    @Override protected Object jsApi() {
        return new BPMJsApi(this);
    }

    /**
     *设置titlebar的隐藏与显示
     */
    private void setTitleBarVisible(Boolean isVisible) {
        if (isVisible) {
            toolbar.setVisibility(View.VISIBLE);
        }else {
            toolbar.setVisibility(View.GONE);
        }
    }

    /**
     *设置titlebar的背景颜色
     */
    private void setTitleBarBgColor(String color) {
        if (!StringUtils.isEmpty(color)) {
            toolbar.setBackgroundColor(Color.parseColor(color));
        }
    }

    /**
     *设置titlebar的背景图片
     */
    private void setTitleBarBgImage(Drawable image) {
        if (Build.VERSION.SDK_INT > 15) {
            toolbar.setBackground(image);
        }else {
            toolbar.setBackgroundDrawable(image);
        }
    }

    /**
     *设置titlebar右边按钮的背景图片
     */
    private void setTitleBarRightBtnBgImage(Drawable image) {
        if (Build.VERSION.SDK_INT > 15) {
            toolbarRightIv.setBackground(image);
        }else {
            toolbarRightIv.setBackgroundDrawable(image);
        }
    }

    /**
     *设置titlebar右边按钮的Callback
     */
    private void setTitleBarRightBtnCallback(final String callback, final JSONObject jsonObject) {
        toolbarRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsWebView.callHandler(callback, new Object[]{jsonObject.toString()});
            }
        });
    }

    /**
     * 设置Toolbar的标题
     *
     * @param title
     */
    private void setToolbarTitle(String title, String fontSize, String fontColor) {
        if (!StringUtils.isEmpty(title)) {
            toolbarTitleTextView.setText(title);
        }
        if (!StringUtils.isEmpty(fontSize)) {
            toolbarTitleTextView.setTextSize(Float.valueOf(fontSize));
        }
        if (!StringUtils.isEmpty(fontColor)) {
            toolbarTitleTextView.setTextColor(Color.parseColor(fontColor));
        }

    }

    /**
     *  在Main线程执行的订阅
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainMessageEvent(BPMJsMsgEvent messageEvent) {
        if (StringUtils.equals(messageEvent.getId(), BPMJsMsgEvent.JS_SET_TITLE)) {
            try {
                JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                setToolbarTitle(jsonObject.getString(BPMJsApi.API_PARAM_TITLE),
                                jsonObject.getString(BPMJsApi.API_PARAM_FONT_SIZE),
                                jsonObject.getString(BPMJsApi.API_PARAM_FONT_COLOR));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.equals(messageEvent.getId(), BPMJsMsgEvent.JS_SET_TITLEBAR_VISIBLE)) {
            try {
                JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                if (jsonObject.getString(BPMJsApi.API_PARAM_TITLEBAR_VISIBLE).equals("yes")) {
                    setTitleBarVisible(true);
                }else {
                    setTitleBarVisible(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.equals(messageEvent.getId(), BPMJsMsgEvent.JS_SET_TITLEBAR_BGCOLOR)) {
            try {
                JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                String color = jsonObject.getString(BPMJsApi.API_PARAM_TITLEBAR_BGCOLOR);
                setTitleBarBgColor(color);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /**
         *  因为DownloadHelper中用到了一些需要在主线程执行的UI操作。所以这里post到主线程
         */
        if (StringUtils.equals(messageEvent.getId(), BPMJsMsgEvent.JS_DOWNLOAD_FILE)) {
            try {
                JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                String id = jsonObject.getString(BPMJsApi.API_PARAM_ATTACHMENT_ID);
                String name = jsonObject.getString(BPMJsApi.API_PARAM_ATTACHMENT_NAME);
                DownloadHelper.download(this, id, name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *  在Main线程执行的图片订阅
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainMessageImageEvent(BPMJsMsgImageEvent messageEvent) {
        if (StringUtils.equals(messageEvent.getId(), BPMJsMsgEvent.JS_SET_TITLEBAR_BGIMAGE)) {
            Drawable image = messageEvent.getImage();
            setTitleBarBgImage(image);
        }
        if (StringUtils.equals(messageEvent.getId(), BPMJsMsgEvent.JS_SET_TITLEBAR_RIGHT_IV_BGIMAGE)) {
            Drawable image = messageEvent.getImage();
            String callback = messageEvent.getMessage();
            setTitleBarRightBtnBgImage(image);

            // 构造回调json数据
            BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
            JSONObject object = new JSONObject(callbackBean.toJson());

            setTitleBarRightBtnCallback(callback, object);

        }
    }

    /**
     * 新建webview的回调方法
     */
    public void startWebViewActivity(String htmlUrl, String title) {
        Intent it = new Intent(this, BPMWebViewActivity.class);
        it.putExtra(INTENT_URL, htmlUrl);
        it.putExtra(INTENT_TITLE, title);
        startActivity(it);
    }

    public void skipWebViewActivity(String htmlUrl, String title) {
        Intent it = new Intent(this, BPMWebViewActivity.class);
        it.putExtra(INTENT_URL, htmlUrl);
        it.putExtra(INTENT_TITLE, title);
        skipActivity(this, it);
    }


}
