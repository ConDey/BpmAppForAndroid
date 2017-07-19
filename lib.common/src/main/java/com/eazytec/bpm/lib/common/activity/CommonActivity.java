package com.eazytec.bpm.lib.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.eazytec.bpm.lib.utils.ActivityUtils;
import com.eazytec.bpm.lib.utils.KeyboardUtils;

import org.json.JSONObject;

/**
 * 封装的基础Activity，主要负责约束Activity的通用行为，封装了一些默认的规则和方法，比如插件之间以及插件内部的跳转规则
 * 基础Activity跟之间不一样的是，不再约束Activity创建和执行规则链，使插件使用起来更开放。
 *
 * @author ConDey
 * @version Id: CommonActivity, v 0.1 2017/6/29 上午10:31 ConDey Exp $$
 */
public abstract class CommonActivity extends AppCompatActivity {

    public Context getContext() {
        return this;
    }

    /**
     * 重新定义返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        KeyboardUtils.hideSoftInput(getWindow().getDecorView()); // 隐藏软键盘
        super.finish();
    }

    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////// Activity跳转相关方法 ///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public void skipActivity(Activity aty, Class<?> cls) {
        startActivity(aty, cls);
        aty.finish();
    }

    public void skipActivity(Activity aty, Intent it) {
        startActivity(aty, it);
        aty.finish();
    }

    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        startActivity(aty, cls, extras);
        aty.finish();
    }

    public void startActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    public void startActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }

    public void startActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    public void fileHandler(boolean isSuccess, JSONObject jsonObject){

    }
}
