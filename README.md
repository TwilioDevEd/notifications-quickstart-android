# notifications-quickstart-android

Twilio Notifications starter Android application in Java

This application should give you a ready-made starting point for writing your
own notification-integrated apps with Twilio Notifications. Before we begin, you will need to setup and run the associated user notifications quick start web app. The Android application will make a call to the web application to register itself and create a binding. The web app comes with a script `notify.js` that will send a notification through Twilio to the app, using the identity that you provide when you run the application.

You can download the Node notifications web app from [here](https://github.com/TwilioDevEd/notifications-quickstart-node).

## Setting Up The Application

After downloading or cloning the app, you will need to provide a `google-services.json` file for the app - otherwise, you will get an error when you try and sync with Gradle. The default applicationId for the app (defined in `app/build.gradle`) is `com.testapp`. 

Use that application id when you register a new project with Google Services using this online tool [Google Cloud Messaging Registration](https://developers.google.com/cloud-messaging/android/client). That web site will generate a `google-services.json` file for your app. Download that file and put it into the app folder of the project you just downloaded. This file gives your app access credentials to GCM.

Now, open the app in Android studio by selecting the `build.gradle` file in the root directory. Android Studio will download the app's dependencies using gradle.

Next, in the `RegistrationIntentService.java` file, on this line,

        private static final String host = "YOUR-SERVER-HOST-NAME";

Replace YOUR-SERVER-HOST-NAME with the address of your server - for instance, from ngrok. You may also need to update the scheme and port on these  lines:

        private static final String schema = "http";
        private static final int port = 80;

Make sure your notification quick start web app is running, and then go ahead and launch the app. You can run this app on a device or on the Android emulator. You will need to provide an identity and an endpoint to use on the app's only screen. Tap the button and send the request over to your notification web service, which will update Twilio with the device token that identifies this app on your phone, tablet or emulator.

After that, run the notify.js script from the notifications quick start as a command line tool:

        node notify.js YOUR_IDENTITY

To receive a notification in your app.

That's it!

## License

MIT
