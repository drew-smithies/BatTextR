package com.unlimited.penguins.battextr;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Drew on 2/25/2017.
 */

public class BatteryLowReceiver extends BroadcastReceiver {
    private static final String TAG = "Drew_BatReceiver";


    @Override
    public void onReceive(final Context context, final Intent intent) {
        final SQLiteDatabase AlertDatabase = new AlertOpenHelper(context).getWritableDatabase();

        final PendingResult pendingResult = goAsync();
        AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {

                // Get alerts
                Cursor alertCursor = AlertDatabase.rawQuery("SELECT * FROM " + context.getString(R.string.sql_table_name), null);


                // Debug
                StringBuilder sb = new StringBuilder();
                sb.append("Action: " + intent.getAction() + "\n");
                sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
                String log = sb.toString();
                Log.d(TAG, log);

                // Must call finish() so the BroadcastReceiver can be recycled.
                pendingResult.finish();
                return log;
            }
        };
        asyncTask.execute();
    }
}
