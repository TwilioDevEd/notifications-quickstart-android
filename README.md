# notifications-quickstart-android

Twilio Notifications starter Android application in Java

This application should give you a ready-made starting point for writing your
own notification-integrated apps with Twilio Notifications. Before we begin, you need to run the associated notifications web app. 

You can download the Node notifications web app from [here](https://github.com/TwilioDevEd/notifications-quickstart-node).

## Setting Up The Application

After downloading or cloning the app, open the app in Android studio and the 
project will immediately build the dependencies using gradle. Right click on the 'notification-client-0.0.1-SNAPSHOT.jar' dependency in the root directory and click 'Add as a Library'. Use to Google Services [tool](https://developers.google.com/cloud-messaging/android/client) to generate a 'google-services.json' file for your app. This file gives your app access credentials to GCM.

Next, in the MainActivity file, on this line,

        editor.putString(MwcDemoPreferences.HOST,"YOUR_WEB_APP_HERE");

Replace the URL with the address of your server. Then on these two lines in the onHandleIntent method of RegistrationIntentService.java

        String identity = sharedPreferences.getString(IDENTITY, "IDENTITY_HERE");
        String endpoint = sharedPreferences.getString(ENDPOINT, "ENDPOINT_HERE");
        

replace "IDENTITY_HERE" and "ENDPOINT_HERE" with your desired values.

Once you've done that you can compile & run the app. Once you login with your Twilio credentials, it'll register your device with GCM and return a JSON response object if successful. After that, run the notify.js script in the web app repo

        node notify.js YOUR_IDENTITY

To receive a notification in your app.

That's it!

## License

MIT
