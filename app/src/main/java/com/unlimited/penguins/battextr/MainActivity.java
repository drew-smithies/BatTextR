package com.unlimited.penguins.battextr;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AlertRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AlertItem> myDataset = new ArrayList<AlertItem>();
    private SQLiteDatabase alertDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test data
//        myDataset.add(new AlertItem("John Smith", "email", "jsmith@me.com"));
//        myDataset.add(new AlertItem("Mike Ross", "text", "903 352 6453"));
//        myDataset.add(new AlertItem("Mary Jewel", "text", "909 736 2342"));

        alertDatabase = new AlertOpenHelper(this).getWritableDatabase();

        // Load saved alerts
        loadSavedAlertsSQL();

        // Setup recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new AlertRecyclerAdapter(MainActivity.this, myDataset);
        mRecyclerView.setAdapter(mAdapter);

        // Setup swipe touch helper
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int swipePosition = viewHolder.getAdapterPosition();
                mAdapter.removeItem(swipePosition);
            }

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add item to recycler view
                try {
                    mAdapter.addItem(new AlertItem("Drew Test", "email", "dtest@me.com"), alertDatabase);
                } catch (IOException e) {
                    Log.d("Drew", "onClick: broke");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Load saved data
    public void loadSavedAlertsSQL(){

        Cursor alertCursor = alertDatabase.rawQuery("SELECT * FROM " + getString(R.string.sql_table_name), null);

        String[] alertResults = new String[alertCursor.getCount()];
        int contactNameIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_contact_name));
        int contactDetailIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_contact_detail));
        int alerTypeIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_alert_type));
        int alerDetailIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_alert_detail));

        alertCursor.moveToFirst();
        while (!alertCursor.isAfterLast()) {
            String thisName = alertCursor.getString(contactNameIndex);
            String thisContactDetail = alertCursor.getString(contactDetailIndex);
            String thisAlerType = alertCursor.getString(alerTypeIndex);
            String thisAlertDetail = alertCursor.getString(alerDetailIndex);

            myDataset.add(new AlertItem(thisName, thisAlerType, thisContactDetail));
            alertCursor.moveToNext();
        }

        alertCursor.close();
    }

    public void loadSavedAlerts() {
        try {
            InputStream stream = this.openFileInput(this.getString(R.string.save_alerts_file));
            if(stream != null) {
                InputStreamReader isr = new InputStreamReader(stream);
                BufferedReader br = new BufferedReader(isr);
                String currentLine;
                StringBuilder builder = new StringBuilder();

                while((currentLine = br.readLine()) != null) {
                    myDataset.add(new AlertItem(currentLine));
                    Log.d("Drew", "loadSavedAlerts: line" + currentLine);
                }
            }
        } catch (IOException e) {

        }
    }
}
