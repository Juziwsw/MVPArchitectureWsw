package com.hc.mvparchitecturewsw.base;

/**
 * Created by hc on 2017/12/8.
 * @author wsw
 */

public interface BaseView {
    /*******内嵌加载*******/
    /**
     * 开始加载dialog
     * @param content dialog显示的内容
     */
    void showLoading(String content);

    /**
     * 停止加载dialog
     */
    void stopLoading();

    /**
     * 请求失败
     * @param msg  请求异常信息
     * @param type  若有多个请求，用于区分不同请求（不同请求失败或有不同的处理）
     *              PS：无需区分则可传null
     */
    void showErrorMsg(String msg,String type);
}
