package com.twilio.notification.api;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by vmuller on 2/16/16.
 */

public interface BindingResource {
    @POST("/register")
    Call<Void> createBinding(@Body Binding binding);
}
