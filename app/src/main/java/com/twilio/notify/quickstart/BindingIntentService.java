package com.twilio.notify.quickstart;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.twilio.notify.quickstart.notifyapi.TwilioSDKStarterAPI;
import com.twilio.notify.quickstart.notifyapi.model.Binding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.twilio.notify.quickstart.BindingSharedPreferences.ENDPOINT;
import static com.twilio.notify.quickstart.BindingSharedPreferences.IDENTITY;
import static com.twilio.notify.quickstart.BindingSharedPreferences.ADDRESS;

public class BindingIntentService extends IntentService {

    private static final String TAG = "BindingIntentService";
    private static final String FCM_BINDING_TYPE = "fcm";

    private SharedPreferences sharedPreferences;
    private Intent bindingResultIntent;

    public BindingIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        bindingResultIntent = new Intent(MainActivity.BINDING_REGISTRATION);
        // Load the old binding values from shared preferences if they exist
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String identity = sharedPreferences.getString(IDENTITY, null);
        String endpoint = sharedPreferences.getString(ENDPOINT, null);
        String address = sharedPreferences.getString(ADDRESS, null);

        // Set the new binding identity
        String newIdentity = intent.getStringExtra(IDENTITY);
        if (newIdentity == null) {
            // If no identity was provided to us then we use the identity stored in shared preferences.
            newIdentity = identity;
        }

        /*
         * Generate a new endpoint based on the new identity and the instanceID. This ensures that we
         * maintain stability of the endpoint even if the instanceID changes without the identity
         * changing.
         */
        String newEndpoint = newIdentity + "@" + FirebaseInstanceId.getInstance().getId();

        /*
         * Obtain the new address based off the Firebase instance token
         */
        String newAddress = FirebaseInstanceId.getInstance().getToken();

        /*
         * Check whether a new binding registration is required by comparing the prior values that
         * were stored in shared preferences after the last successful binding registration.
         */
        boolean sameBinding =
                newIdentity.equals(identity) && newEndpoint.equals(endpoint) && newAddress.equals(address);

        if (newIdentity == null) {
            Log.i(TAG, "A new binding registration was not performed because" +
                    " the identity cannot be null.");
            bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, false);
            bindingResultIntent.putExtra(MainActivity.BINDING_RESPONSE, "Binding identity was null");
            // Notify the MainActivity that the registration ended
            LocalBroadcastManager.getInstance(BindingIntentService.this)
                    .sendBroadcast(bindingResultIntent);
        } else if (sameBinding) {
            Log.i(TAG, "A new binding registration was not performed because" +
                    "the binding values are the same as the last registered binding.");
            bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, true);
            bindingResultIntent.putExtra(MainActivity.BINDING_RESPONSE, "Binding already registered");
            // Notify the MainActivity that the registration ended
            LocalBroadcastManager.getInstance(BindingIntentService.this)
                    .sendBroadcast(bindingResultIntent);
        } else {
            /*
             * Clear all the existing bindings from SharedPreferences and attempt to register
             * the new binding values.
             */
            sharedPreferences.edit().clear().commit();
            final Binding binding = new Binding(newIdentity, newEndpoint, newAddress, FCM_BINDING_TYPE);
            registerBinding(binding);
        }
    }

    /**
     * Register the binding with Twilio Notify via your application server.
     */
    private void registerBinding(final Binding binding) {
        /*
         * Issue the request to your application server and wait for the result in the callback
         */
        Log.i(TAG, "Binding with" +
                " identity: " + binding.identity +
                " endpoint: " + binding.endpoint +
                " address: " + binding.Address);
        Call<Void> call = TwilioSDKStarterAPI.registerBinding(binding);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccess()) {
                    // Store binding in SharedPreferences upon success
                    sharedPreferences.edit().putString(IDENTITY, binding.identity).commit();
                    sharedPreferences.edit().putString(ENDPOINT, binding.endpoint).commit();
                    sharedPreferences.edit().putString(ADDRESS, binding.Address).commit();

                    bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, true);
                } else {
                    String message = "Binding failed " + response.code() + " " + response.message();
                    Log.e(TAG, message);
                    bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, false);
                    bindingResultIntent.putExtra(MainActivity.BINDING_RESPONSE, message);
                }

                // Notify the MainActivity that the registration ended
                LocalBroadcastManager.getInstance(BindingIntentService.this)
                        .sendBroadcast(bindingResultIntent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String message = "Binding failed " + t.getMessage();
                Log.e(TAG, message);
                bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, false);
                bindingResultIntent.putExtra(MainActivity.BINDING_RESPONSE, message);
                // Notify the MainActivity that the registration ended
                LocalBroadcastManager.getInstance(BindingIntentService.this)
                        .sendBroadcast(bindingResultIntent);
            }
        });
    }

}
