package com.twilio.notify.quickstart.notifyapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.notify.quickstart.notifyapi.model.Binding;
import com.twilio.notify.quickstart.notifyapi.model.CreateBindingResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class TwilioSDKStarterAPI {
    public final static String BASE_SERVER_URL = "https://f107bbc9.ngrok.io";

    /**
     * A resource defined to register Notify bindings using the sdk-starter projects available in
     * C#, Java, Node, PHP, Python, or Ruby.
     *
     * https://github.com/TwilioDevEd?q=sdk-starter
     */
    interface SDKStarterService {
        @POST("/register")
        Call<CreateBindingResponse> register(@Body Binding binding);
    }

    private static SDKStarterService sdkStarterService = new Retrofit.Builder()
            .baseUrl(BASE_SERVER_URL)
            .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)))
            .build()
            .create(SDKStarterService.class);

    public static Call<CreateBindingResponse> registerBinding(final Binding binding) {
        return sdkStarterService.register(binding);
    }

}
