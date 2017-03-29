package com.twilio.notify.quickstart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static com.twilio.notify.quickstart.BindingSharedPreferences.*;

public class MainActivity extends AppCompatActivity {

    public static final String BINDING_REGISTRATION = "BINDING_REGISTRATION";
    public static final String BINDING_SUCCEEDED = "BINDING_SUCCEEDED";
    public static final String BINDING_RESPONSE = "BINDING_RESPONSE";

    private WakefulBroadcastReceiver bindingBroadcastReceiver;
    private TextView resultTextView;
    private EditText identityTextView;
    private Button registerBindingButton;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        identityTextView = (EditText) findViewById(R.id.identity_edit_text);
        identityTextView.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(IDENTITY, null));

        resultTextView = (TextView) findViewById(R.id.result_text_view);
        resultTextView.setVisibility(View.INVISIBLE);

        registerBindingButton = (Button) findViewById(R.id.register_binding_button);
        registerBindingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultTextView.setVisibility(View.INVISIBLE);
                registerBindingButton.setEnabled(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.progress_dialog_message));
                progressDialog.show();

                registerBinding();
            }
        });

        bindingBroadcastReceiver = new WakefulBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean succeeded = intent.getBooleanExtra(BINDING_SUCCEEDED, false);
                String message = intent.getStringExtra(BINDING_RESPONSE);
                if (message == null) {
                    message = "";
                }

                if (succeeded) {
                    resultTextView.setText("Binding registered. " + message);
                } else {
                    resultTextView.setText("Binding failed: " + message);
                }

                resultTextView.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                registerBindingButton.setEnabled(true);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(bindingBroadcastReceiver,
                new IntentFilter(BINDING_REGISTRATION));
    }

    /**
     * Start the IntentService to register this app identity with Twilio Notify
     */
    public void registerBinding() {
        String identity = identityTextView.getText().toString();
        Intent intent = new Intent(this, BindingIntentService.class);
        intent.putExtra(IDENTITY, identity);
        startService(intent);
    }

}
