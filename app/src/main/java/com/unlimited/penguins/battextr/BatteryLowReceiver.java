package com.unlimited.penguins.battextr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.telephony.SmsManager;

import java.util.ArrayList;



/**
 * Created by Drew on 2/25/2017.
 */

public class BatteryLowReceiver extends BroadcastReceiver {
    private static final String TAG = "Drew_BatReceiver";


    @Override
    public void onReceive(final Context context, final Intent intent) {

        final PendingResult pendingResult = goAsync();
        AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String log = "";

                // Check for permissions
                android.Manifest.permission perm = new android.Manifest.permission();
                int permissionCheck = ContextCompat.checkSelfPermission(context, perm.SEND_SMS);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                    // Get alert data helper
                    AlertItemDataHelper dh = new AlertItemDataHelper(context);
                    ArrayList<AlertItem> list = dh.getAllAlerts();
                    SmsManager smsManager = SmsManager.getDefault();


                    StringBuilder sb = new StringBuilder();
                    sb.append("Alert Count: " + list.size() + "\n");
                    for (AlertItem item : list) {
                        sb.append(item.getID() + ". " + item.getName() + "\n");
                        smsManager.sendTextMessage("5556", null, "This is a test. Id: " + item.getID(), null, null);
                    }

                    // Debug
                    sb.append("Action: " + intent.getAction() + "\n");
                    sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
                    log = sb.toString();
                    Log.d(TAG, log);

                    // Must call finish() so the BroadcastReceiver can be recycled.
                    pendingResult.finish();
                } else {
                    // TODO: do something noting sms failed because of permissions
                }
                return log;
            }
        };
        asyncTask.execute();
    }
}
