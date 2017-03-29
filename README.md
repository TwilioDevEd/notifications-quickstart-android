# Twilio Notify Quickstart for Android

This application should give you a ready-made starting point for writing your
own notification integrated app with Twilio Notify. Before we begin, you will need to set up a
web server application that communicates with your mobile app.

## Download a Twilio SDK Starter Server project

You can download one of the web server application in these languages:

| Language  | GitHub Repo |
| :-------------  |:------------- |
PHP | [sdk-starter-php](https://github.com/TwilioDevEd/sdk-starter-php/)
Ruby | [sdk-starter-ruby](https://github.com/TwilioDevEd/sdk-starter-ruby/)
Python | [sdk-starter-python](https://github.com/TwilioDevEd/sdk-starter-python/)
Node.js | [sdk-starter-node](https://github.com/TwilioDevEd/sdk-starter-node/)
Java | [sdk-starter-java](https://github.com/TwilioDevEd/sdk-starter-java/)

You'll only need to download one of those. Not sure which one to choose?
The [Node.js](https://github.com/TwilioDevEd/sdk-starter-node/) server starter kit
is pretty easy to set up and follow along with.

Follow the directions in the README on one of the above servers, and get the web server up
and running to make sure you have everything configured right for the demos you are interested in.

## Setting Up The Application

The application uses [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/) (FCM)
from Google, the successor to Google Cloud Messaging (GCM)

After downloading or cloning the app, you will need to provide a `google-services.json` file for the app,
otherwise you will get an error when you try and sync with Gradle.

The default applicationId for the app defined in `app/build.gradle` is `com.twilio.notify.quickstart`.

Use this application id when you register a new project with Firebase using this online tool
[Firebase Console](https://console.firebase.google.com/). That web site will generate a
`google-services.json` file for your app. Download that file and put it into the `app` folder of the
project you just downloaded. This file gives your app access credentials to FCM.

Now, open the app in Android studio by selecting the `build.gradle` file in the root directory.

Next, in the `TwilioSDKStarterAPI.java` file, add your server url:

        private static String BASE_SERVER_URL = "YOUR_SDK_STARTER_SERVER_URL";

Make sure your notification quick start web app is running, and then go ahead and launch the app. You can run this app on a device or on the Android emulator. You will need to provide an identity and an endpoint to use on the app's only screen. Tap the button and send the request over to your notification web service, which will update Twilio with the device token that identifies this app on your phone, tablet or emulator.

The app uses 4 credentials to register your device for notifications.

Credential | Description
---------- | -----------
Identity | This is how the web app identifies an individual user as the receiver of notifications.
Endpoint | This is a unique device ID and identity combination that can receive a message. (i.e Alice on her iPad is a different notification destination than Alice on her iPhone).
Address | This is the unique device identifier of the mobile client.
Bindingtype | This lets the web app know which service to register with (APNS or FCM).

Once you've entered your URL, you can compile and run the app. Enter an identity in the text field
that's presented. Once you tap register, the app will register your device with Twilio Notify and
return a JSON response object if successful. After that, visit the Notify page on your server web application,
and send a notification to the identity you registered as to receive a push notification in your app.

That's it!

## License

MIT

