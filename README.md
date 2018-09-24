# Twilio Notify Quickstart for Android

This application should give you a ready-made starting point for writing your
own notification integrated app with Twilio Notify. 

## Gather account information

We need to get the necessary information from our Twilio account. Here's what we'll need:

Config Value  | Description
:-------------  |:-------------
Service Instance SID | A [service](https://www.twilio.com/docs/api/notifications/rest/services) instance where all the data for our application is stored and scoped. You can create one in the [console](https://www.twilio.com/console/notify/services).

You will also need to create a push credential on the Twilio Console, and then configure it on your Notify service. You can [upload your push credentials here](https://www.twilio.com/console/notify/credentials/create). If you haven't set up the Firebase Cloud Messaging Service (FCM) for your app, you can do so by following [the Android push notification guide](https://www.twilio.com/docs/notify/configure-android-push-notifications).

## Set up Twilio Functions

The sample mobile app is already set up to communicate with Twilio Functions to register a device for notifications. You just need to create two Functions in your account from a template, and then specify the URL for one of those Twilio Functions in the source code to the app.

To get started with this, create a new Twilio Function on the [Twilio Console's Manage Functions page](https://www.twilio.com/console/runtime/functions/manage). Choose the Twilio Notify Quickstart template from the list of templates.
Use the Notify service SID you collected in the previous section for the only required configuration parameter for the template.

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

Next, in the `TwilioSDKStarterAPI.java` file, add your Twilio Functions url - just the domain, without the trailing slash (like this - https://sturdy-concrete-1234.twil.io):

        private static String BASE_SERVER_URL = "YOUR_TWILIO_FUNCTIONS_URL";

Make sure your notification quick start web app is running, and then go ahead and launch the app. You can run this app on a device or on the Android emulator. You will need to provide an identity and an endpoint to use on the app's only screen. 

Note that user identities for Notify should not be Personally Identifiable Information (PII), such as names. 

Once you tap `Register Binding`, the app will register your device with your Notify service and return a JSON response object to the app if successful. 

After that, send a notification to the identity you registered so that you will receive a push notification in your app. 

You can call the Twilio Function directly with an identity parameter and a body parameter, like this:

    https://sturdy-concrete-1234.twil.io/send-notification?identity=user1&body=Hello

That's it!

## License

MIT

