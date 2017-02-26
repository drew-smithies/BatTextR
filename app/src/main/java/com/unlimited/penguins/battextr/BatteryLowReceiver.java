package com.unlimited.penguins.battextr;

import android.Manifest;
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

                // Check for permissions
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                    // Get alert data helper
                    AlertItemDataHelper dh = new AlertItemDataHelper(context);
                    ArrayList<AlertItem> list = dh.getAllAlerts();
                    SmsManager smsManager = SmsManager.getDefault();

                    for (AlertItem item : list) {
                        if (!item.getDetail().equals("")) {
                            smsManager.sendTextMessage(item.getDetail(), null, "ID: " + item.getID() + context.getString(R.string.sms_body), null, null);
                        }
                    }

                    // Must call finish() so the BroadcastReceiver can be recycled.
                    pendingResult.finish();
                } else {
                    // TODO: do something noting sms failed because of permissions
                    pendingResult.finish();
                }
                return "";
            }
        };
        asyncTask.execute();
    }
}
