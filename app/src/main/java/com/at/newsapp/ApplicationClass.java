package com.at.newsapp;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.at.newsapp.Activity.DetailsActivity;
import com.google.gson.JsonObject;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class ApplicationClass extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
                    @Override
                    public void notificationReceived(OSNotification notification) {
                        JSONObject data = notification.payload.additionalData;
                        String customKey;

                        if (data != null) {
                            customKey = data.optString("customkey", null);
                            if (customKey != null)
                                Log.d("OneSignalExample", "customkey set with value: " + customKey);
                        }
                    }
                })
                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenResult result) {
                        OSNotificationAction.ActionType actionType = result.action.type;
                        JSONObject data = result.notification.payload.additionalData;
                        String customKey;
                        Log.d("OSNotificationPayload", "result.notification.payload.toJSONObject().toString(): " + result.notification.payload.toJSONObject().toString());
                        if (data != null) {
                            customKey = data.optString("customkey", null);
                            if (customKey != null)
                                Log.d("OneSignalExample", "customkey set with value: " + customKey);
                        }
                        if (actionType == OSNotificationAction.ActionType.ActionTaken)
                            Log.d("OneSignalExample", "Button pressed with id: " + result.action.actionID);

                        // The following can be used to open an Activity of your choice.
                        // Replace - getApplicationContext() - with any Android Context.

                         try {
                             JSONObject dataNoti = result.notification.payload.toJSONObject();
                             Log.d("osnoti", dataNoti.toString());
                             Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                             String url = dataNoti.getString("launchURL");
                             intent.putExtra("url", url);
                             intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                             startActivity(intent);
                         }catch (Exception ex){
                             ex.printStackTrace();
                             Log.d("osnoti", "Fucked");
                             Toast.makeText(getApplicationContext(), "Failed to load URL", Toast.LENGTH_LONG);
                         }
                        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
                        //   if you are calling startActivity above.
     /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
                    }
                })
                .init();
    }
}
