package com.hc.mvparchitecturewsw.ui.main.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.hc.mvparchitecturewsw.R;
import com.hc.mvparchitecturewsw.base.BaseActivity;
import com.hc.mvparchitecturewsw.model.RegisterCodeBean;
import com.hc.mvparchitecturewsw.ui.main.contract.MainContract;
import com.hc.mvparchitecturewsw.ui.main.fragment.FristFragment;
import com.hc.mvparchitecturewsw.ui.main.fragment.SecondFragment;
import com.hc.mvparchitecturewsw.ui.main.presenter.MainPresenter;

import butterknife.OnClick;

/**
 * Created by hc on 2017/12/11.
 */

public class MainAcvitity extends BaseActivity<MainPresenter> implements MainContract.MainView {

    /** 第一页 */
    public static final int TAB_FRIST = 0;
    /** 第二页 */
    public static final int TAB_SECOND = 1;
    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void loginSuccess(RegisterCodeBean bean) {

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    public void initPresenter() {
    }
    @Override
    public void loadData() {
        initView();
    }

    private void initView() {
        replaceView(TAB_FRIST);
    }

    @OnClick({R.id.button_frist, R.id.button_second})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_frist:
                replaceView(TAB_FRIST);
                break;
            case R.id.button_second:
                replaceView(TAB_SECOND);
                break;
        }
    }
    /**
     * 切换视图
     *
     * @param currItem
     */
    private synchronized void replaceView(int currItem) {
        switch (currItem) {
            case TAB_FRIST:
                showFragment(FristFragment.class.getName());
                break;
            case TAB_SECOND:
                showFragment(SecondFragment.class.getName());
                break;
        }
    }
    private Fragment mFragment = null;
    //切换视图
    private void showFragment(String frgName) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(frgName);
        if (mFragment != null && fragment == mFragment) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = Fragment.instantiate(this, frgName);
            ft.add(R.id.frameLayout, fragment, frgName);
            ft.addToBackStack(null);
        }
        if (mFragment != null) {
            ft.hide(mFragment);
        }
        mFragment = fragment;
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }
}
