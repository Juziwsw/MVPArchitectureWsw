package com.hc.mvparchitecturewsw.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hc.mvparchitecturewsw.utils.TUtil;
import com.hc.mvparchitecturewsw.utils.TitleUtil;

import butterknife.ButterKnife;

/**
 * fragment基类
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    protected View mView;
    public T mPresenter;
    public Context mContext;
    public BaseActivity mActivity;
    protected TitleUtil mTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(),null);
        ButterKnife.bind(this, mView);
        mContext = getContext();
        mActivity= (BaseActivity) getActivity();
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) {
            //mPresenter.mContext = mContext;
        }
        mTitle = new TitleUtil(mActivity,mView);
        initPresenter();
        loadData();
        return mView;
    }

    /**
     * 获取布局文件
     * @return
     */
    public abstract int getLayoutId();
    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    public abstract void initPresenter();

    /**
     * 加载、设置数据
     */
    public abstract void loadData();


    /**
     * 请求权限封装
     * @param permissions
     * @param listener
     */
    public void requestPermissions(String[] permissions, PermissionsListener listener) {
       mActivity.requestPermissions(permissions,listener);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    protected void startActivityToLogin(Intent intent) {
        /*if (!App.getConfig().getLoginStatus()) {
            App.toLogin(getActivity());
        } else {
            startActivity(intent);
        }*/
    }
}
