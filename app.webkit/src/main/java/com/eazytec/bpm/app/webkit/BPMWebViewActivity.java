package com.eazytec.bpm.app.webkit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.eazytec.bpm.app.webkit.data.BaseCallbackBean;
import com.eazytec.bpm.app.webkit.data.FileCallbackBean;
import com.eazytec.bpm.app.webkit.data.LocationCallbackBean;
import com.eazytec.bpm.app.webkit.data.MediaCallbackBean;
import com.eazytec.bpm.app.webkit.data.UserChooseCallbackBean;
import com.eazytec.bpm.app.webkit.event.BPMJsMsgEvent;
import com.eazytec.bpm.app.webkit.event.BPMJsMsgImageEvent;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.WebViewActivity;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.bundle.BundleApplication;
import com.eazytec.bpm.lib.common.webkit.CompletionHandler;
import com.eazytec.bpm.lib.common.webkit.JsWebView;
import com.eazytec.bpm.lib.common.webkit.JsWebViewActiEvent;
import com.eazytec.bpm.lib.common.webkit.WebViewUtil;
import com.eazytec.bpm.lib.common.webservice.DownloadHelper;
import com.eazytec.bpm.lib.common.webservice.UploadHelper;
import com.eazytec.bpm.lib.utils.EncodeUtils;
import com.eazytec.bpm.lib.utils.MIMETypeUtil;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;

