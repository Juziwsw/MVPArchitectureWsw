package com.hc.mvparchitecturewsw.app;

import android.app.Application;
import android.content.Context;

import com.hc.mvparchitecturewsw.R;
import com.hc.mvparchitecturewsw.utils.Contants;
import com.hc.mvparchitecturewsw.utils.LogUtils;
import com.hc.mvparchitecturewsw.utils.ToastUtil;

/**
 * Created by hc on 2017/12/8.
 * @author wsw
 */

public class App extends Application {
    public static App mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //log初始化,根据configUtil的isDebug参数控制是否显示log
        LogUtils.logInit(Contants.DEBUG_ENABLE);
        //toast初始化
        ToastUtil.register(getContext());
    }

    public static Context getContext() {
        return mApp.getApplicationContext();
    }

    public static String getAPPName() {
        return getContext().getResources().getString(R.string.app_name);
    }

}
