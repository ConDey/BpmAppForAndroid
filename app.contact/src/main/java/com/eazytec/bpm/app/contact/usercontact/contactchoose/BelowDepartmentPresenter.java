package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.app.contact.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Administrator
 * @version Id: BelowDepartmentPresenter, v 0.1 2017/7/24 13:09 Administrator Exp $$
 */
public class BelowDepartmentPresenter extends RxPresenter<BelowDepartmentContract.View> implements BelowDepartmentContract.Presenter<BelowDepartmentContract.View> {

    @Override public void loadDep(String id) {

        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).loadDepartmentsAndUsers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DepartmentDataTObject>() {
                    @Override public void onNext(DepartmentDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadDepSuccess(data);
                        }else{
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"部门信息加载失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"部门信息加载失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
