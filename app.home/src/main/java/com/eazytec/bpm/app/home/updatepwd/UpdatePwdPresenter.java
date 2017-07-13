package com.eazytec.bpm.app.home.updatepwd;

import com.eazytec.bpm.app.home.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author Administrator
 * @version Id: UpdatePwdPresenter, v 0.1 2017/7/13 15:57 Administrator Exp $$
 */
public class UpdatePwdPresenter extends RxPresenter<UpdatePwdContract.View> implements UpdatePwdContract.Presenter<UpdatePwdContract.View> {

    @Override
    public void updatePwd(String originPwd, String newPwd, String confirmPwd) {
        if (StringUtils.isSpace(originPwd)) {
            mView.toast(ToastDelegate.TOAST_TYPE_INFO,"原密码不得为空");
            return;
        }

        if (StringUtils.isSpace(newPwd)) {
            mView.toast(ToastDelegate.TOAST_TYPE_INFO,"新密码不得为空");
            return;
        }

        if (StringUtils.isSpace(originPwd)) {
            mView.toast(ToastDelegate.TOAST_TYPE_INFO,"新密码确认不得为空");
            return;
        }

        if (!StringUtils.equals(newPwd, confirmPwd)) {
            mView.toast(ToastDelegate.TOAST_TYPE_INFO,"新密码两次输入不一致");
            return;
        }

        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).updatePwd(originPwd, newPwd, confirmPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<WebDataTObject>() {
                    @Override public void onNext(WebDataTObject data) {
                        if (data.isSuccess()) {
                            mView.updatePwdSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"密码修改失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"密码修改失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}


