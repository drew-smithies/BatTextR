package com.unlimited.penguins.battextr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AlertRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AlertItem> myDataset = new ArrayList<>();
    private AlertItemDataHelper mAlertItemDataHelper;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 808;
    private static final int RESULT_PICK_CONTACT = 809;
    private static final String TAG = "Drew_MainActivity";


    /*************************************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSMSPermissions();

        // Load saved alerts
        loadSavedAlertsSQL();

        // Setup recycler view
        setupRecyclerView();

        // Setup toolbar and FAB
        setupToolbars();
    }

    /*************************************************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*************************************************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            showHelpDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*************************************************************************************************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // Permission denied, show warning message
                    showMissingPermissionWarning(this);
                }
                return;
            }
        }
    }

    /*************************************************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // Check for the request code
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            // Contact was not picked
            // TODO: Handle contact not picked
        }
    }


    /*************************************************************************************************************************/
    // Load saved data
    public void loadSavedAlertsSQL(){

        // Get alert items
        mAlertItemDataHelper = new AlertItemDataHelper(this);
        myDataset = mAlertItemDataHelper.getAllAlerts();
    }

    /*************************************************************************************************************************/
    public void setupRecyclerView() {
        // Setup recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AlertRecyclerAdapter(myDataset, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL), -1);

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

    /*************************************************************************************************************************/
    public void setupToolbars() {
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Activity thisActivity = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                thisActivity.startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });
    }

    /*************************************************************************************************************************/
    public void checkSMSPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            // Check if an explanation should be shown
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                
                // Show explanation
                showPermissionExlanation(this);

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    /*************************************************************************************************************************/
    void showPermissionExlanation(final Activity thisActivity){
        // Show an explanation of permission to the user in an alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.perm_dialog_title));
        builder.setMessage(getString(R.string.perm_dialog_message));
        builder.setCancelable(true);

        // Set cancel button
        builder.setNegativeButton(getString(R.string.perm_dialog_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Show message in snackbar that SMS permissions are disabled
                showMissingPermissionWarning(thisActivity);
            }
        });

        // Set cancel listener
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // Show message in snackbar that SMS permissions are disabled
                showMissingPermissionWarning(thisActivity);
            }
        });

        // Set okay button and listener
        builder.setPositiveButton(getString(R.string.perm_dialog_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        });
        builder.show();
    }

    /*************************************************************************************************************************/
    void showMissingPermissionWarning(final Activity thisActivity) {
        Snackbar.make(findViewById(R.id.activity_main), getString(R.string.perm_snackbar_text), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.perm_snackbar_action), new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                })
                .show();
    }

    /*************************************************************************************************************************/
    void contactPicked(Intent intent) {
        String contactName;
        String contactNumber;

        Uri uri = intent.getData();

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        contactName = cursor.getString(nameIndex);
        contactNumber = cursor.getString(phoneIndex);

        mAdapter.addItem(new AlertItem(0, contactName, "text", contactNumber, 1));
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.info_title));
        builder.setMessage(getString(R.string.info_message));
        builder.show();
    }

    public float getBatteryPercent() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int batteryLevel = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batteryScale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        final float batteryPercent = batteryLevel / (float) batteryScale;
        Log.d(TAG, "Battery p = l/s | " + batteryPercent + " = " + batteryLevel + "/" + batteryScale);

        return batteryPercent;
    }


}
