package com.hc.mvparchitecturewsw.ui.main.contract;

import com.hc.mvparchitecturewsw.base.BaseView;
import com.hc.mvparchitecturewsw.model.RegisterCodeBean;

/**
 * Created by hc on 2017/12/11.
 */

public interface MainContract {
    interface MainView extends BaseView{
        void loginSuccess(RegisterCodeBean bean);

    }
    interface MainPresenter{
        void login(String id,String w1,String w2);
    }
}
