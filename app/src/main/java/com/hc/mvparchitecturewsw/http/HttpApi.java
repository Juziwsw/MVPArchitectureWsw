package com.hc.mvparchitecturewsw.http;

import com.hc.mvparchitecturewsw.bean.BaseResponse;
import com.hc.mvparchitecturewsw.model.RegisterCodeBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hc on 2017/12/8.
 */

public interface HttpApi {

    @FormUrlEncoded
    @POST("credit-user/reg-get-code")
    Observable<BaseResponse<RegisterCodeBean>> getRegisterCode(@Field("phone") String phone,
                                                               @Field("type") String type,
                                                               @Field("captcha") String captcha);
}
