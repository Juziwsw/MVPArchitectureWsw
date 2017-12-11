package com.hc.mvparchitecturewsw.base;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.hc.mvparchitecturewsw.app.App;
import com.hc.mvparchitecturewsw.app.AppManager;
import com.hc.mvparchitecturewsw.dialog.AlertFragmentDialog;
import com.hc.mvparchitecturewsw.utils.LogUtils;
import com.hc.mvparchitecturewsw.utils.TUtil;
import com.hc.mvparchitecturewsw.utils.TitleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    public T mPresenter;
    public Context mContext;
    public BaseActivity mActivity;
    private PermissionsListener mListener;
    protected TitleUtil mTitle;
    protected int type;
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS};
    private boolean isRequesting;//为了避免在onResume中多次请求权限

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        AppManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
        mPresenter = TUtil.getT(this, 0);
        mTitle = new TitleUtil(this, getWindow().getDecorView());
        //initStatusBar();
        initPresenter();
        loadData();


    }

    private void initStatusBar() {
        //层垫式状态栏
        //StatusBarUtil.setStatusBarColor(this, R.color.colorPrimaryDark);

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
     *
     * @param permissions
     * @param listener
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, PermissionsListener listener) {
        mListener = listener;
        List<String> requestPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permission);
            }
        }
        if (requestPermissions.isEmpty()) {
            //已经全部授权
            mListener.onGranted();
        } else {
            LogUtils.logd("没有授权");
            //申请授权
            requestPermissions(requestPermissions.toArray(new String[requestPermissions.size()]), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                List<String> deniedPermissions = new ArrayList<>();
                //当所有拒绝的权限都勾选不再询问，这个值为true,这个时候可以引导用户手动去授权。
                boolean isNeverAsk = true;
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    String permission = permissions[i];
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        deniedPermissions.add(permissions[i]);
                        // 点击拒绝但没有勾选不再询问
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                            isNeverAsk = false;
                        }
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    try {
                        mListener.onGranted();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        mListener.onDenied(Arrays.asList(permissions), true);
                    }
                } else {
                    mListener.onDenied(deniedPermissions, isNeverAsk);
                }
                break;
            default:
                break;
        }
    }

    /**
     *  启动应用的设置弹窗
     * @param message
     * @param isFinish
     */
    public void toAppSettings(String message, final boolean isFinish) {
        if (TextUtils.isEmpty(message)) {
            message = "\"" + App.getAPPName() + "\"缺少必要权限";
        }
        AlertFragmentDialog.Builder builder = new AlertFragmentDialog.Builder(this);
        if (isFinish) {
            builder.setLeftBtnText("退出")
                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                        @Override
                        public void dialogLeftBtnClick() {
                            finish();
                        }
                    });
        } else {
            builder.setLeftBtnText("取消");
        }
        builder.setContent(message + "\n请手动授予\"" + App.getAPPName() + "\"访问您的权限")
                .setRightBtnText("去设置")
                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                }).build();
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
        intent.setClass(this, cls);
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
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    IWindowFocus iFocus;

    public void setOnIWindowFocus(IWindowFocus windowFocus) {
        iFocus = windowFocus;
    }

    public interface IWindowFocus {
        void focused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        //MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        ///MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        AppManager.getInstance().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
    }
}
