package com.twilio.notification.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by vmuller on 2/16/16.
 */

public interface BindingResource {
    @FormUrlEncoded
    @POST("/register")
    Call<Void> createBinding(@Field("Identity") String identity, @Field("Endpoint") String endpoint, @Field("Address") String address, @Field("BindingType") String bindingType);
}
