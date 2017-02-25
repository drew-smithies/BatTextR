package com.unlimited.penguins.battextr;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AlertRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AlertItem> myDataset = new ArrayList<AlertItem>();
    private SQLiteDatabase mAlertDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load saved alerts
        loadSavedAlertsSQL();

        // Setup recycler view
        setupRecyclerView();

        // Setup toolbar and FAB
        setupToolbars();
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
        mAlertDatabase = new AlertOpenHelper(this).getWritableDatabase();
        Cursor alertCursor = mAlertDatabase.rawQuery("SELECT * FROM " + getString(R.string.sql_table_name), null);

        int idIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_alert_id));
        int contactNameIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_contact_name));
        int contactDetailIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_contact_detail));
        int alertTypeIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_alert_type));
        int alertDetailIndex = alertCursor.getColumnIndex(getString(R.string.sql_column_alert_detail));

        alertCursor.moveToFirst();
        while (!alertCursor.isAfterLast()) {
            int alertID = alertCursor.getInt(idIndex);
            String thisName = alertCursor.getString(contactNameIndex);
            String thisContactDetail = alertCursor.getString(contactDetailIndex);
            String thisAlerType = alertCursor.getString(alertTypeIndex);
            String thisAlertDetail = alertCursor.getString(alertDetailIndex);

            myDataset.add(new AlertItem(alertID, thisName, thisAlerType, thisContactDetail));
            alertCursor.moveToNext();
        }

        alertCursor.close();
    }

    public void setupRecyclerView() {
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
    }

    public void setupToolbars() {
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add item to recycler view
                mAdapter.addItem(new AlertItem(new Random().nextInt(1000), "Drew Test", "email", "dtest@me.com"), mAlertDatabase);
            }
        });
    }
}
