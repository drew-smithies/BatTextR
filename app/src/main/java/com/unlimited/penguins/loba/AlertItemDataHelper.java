package com.unlimited.penguins.loba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


/**
 * Created by Drew on 2/25/2017.
 */

public class AlertItemDataHelper {
    SQLiteDatabase mDatabase;
    Context mContext;

    public AlertItemDataHelper(Context context) {
        mContext = context;
        mDatabase = new AlertOpenHelper(mContext).getWritableDatabase();
    }

    public void saveItem(AlertItem item) {

        // Save to SQL
        ContentValues insertValues = new ContentValues(1);
        insertValues.put(mContext.getString(R.string.sql_column_contact_name), item.getName());
        insertValues.put(mContext.getString(R.string.sql_column_contact_detail), item.getDetail());
        insertValues.put(mContext.getString(R.string.sql_column_alert_type), item.getType());
        insertValues.put(mContext.getString(R.string.sql_column_alert_isOn), item.getIsOn());
        long newID = mDatabase.insert(mContext.getString(R.string.sql_table_name), "null", insertValues);
        item.setID((int) newID);
    }

    public void updateItem(AlertItem item) {
        ContentValues updateValues = new ContentValues(1);
        updateValues.put(mContext.getString(R.string.sql_column_alert_isOn), item.getIsOn());
        mDatabase.update(mContext.getString(R.string.sql_table_name), updateValues, "_id=" + item.getID(), null);
    }

    public void deleteItem(AlertItem item) {
        String[] whereArgs = new String[1];
        whereArgs[0] = "" + item.getID() + "";
        mDatabase.delete(mContext.getString(R.string.sql_table_name), "_id=?", whereArgs);
    }

    public ArrayList<AlertItem> getAllAlerts() {

        // Query for alerts
        Cursor alertCursor = mDatabase.rawQuery("SELECT * FROM " + mContext.getString(R.string.sql_table_name), null);

        // Get column indexes
        int idIndex = alertCursor.getColumnIndex(mContext.getString(R.string.sql_column_alert_id));
        int contactNameIndex = alertCursor.getColumnIndex(mContext.getString(R.string.sql_column_contact_name));
        int contactDetailIndex = alertCursor.getColumnIndex(mContext.getString(R.string.sql_column_contact_detail));
        int alertTypeIndex = alertCursor.getColumnIndex(mContext.getString(R.string.sql_column_alert_type));
        int alertDetailIndex = alertCursor.getColumnIndex(mContext.getString(R.string.sql_column_alert_detail));
        int alertIsOnIndex = alertCursor.getColumnIndex(mContext.getString(R.string.sql_column_alert_isOn));

        // Loop over query results and create new alertitems
        ArrayList<AlertItem> thisList = new ArrayList<>();
        alertCursor.moveToFirst();
        while (!alertCursor.isAfterLast()) {
            int alertID = alertCursor.getInt(idIndex);
            String thisName = alertCursor.getString(contactNameIndex);
            String thisContactDetail = alertCursor.getString(contactDetailIndex);
            String thisAlerType = alertCursor.getString(alertTypeIndex);
            String thisAlertDetail = alertCursor.getString(alertDetailIndex);
            int thisAlertIsOn = alertCursor.getInt(alertIsOnIndex);

            thisList.add(new AlertItem(alertID, thisName, thisAlerType, thisContactDetail, thisAlertIsOn));
            alertCursor.moveToNext();
        }

        alertCursor.close();
        return thisList;
    }
}
