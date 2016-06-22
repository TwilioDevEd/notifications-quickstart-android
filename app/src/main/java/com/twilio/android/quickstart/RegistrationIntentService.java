/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twilio.android.quickstart;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.twilio.notification.api.BindingResource;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.twilio.android.quickstart.QuickstartPreferences.*;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private BindingResource bindingResource;
    private static final String schema = "http";
    private static final String host = "myserver.com";
    private static final int port = 80;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate(){
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(schema + "://" + host + ":" + port).addConverterFactory(JacksonConverterFactory.create())
                .build();

        bindingResource = retrofit.create(BindingResource.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            String identity = sharedPreferences.getString(IDENTITY, "Bob");
            String endpoint = sharedPreferences.getString(ENDPOINT, "Bob's Moto E");

            Log.i(TAG,"Identity = " + identity);
            Log.i(TAG,"Endpoint = " + endpoint);

            sendRegistrationToServer(identity, endpoint, token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on Twilio server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to Twilio via your application server.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String identity, String endpoint, String token) throws IOException {
        Call<Void> call = bindingResource.createBinding(identity, endpoint, token, "gcm");
        Response<Void> response = call.execute();
    }

}