import net.wequick.small.Small;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;

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
    /**
     * 图片选择
     */
    private static final int COMPRESSMODE = PictureConfig.SYSTEM_COMPRESS_MODE;  //压缩模式，系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
    private List<LocalMedia> selectList = new ArrayList<>();
    private boolean hasBindBackButton = false;
    private String backBtonCallBack = "";
    private Toolbar toolbar;
    private TextView toolbarTitleTextView;
    private ImageView toolbarRightIv;
    private String url;
    private BPMWebViewActivity activity;

    private String rightBtnHtmlUrl = "";
    private String rightBtnAcType = "";
    private String rightBtnAcTitle = "";
    private CompletionHandler rightButtonhandler;
    private JSONObject rightobject;
    private String rightBtnInfo = "";
    private String rightBtnTitle = "";
    private String rightBtnCallBack = "";
    private String alterCallBack = "";

    // 单独为文件上传下载服务
    private CompletionHandler mHandler;
    // 图片下载
    private CompletionHandler mediaHandler;
    // 文件选择
    private CompletionHandler fileHandler;
    // 人员选择
    private CompletionHandler userchooseHandler;
    //定位信息
    private CompletionHandler locationHandler;

    private MaterialDialog progressDialog;


    // 定位相关
    private LocationClient mLocationClient;
    private boolean isFirstLocation = true;

    /**
     * toolbar的事件全在里面
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpmwebview);


        toolbar = (Toolbar) findViewById(R.id.tb_common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);

        toolbarTitleTextView = (TextView) findViewById(R.id.tv_common_toolbar);
        toolbarRightIv = (ImageView) findViewById(R.id.iv_right_common_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasBindBackButton && !StringUtils.isSpace(backBtonCallBack)) {
                    jsWebView.callHandler(backBtonCallBack, new Object[]{});
                } else {
                    if (jsWebView != null && jsWebView.canGoBack()) {
                        jsWebView.goBack();
                    } else {
                        finish();
                    }
                }
            }
        });

        toolbarRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(!StringUtils.isSpace(rightBtnCallBack)){

                 jsWebView.callHandler(rightBtnCallBack, new Object[]{});
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
        initWebView();
        initLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // eventBus注册事件
        EventBus.getDefault().register(this);
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

    public void syncCookie(CookieManager cookieManager) {
        super.syncCookie(cookieManager);

        // 加入BPM的Token
        cookieManager.setCookie(Config.WEB_SERVICE_URL, "token=" + CurrentUser.getCurrentUser().getToken().toString());

    }

    @Override protected Object jsApi() {
        return new BPMJsApi(this);
    }

    /**
     * 设置titlebar的隐藏与显示
     */
    private void setTitleBarVisible(Boolean isVisible, CompletionHandler handler, String result) {
        if (isVisible) {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        handler.complete(result);
    }


    /**
     * 设置titlebar的背景颜色
     */
    private void setTitleBarBgColor(String color, CompletionHandler handler, String result) {
        if (!StringUtils.isEmpty(color)) {
            toolbar.setBackgroundColor(Color.parseColor(color));
            handler.complete(result);
        }
    }

    /**
     * 设置titlebar的背景图片
     */
    private void setTitleBarBgImage(Drawable image, CompletionHandler handler, String result) {
        if (Build.VERSION.SDK_INT > 15) {
            toolbar.setBackground(image);
        } else {
            toolbar.setBackgroundDrawable(image);
        }
        handler.complete(result);
    }

    /**
     * 设置titlebar右边按钮的背景图片
     */
    private void setTitleBarRightBtnBgImage(Drawable image) {
            toolbarRightIv.setImageDrawable(image);
    }


    /**
     * 设置Toolbar的标题
     *
     * @param title
     */
    private void setToolbarTitle(String title, String fontSize, String fontColor, CompletionHandler handler, String result) {
        if (!StringUtils.isEmpty(title)) {
            toolbarTitleTextView.setText(title);
        }
        if (!StringUtils.isEmpty(fontSize)) {
            toolbarTitleTextView.setTextSize(Float.valueOf(fontSize));
        }
        if (!StringUtils.isEmpty(fontColor)) {
            toolbarTitleTextView.setTextColor(Color.parseColor(fontColor));
        }
        handler.complete(result);

    }

    /**
     * 在Main线程执行的订阅
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainMessageEvent(BPMJsMsgEvent messageEvent) {
        switch (messageEvent.getId()) {
            case BPMJsMsgEvent.JS_SET_TITLE:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                    JSONObject object = new JSONObject(callbackBean.toJson());

                    setToolbarTitle(jsonObject.getString(BPMJsApi.API_PARAM_TITLE),
                            jsonObject.getString(BPMJsApi.API_PARAM_FONT_SIZE),
                            jsonObject.getString(BPMJsApi.API_PARAM_FONT_COLOR),
                            handler,
                            object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case BPMJsMsgEvent.JS_SET_TITLEBAR_VISIBLE:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                    JSONObject object = new JSONObject(callbackBean.toJson());

                    if (jsonObject.getBoolean(BPMJsApi.API_PARAM_TITLEBAR_VISIBLE)) {
                        setTitleBarVisible(true, handler, object.toString());
                    } else {
                        setTitleBarVisible(false, handler, object.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case BPMJsMsgEvent.JS_SET_TITLEBAR_BGCOLOR:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                    JSONObject object = new JSONObject(callbackBean.toJson());

                    String color = jsonObject.getString(BPMJsApi.API_PARAM_TITLEBAR_BGCOLOR);
                    setTitleBarBgColor(color, handler, object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            /**
             *  因为DownloadHelper中用到了一些需要在主线程执行的UI操作。所以这里post到主线程
             */
            case BPMJsMsgEvent.JS_DOWNLOAD_FILE:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                    JSONObject object = new JSONObject(callbackBean.toJson());
                    mHandler = handler;
                    //result = object.toString();

                    final String id = jsonObject.getString(BPMJsApi.API_PARAM_ATTACHMENT_ID);
                    final String name = jsonObject.getString(BPMJsApi.API_PARAM_ATTACHMENT_NAME);
                    final boolean isAutoOpen = jsonObject.getBoolean(BPMJsApi.API_PARAM_AUTO_OPEN);

                    RxPermissions rxPermissions = new RxPermissions(BPMWebViewActivity.this);
                    rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if(aBoolean){
                                        DownloadHelper.download(BPMWebViewActivity.this, id, name, isAutoOpen, mHandler);
                                    }else{
                                        ToastDelegate.error(getContext(), "您没有授权存储权限，请到设置里设置权限！");
                                    }
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case BPMJsMsgEvent.JS_UPLOAD_FILE:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                    JSONObject object = new JSONObject(callbackBean.toJson());
                    mHandler = handler;
                    //result = object.toString();

                    String filePath = jsonObject.getString(BPMJsApi.API_PARAM_FILE_PATH);
                    final File file = new File(filePath);
                    if (!file.exists()) {
                        ToastDelegate.error(this, "文件不存在");
                        return;
                    }
                    RxPermissions rxPermissions = new RxPermissions(BPMWebViewActivity.this);
                    rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if(aBoolean){
                                        UploadHelper.upload(BPMWebViewActivity.this, file, mHandler);
                                    }else{
                                        ToastDelegate.error(getContext(), "您没有授权存储权限，请到设置里设置权限！");
                                    }
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case BPMJsMsgEvent.JS_FILE_SELECT:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    int selectNum = jsonObject.getInt(BPMJsApi.API_PARAM_FILE_NUM);
                    final String selectNumStr = String.valueOf(selectNum);
                    fileHandler = handler;
                    //获取权限
                    RxPermissions rxPermissions = new RxPermissions(BPMWebViewActivity.this);
                    rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if(aBoolean){
                                        Small.openUri("app.filepicker/forfilepicker?CUSTOM_MAX_COUNT=" + selectNumStr, getContext());
                                    }else{
                                        ToastDelegate.error(getContext(), "您没有授权存储权限，请到设置里设置权限！");
                                    }
                                }
                            });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case BPMJsMsgEvent.JS_GET_IMAGES:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                    JSONObject object = new JSONObject(callbackBean.toJson());
                    int selectNum = jsonObject.getInt(BPMJsApi.API_PARAM_IMAGE_SELECTOR_NUM);
                    int chooseMode = 1;

                    if (selectNum > 9) {
                        selectNum = 9; // 最多选择9张
                    }

                    getImages(chooseMode, selectNum, handler);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case BPMJsMsgEvent.JS_GET_VIDEOS:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                    JSONObject object = new JSONObject(callbackBean.toJson());
                    int selectNum = jsonObject.getInt(BPMJsApi.API_PARAM_IMAGE_SELECTOR_NUM);
                    int chooseMode = 2;

                    if (selectNum > 9) {
                        selectNum = 9; // 最多选择9张
                    }

                    getImages(chooseMode, selectNum, handler);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


            case BPMJsMsgEvent.JS_BIND_BACKBTN:

                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();

                    hasBindBackButton = true;
                    backBtonCallBack = jsonObject.getString(BPMJsApi.CALL_BACK);

                    if (handler != null) {

                        BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                        JSONObject object = new JSONObject(callbackBean.toJson());
                        handler.complete(object.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case BPMJsMsgEvent.JS_UNBIND_BACKBTN:
                hasBindBackButton = false;
                backBtonCallBack = null;

                if (true) {
                    CompletionHandler handler = messageEvent.getHandler();
                    if (handler != null) {

                        BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                        JSONObject object = new JSONObject(callbackBean.toJson());
                        handler.complete(object.toString());
                    }
                }
                break;


            case BPMJsMsgEvent.JS_USER_CHOOSE:

                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    userchooseHandler = handler;
                    if (handler != null) {

                        int selectNum = jsonObject.getInt(BPMJsApi.API_PARAM_IMAGE_SELECTOR_NUM);

                        StringBuffer selectUsersBuffer = new StringBuffer();
                        if (jsonObject.has(BPMJsApi.API_PARAM_IMAGE_SELECTOR_USERS)) {

                            JSONArray jsonArray = jsonObject.getJSONArray(BPMJsApi.API_PARAM_IMAGE_SELECTOR_USERS);
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int index = 0; index < jsonArray.length(); index++) {

                                    JSONObject userObject = jsonArray.getJSONObject(index);
                                    selectUsersBuffer.append(userObject.get("id"));
                                    selectUsersBuffer.append("-");
                                    selectUsersBuffer.append(userObject.get("name"));

                                    if (index != jsonArray.length() - 1) {
                                        selectUsersBuffer.append(",");
                                    }
                                }

                            }
                        }

                        String uri = "app.contact/forcontactchoose?numdatas=" + selectNum + "&choosedatas=" + selectUsersBuffer.toString();
                        Small.openUri(uri, getContext());

                    }
                } catch (JSONException e) {

                }
                break;

            //获得定位信息
            case BPMJsMsgEvent.JS_GET_LOCATION:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handler = messageEvent.getHandler();
                    locationHandler = handler;
                    if (handler != null) {
                        isFirstLocation = true;
                        initLocation();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            /**
             * alert回调
             */
            case BPMJsMsgEvent.JS_SET_DIALOG_SHOW_AL:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    final CompletionHandler handler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                    final JSONObject object = new JSONObject(callbackBean.toJson());
                    String title = jsonObject.getString(BPMJsApi.API_DIALOG_Title);
                    String info = jsonObject.getString(BPMJsApi.API_DIALOG_INFO);
                    dialogShowAl(title,info, handler, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            /**
             * alter触发js
             */
            case BPMJsMsgEvent.JS_BIND_ALTER:
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    final CompletionHandler handler = messageEvent.getHandler();
                    alterCallBack = jsonObject.getString(BPMJsApi.CALL_BACK);
                    if(handler!= null){
                        BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                        final JSONObject object = new JSONObject(callbackBean.toJson());
                        String title = jsonObject.getString(BPMJsApi.API_DIALOG_Title);
                        String info = jsonObject.getString(BPMJsApi.API_DIALOG_INFO);
                        dialogShowAl(title,info, handler, object);
                    }
                    // 构造回调json数据
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Dialog的callback
     */

    public void dialogShowAl(String title,String info, final CompletionHandler handler, final JSONObject object) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                .title(title)
                .content(info)
                .positiveText("确定")
                .negativeText("取消")
                .negativeColor(Color.RED)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(!StringUtils.isSpace(alterCallBack)){
                            jsWebView.callHandler(alterCallBack, new Object[]{});
                        }
                    }
                }).build();
        materialDialog.show();
    }

    /**
     * 在Main线程执行的图片订阅
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainMessageImageEvent(BPMJsMsgImageEvent messageEvent) {
        switch (messageEvent.getId()) {
            case BPMJsMsgEvent.JS_SET_TITLEBAR_BGIMAGE:
                Drawable image = messageEvent.getImage();
                CompletionHandler handler = messageEvent.getHandler();
                // 构造回调json数据
                BaseCallbackBean callbackBean = new BaseCallbackBean(true, StringUtils.blank());
                JSONObject object = new JSONObject(callbackBean.toJson());

                setTitleBarBgImage(image, handler, object.toString());
                break;

            case BPMJsMsgEvent.JS_BIND_RIGHTBTN:
                Drawable imageRight = messageEvent.getImage();
                setTitleBarRightBtnBgImage(imageRight);
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    CompletionHandler handlerEvent = messageEvent.getHandler();
                    rightBtnCallBack = jsonObject.getString(BPMJsApi.CALL_BACK);
                    if (handlerEvent != null) {
                        BaseCallbackBean callback = new BaseCallbackBean(true, StringUtils.blank());
                        JSONObject jsonObj = new JSONObject(callback.toJson());
                        handlerEvent.complete(jsonObj.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case BPMJsMsgEvent.JS_SET_TITLEBAR_RIGHT_IV_BGIMAGE:
                Drawable img = messageEvent.getImage();
                setTitleBarRightBtnBgImage(img);
                try {

                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    rightButtonhandler = messageEvent.getHandler();
                    // 构造回调json数据
                    BaseCallbackBean callbackBeanRt = new BaseCallbackBean(true, StringUtils.blank());
                    rightobject = new JSONObject(callbackBeanRt.toJson());

                   /* String htemlUrl=jsonObject.getString(BPMJsApi.API_HTML_URL);
                    String imgUrl=jsonObject.getString(BPMJsApi.API_IMAGE_URL);*/
                    rightBtnInfo = jsonObject.getString(BPMJsApi.API_DIALOG_INFO);
                   /* String rightBtnType=jsonObject.getString(BPMJsApi.API_RIGHT_BTN_TYPE);*/
                    rightBtnTitle = jsonObject.getString(BPMJsApi.API_DIALOG_Title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case BPMJsMsgEvent.JS_SET_TITLEBAR_RIGHT_IV_BGIMAGE_SEC:
                Drawable rimg = messageEvent.getImage();
                setTitleBarRightBtnBgImage(rimg);
                try {
                    JSONObject jsonObject = new JSONObject(messageEvent.getMessage());
                    rightBtnAcType = jsonObject.getString(BPMJsApi.API_RIGHT_BTN_TYPE);
                    rightBtnAcTitle = jsonObject.getString(BPMJsApi.API_RIGHT_AC_TITLE);
                    rightBtnHtmlUrl = jsonObject.getString(BPMJsApi.API_HTML_URL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }


    /**
     * 新建webview的回调方法
     */


    public void startWebViewActivity(String htmlUrl, String title) {
        Intent it = new Intent(this, BPMWebViewActivity.class);

        if (htmlUrl.startsWith("http:") ||
                htmlUrl.startsWith("https:") ||
                htmlUrl.startsWith("file:")) {
            it.putExtra(INTENT_URL, htmlUrl);
        } else {

            it.putExtra(INTENT_URL, Config.WEB_SERVICE_URL + htmlUrl);
        }

        it.putExtra(INTENT_TITLE, title);
        startActivity(it);
    }

    public void skipWebViewActivity(String htmlUrl, String title) {
        Intent it = new Intent(this, BPMWebViewActivity.class);
        if (htmlUrl.startsWith("http:") ||
                htmlUrl.startsWith("https:") ||
                htmlUrl.startsWith("file:")) {
            it.putExtra(INTENT_URL, htmlUrl);
        } else {
            it.putExtra(INTENT_URL, Config.WEB_SERVICE_URL + htmlUrl);
        }
        it.putExtra(INTENT_TITLE, title);
        skipActivity(this, it);
    }

    /**
     * toast显示
     */
    public void toastInfo(String info, String type) {
        switch (type) {
            case "info":
                ToastDelegate.info(getContext(), info);
                break;
            case "error":
                ToastDelegate.info(getContext(), info);
                break;
            case "success":
                ToastDelegate.success(getContext(), info);
                break;
            case "normal":
                ToastDelegate.normal(getContext(), info);
                break;
            case "warning":
                ToastDelegate.warning(getContext(), info);
                break;
        }
    }


    /**
     * 选取本地图片
     */
    private void getImages(int chooseMode, int maxSelectNum, CompletionHandler handler) {

        mediaHandler = handler;
        pictureSelector(chooseMode, maxSelectNum);
    }


    /**
     * 图片选择方法
     *
     * @param chooseMode   选择模式
     * @param maxSelectNum 最大图片/视频数
     */

    private void pictureSelector(int chooseMode, int maxSelectNum) {
        // 清空selectList
        selectList = new ArrayList<>();
        PictureSelector.create(this)
                .openGallery(chooseMode)//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .theme(R.style.picture_Custom_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效

                //压缩功能设置
                .compress(false)
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .compressGrade(Luban.FIRST_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                // 是否裁剪 true or false
                .enableCrop(false)
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(0, 0)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false

                .isGif(false)// 是否显示gif图片 默认false
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                //.cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
                // .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                // .videoQuality()// 视频录制质量 0 or 1 int
                // .videoSecond()// 显示多少秒以内的视频or音频也可适用 int
                // .recordVideoSecond()//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    if (mediaHandler != null) {
                        MediaCallbackBean callbackBean = new MediaCallbackBean(true, "", selectList);
                        JSONObject jsonObject = new JSONObject(callbackBean.toJson());
                        mediaHandler.complete(jsonObject.toString());
                        mediaHandler = null;
                    }
                    break;

                case Small.REQUEST_CODE_DEFAULT:

                    String code = data.getStringExtra(JsWebViewActiEvent.SMALL_RESULT);
                    if (StringUtils.equals(code, JsWebViewActiEvent.FILE_SELECTED)) {
                        ArrayList<String> list = data.getStringArrayListExtra("SELECTED_DOCS");
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        FileCallbackBean callbackBean = new FileCallbackBean(true, "", list);
                        JSONObject jsonObject = new JSONObject(callbackBean.toJson());

                        if (fileHandler != null) {
                            fileHandler.complete(jsonObject.toString());
                            fileHandler = null;
                        }

                    } else if (StringUtils.equals(code, JsWebViewActiEvent.USER_CHOOSE)) {
                        String datas = data.getStringExtra("datas");

                        JSONObject dataobject = null;
                        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                        try {
                            dataobject = new JSONObject(datas);
                            JSONArray array = dataobject.getJSONArray("datas");

                            for (int index = 0; index < array.length(); index++) {

                                JSONObject object = array.getJSONObject(index);
                                HashMap<String, String> hashMap = new HashMap<String, String>();

                                hashMap.put("id", object.getString("id"));
                                hashMap.put("name", object.getString("name"));
                                list.add(hashMap);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            dataobject = new JSONObject();
                        }

                        UserChooseCallbackBean callbackBean = new UserChooseCallbackBean(true, "", list);
                        JSONObject jsonObject = new JSONObject(callbackBean.toJson());
                        if (userchooseHandler != null) {
                            userchooseHandler.complete(jsonObject.toString());
                            userchooseHandler = null;
                        }


                    }
                    break;
            }
        }
    }

    /**
     * alter确认后新建activity
     */
    protected void dialogShowAc(String dialogtitle,String info, final String htmlUrl, final String title, final String dialogType) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                .title(dialogtitle)
                .content(info)
                .positiveText("确定")
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialogType.equals("startWindow")) {
                            startWebViewActivity(htmlUrl, title);
                        } else {
                            skipWebViewActivity(htmlUrl, title);
                        }
                    }
                }).build();
        materialDialog.show();
    }

    /**
     * 设置progress的显示和取消
     */
    public void progressShow() {
        if (progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(this)
                    .content("系统正在处理，请稍后")
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .cancelable(true)
                    .build();
        }
        progressDialog.show();

    }

    public void progressDismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 文件打开安卓系统判断
     */
        public void openFile(String filepath) {
            final File file = new File(filepath);
            if (!file.exists()) {
                ToastDelegate.error(getContext(),"文件不存在！");
                return;
            }
            RxPermissions rxPermissions = new RxPermissions(BPMWebViewActivity.this);
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if(aBoolean){
                                if (Build.VERSION.SDK_INT >= 24) {
                                    // Android 7.0 需要用FileProvider的方式来将uri给外部应用使用
                                    PackageInfo packageInfo = new PackageInfo();
                                    Uri uri = FileProvider.getUriForFile(getContext(), Config.APK_PROVIDER_ID, file);
                                    Intent intent = new Intent("android.intent.action.VIEW");
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.setDataAndType(uri, MIMETypeUtil.getMIMEType(file));
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent("android.intent.action.VIEW");
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Uri uri = Uri.fromFile(file);
                                    intent.setDataAndType(uri, MIMETypeUtil.getMIMEType(file));
                                    startActivity(intent);
                                }
                            }else{
                                ToastDelegate.error(getContext(), "您没有授权存储权限，请到设置里设置权限！");
                            }
                        }
                    });
         }


    private void initLocation(){
        // 定位客户端的设置
        mLocationClient = LocationUtil.initLocationClient(this, new MyLocationListener());
        mLocationClient.start();
    }

    /**
     *  定位监听程序
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            LatLng nowLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder().accuracy(0).latitude(nowLatLng.latitude).longitude(nowLatLng.longitude).direction(location.getDirection()).build();
            if (locData.latitude == 0 || locData.longitude == 0) {
                ToastDelegate.info(getContext(), "定位失败");
                return;
            }
            if (isFirstLocation){
                isFirstLocation = false;
                LocationCallbackBean locationCallbackBean = new LocationCallbackBean(true,"",String.valueOf(locData.latitude),String.valueOf(locData.longitude));
                JSONObject jsonObject = new JSONObject(locationCallbackBean.toJson());

                if (locationHandler != null) {
                    locationHandler.complete(jsonObject.toString());
                    locationHandler = null;
                }
            }
        }
    }



}
