package com.hc.mvparchitecturewsw.ui.start.activity;

import android.content.Intent;
import android.view.View;

import com.hc.mvparchitecturewsw.R;
import com.hc.mvparchitecturewsw.base.BaseActivity;
import com.hc.mvparchitecturewsw.model.RegisterCodeBean;
import com.hc.mvparchitecturewsw.ui.main.activity.MainAcvitity;
import com.hc.mvparchitecturewsw.ui.start.contract.StartContract;
import com.hc.mvparchitecturewsw.ui.start.presenter.StartPresenter;
import com.hc.mvparchitecturewsw.utils.LogUtils;
import com.hc.mvparchitecturewsw.utils.ToastUtil;

/**
 * Created by hc on 2017/12/8.
 * @author wsw
 */


public class StartActivity extends BaseActivity<StartPresenter> implements StartContract.View {

    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }
    @Override
    public void initPresenter() {
        mPresenter.init(this);

    }
    @Override
    public void loadData() {
        initView();
    }
    private void initView(){
        mTitle.setTitle("一个界面");
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button:
                mPresenter.login("18639737039","","");
                //Toast.makeText(this, "点击事件", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button1:
                startActivity(new Intent(this, MainAcvitity.class));
                break;
        }
    }

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
        LogUtils.loge(bean.toString(),"");
        ToastUtil.show(bean.toString());
    }
}
