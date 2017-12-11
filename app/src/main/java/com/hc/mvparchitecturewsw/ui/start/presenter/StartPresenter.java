package com.hc.mvparchitecturewsw.ui.start.presenter;

import com.hc.mvparchitecturewsw.base.BasePresenter;
import com.hc.mvparchitecturewsw.http.HttpManager;
import com.hc.mvparchitecturewsw.http.HttpSubscriber;
import com.hc.mvparchitecturewsw.model.RegisterCodeBean;
import com.hc.mvparchitecturewsw.ui.start.contract.StartContract;
/**
 * Created by hc on 2017/12/8.
 * @author wsw
 */

public class StartPresenter extends BasePresenter<StartContract.View> implements StartContract.presenter {

    @Override
    public void login(String id,String w1,String w2) {
        toSubscribe(HttpManager.getApi().getRegisterCode(id,w1,w2), new HttpSubscriber<RegisterCodeBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("请求中...");
            }
            @Override
            protected void _onNext(RegisterCodeBean book) {
                mView.loginSuccess(book);

            }
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });

    }
}
