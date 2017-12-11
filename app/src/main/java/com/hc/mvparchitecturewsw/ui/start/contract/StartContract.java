package com.hc.mvparchitecturewsw.ui.start.contract;

import com.hc.mvparchitecturewsw.base.BaseView;
import com.hc.mvparchitecturewsw.model.RegisterCodeBean;

/**
 * Created by hc on 2017/12/8.
 */

public interface StartContract {
    public interface View extends BaseView {
        void loginSuccess(RegisterCodeBean bean);
    }
    public interface presenter{
        void login(String id,String w1,String w2);
    }

}
