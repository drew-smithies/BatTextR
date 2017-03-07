package com.unlimited.penguins.loba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Drew on 2/23/2017.
 */

public class AlertOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private Context mContext;

    AlertOpenHelper(Context context) {
        super(context, context.getString(R.string.sql_database_name), null, DATABASE_VERSION);
        mContext = context;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:

        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + mContext.getString(R.string.sql_table_name) + "( "
                + "_id INTEGER PRIMARY KEY NOT NULL"
                + ", " + mContext.getString(R.string.sql_column_contact_name) + " " + mContext.getString(R.string.sql_column_contact_name_type) + " NOT NULL "
                + ", " + mContext.getString(R.string.sql_column_contact_detail) + " " + mContext.getString(R.string.sql_column_contact_detail_type) + " NOT NULL "
                + ", " + mContext.getString(R.string.sql_column_alert_type) + " " + mContext.getString(R.string.sql_column_alert_type_type) + " NOT NULL "
                + ", " + mContext.getString(R.string.sql_column_alert_detail) + " " + mContext.getString(R.string.sql_column_alert_detail_type)
                + ", " + mContext.getString(R.string.sql_column_alert_isOn) + " " + mContext.getString(R.string.sql_column_alert_isOn_type)
                + ");"
                ;
        db.execSQL(createTableSQL);
    }
}