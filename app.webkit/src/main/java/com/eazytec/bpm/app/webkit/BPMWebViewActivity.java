package com.eazytec.bpm.app.webkit;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eazytec.bpm.lib.common.activity.WebViewActivity;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.bundle.BundleApplication;
import com.eazytec.bpm.lib.common.webkit.JsWebView;
import com.eazytec.bpm.lib.common.webkit.WebViewUtil;
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
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpmwebview);

        toolbar = (Toolbar) findViewById(R.id.tb_common_toolbar);
        toolbarTitleTextView = (TextView) findViewById(R.id.tv_common_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        // 以Intent为准
        String title = getIntent().getStringExtra(INTENT_TITLE);
        if (!StringUtils.isSpace(title)) {
            toolbarTitleTextView.setText(title);
        }

        String url = getIntent().getStringExtra(INTENT_URL);
        if (!StringUtils.isSpace(url)) {
            this.url = getIntent().getStringExtra(INTENT_URL);
        }

        // 测试模式下自动加载本地的main
        // 这个代码要删除
        if (BundleApplication.isDebug()) {
            toolbarTitleTextView.setText("JSWEB");
            this.url = WebViewUtil.getLocalHTMLUrl("jswebview.html");
        }

        // eventBus注册事件
        EventBus.getDefault().register(this);
        initWebView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
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
     * 设置Toolbar的标题
     *
     * @param title
     */
    private void setToolbarTitle(String title) {
        toolbarTitleTextView.setText(title);
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
                setToolbarTitle(jsonObject.getString(BPMJsApi.API_PARAM_TITLE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
