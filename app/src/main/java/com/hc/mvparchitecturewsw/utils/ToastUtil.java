package com.hc.mvparchitecturewsw.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.hc.mvparchitecturewsw.R;


/**
 * 弹出窗口显示
 * Toast工具类
 * 为了统一UI，绕过手机权限禁用，采用自定义Toast
 * MiuiV8因为禁用了自定义Toast，因此检测到MiuiV8，依然采用系统Toast
 *
 * 目前支持的特性：
 * 自定义时长duration
 * 延时显示delay
 * 显示位置gravity + marginDp（居中，或距离上边/距离底边多大尺寸）
 * 在任意线程里调用
 *
 * 目前尚不支持的特性：
 * ToastUI样式扩展，如增加图标等
 * MiuiV8采用系统Toast，尚不支持特殊时长（只有系统提供的两种）
 *
 * Created by wangkw on 2016/8/24.
 */

public final class ToastUtil {

    /**
     * 单例
     */
    static private ToastUtil instance;
    static byte[] mLocker = new byte[0];
    static public ToastUtil getInstance() {
        synchronized (mLocker) {

            if (instance == null) {
                instance = new ToastUtil();
            }
        }
        return instance;
    }

    /**
     * 程序入口注册
     * @param context
     */
    public static void register(Context context){
        mCtx=context.getApplicationContext();
        toastLayout = R.layout.fs_toast;
        sHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 初始化上下文和布局
     */
    static int toastLayout;
    static Context mCtx;
    static Handler sHandler;

    public final static void fileErr() {
        show("附件大于50M,请选择其它附件", false);
    }

    public final static void fileMinErr() {
        show("附件小于0,请选择其它附件", false);
    }

    public final static void fileErrEx() {
        show("您已超过最大选择数"/*FileMainActivity.outOfRangToast*/, false);
    }

    public final static void fileNoErr() {
        show("文件不存在", false);
    }

    /**
     * 仅限应用联网受限，网络不可用的时候调用提示
     * @param bl true:屏幕中间弹toast
     *           false:屏幕下边弹toast
     */
    public static void show(String message, boolean bl) {
        if (bl) {
            show(message, Toast.LENGTH_SHORT, Gravity.CENTER, 0);
        } else {
            show(message, Toast.LENGTH_SHORT);
        }
    }

    public void setContext(Context context, int layout) {
        if (!isMainThread()){
            throw new RuntimeException("ToastUtil::setContext must be in UI thread");
        }
        mCtx = context;
        toastLayout = layout;
        sHandler = new Handler(Looper.getMainLooper());
    }

    static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    static boolean useSysToast = false;
    /**
     * MiuiV8 采用系统Toast
     */
    static {
        useSysToast = RomUtils.checkIsMiuiRom() && RomUtils.getMiuiVersion() >= 8;
    }

    private static final int LENGTH_LIMIT = 8;      //超过8个字则显示更长时间
    private static final int DURATION_UNSET = -1;   //未设定时长，按文本长度控制时长

    /**
     * 最简单的showToast
     */
    public static void show(CharSequence text){
        show(text, DURATION_UNSET);
    }

    public static void showToast(CharSequence text){
        show(text, DURATION_UNSET);
    }

    /**
     * 增加显示时长
     */
    public static void show(CharSequence text, int duration){
        show(text, duration, Gravity.NO_GRAVITY, 0);
    }

    /**
     * 增加位置属性：marginDp代表距离的dp值
     * 如果Gravity是Center，marginDp代表往下移
     * 如果Gravity是Bottom，marginDp代表距离底边
     * 如果Gravity是Top，marginDp代表距离上边
     */
    public static void show(final CharSequence text, int duration, int gravity, int marginDp){
        show(text, duration, gravity, marginDp, 0);
    }

    /**
     * 增加延时
     */
    public static void show(final CharSequence text, final int duration,
                            final int gravity, final int marginDp, int delay) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (useSysToast){
                    showSysToast(mCtx, text, duration, gravity, marginDp);
                } else {
                    showCustomToast(mCtx, text, duration, gravity, marginDp);
                }
            }
        }, delay);
    }

    /**
     * 主线程运行，并附带延时
     */
    private static void runOnUiThread(Runnable runnable, int delay){
        if (delay <= 0 && isMainThread()){
            runnable.run();
        } else {
            sHandler.postDelayed(runnable, delay);
        }
    }


    /**
     * 调用系统Toast
     */
    private static void showSysToast(final Context context, final CharSequence text, int duration,
                                     final int gravity, final int marginDp) {
        if (duration > FsToast.Builder.DURATION_DEFAULT){   //大于默认值是长消息
            duration = Toast.LENGTH_LONG;
        } else if (duration < 0){                           //值小于零，文本超长是长消息，短的是短消息
            if (!TextUtils.isEmpty(text) && text.length() > LENGTH_LIMIT){
                duration = Toast.LENGTH_LONG;
            } else {
                duration = Toast.LENGTH_SHORT;
            }
        } else if (duration == Toast.LENGTH_LONG){          //原本是长的，设定为长的
            duration = Toast.LENGTH_LONG;
        } else {                                            //其余情况都认为是短的
            duration = Toast.LENGTH_SHORT;
        }

        Toast toast = Toast.makeText(context, text, duration);
        if (gravity != Gravity.NO_GRAVITY){
            int yOffset = FSScreen.dip2px(context, marginDp);
            toast.setGravity(gravity, 0, yOffset);
        }
        toast.show();
    }

    /**
     * 显示自定义Toast
     */
    private static void showCustomToast(Context context, final CharSequence text, int duration,
                                        int gravity, int marginDp) {
        if (duration == Toast.LENGTH_SHORT) {                   //短消息设置默认时长
            duration = FsToast.Builder.DURATION_DEFAULT;
        } else if (duration == Toast.LENGTH_LONG) {             //长消息设置长的时长
            duration = FsToast.Builder.DURATION_LONG;
        } else if (duration < 0){                               //duration 未设定，则根据文本长度选择
            if (!TextUtils.isEmpty(text) && text.length() > LENGTH_LIMIT){
                duration = FsToast.Builder.DURATION_LONG;
            } else {
                duration = FsToast.Builder.DURATION_DEFAULT;
            }
        }                                                       //其余值得按设定的值
        FsToast.Builder builder = new FsToast.Builder(toastLayout)
                .setDuration(duration);

        if (gravity != Gravity.NO_GRAVITY){
            builder = builder.setGravity(gravity)
                    .setMarginDp(marginDp);
        }

        builder.build(context, text)
                .show();
    }

}
