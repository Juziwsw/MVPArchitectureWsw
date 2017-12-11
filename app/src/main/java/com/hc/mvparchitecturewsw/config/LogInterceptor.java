package com.hc.mvparchitecturewsw.config;

import android.text.TextUtils;

import com.hc.mvparchitecturewsw.utils.Contants;
import com.hc.mvparchitecturewsw.utils.LogUtils;
import com.hc.mvparchitecturewsw.utils.ToastUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Description:
 * Company    :
 * Author     : Xiccc.
 * Email      : 1024003167@qq.com
 * Date       : 2017/07/17 17:59
 */

public class LogInterceptor implements Interceptor {
    String TAG = "LogInterceptor";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        String debugShowInfo = "";

        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        LogUtils.logd(TAG, "\n");
        LogUtils.logd(TAG, "----------------------Start----------------------");
        LogUtils.logd(TAG, "| " + request.toString());
        debugShowInfo += request.toString()+"\n";
        String method = request.method();
        if ("POST".equals(method)) {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                LogUtils.logd(TAG, "| RequestParams:{" + sb.toString() + "}");
                debugShowInfo += "RequestParams:{" + sb.toString() + "}\n";
            }
        }
        try {
            JSONObject jobj = new JSONObject(content);
            content = jobj.toString(2);
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtils.logd(TAG, "| Response:" + content);
        LogUtils.logd(TAG, "----------------------End:" + duration + "毫秒----------------------");
        //debugShowInfo += "Response:" + content +"\n";
        //debug模式下检查Server中是否包含脏数据
        String toastInfo = "";

        if(!TextUtils.isEmpty(toastInfo)){
            List<String> segs = request.url().pathSegments();
            if(segs != null && segs.size() != 0){
                for (int i = 0; i < segs.size(); i++) {
                    toastInfo += segs.get(i)+"/";
                }
            }
            try{
                if (Contants.DEBUG_ENABLE) {
                ToastUtil.showToast(toastInfo);
                //ToastUtil.showToast(debugShowInfo);
                LogUtils.loge(toastInfo);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
    }
}
