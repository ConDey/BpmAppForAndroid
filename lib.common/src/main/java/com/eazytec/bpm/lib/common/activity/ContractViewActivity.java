package com.eazytec.bpm.lib.common.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.CommonContract;
import com.eazytec.bpm.lib.common.RxPresenter;

/**
 * 通用的MVP设计架构下所衍生出来的Activity,主要作用是封装了一些公共的方法
 * <p>
 * 如果不使用MVP，则无需强制继承此类，如果使用MVP不继承此类的话，也没有毛病，但是需要自己实现一部分方法
 *
 * @author ConDey
 * @version Id: ContractViewActivity, v 0.1 2017/6/29 下午1:41 ConDey Exp $$
 */
public abstract class ContractViewActivity<T extends RxPresenter> extends CommonActivity implements CommonContract.CommonView {

    private T presenter;

    /**
     * MaterialDialog 这个类库目前看起来没有办法与View解耦，但是就目前来看这个类库非常稳定，
     * 所以这个类库允许一定的耦合
     */
    private MaterialDialog progressDialog;

    @Override @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化presenter
        presenter = createPresenter();
        presenter.attachView(this);
    }

    @Override @CallSuper
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    /**
     * 创建Presenter
     * <p>
     * 这个方法是系统调用的，内部请勿再调用
     *
     * @return
     */
    protected abstract T createPresenter();


    /**
     * 获得Presenter,这个方法提供给子Activity获取Presenter对象使用
     *
     * @return
     */
    protected T getPresenter() {
        return presenter;
    }

    ///////////////////////// BaseView默认的实现的相关方法 ///////////////////////////////

    /**
     * 通用的Toast显示方法
     * <p>
     * 根据Toast的类型来调用Delegate的不同方法
     *
     * @param type
     * @param string
     */
    public void toast(@ToastDelegate.TOAST_TYPE int type, String string) {
        switch (type) {
            case ToastDelegate.TOAST_TYPE_ERROR:
                ToastDelegate.error(getContext(), string);
                break;
            case ToastDelegate.TOAST_TYPE_SUCCESS:
                ToastDelegate.success(getContext(), string);
                break;
            case ToastDelegate.TOAST_TYPE_INFO:
                ToastDelegate.info(getContext(), string);
                break;
            case ToastDelegate.TOAST_TYPE_WARINGING:
                ToastDelegate.warning(getContext(), string);
                break;
            default:
                ToastDelegate.normal(getContext(), string);
                break;
        }
    }

    /**
     * 通用的Toast显示方法
     *
     * @param type
     * @param res
     */
    public void toast(@ToastDelegate.TOAST_TYPE int type, @StringRes int res) {
        toast(type, getContext().getString(res));
    }

    /**
     * 通用的进度显示器
     */
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(this)
                    .content("系统正在处理，请稍后")
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .cancelable(false)
                    .build();
        }
        progressDialog.show();

    }

    /**
     * 取消进度显示器显示
     */
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
