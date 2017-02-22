package com.unlimited.penguins.battextr;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Drew on 2/19/2017.
 */

class AlertRecyclerAdapter extends RecyclerView.Adapter<AlertRecyclerAdapter.ViewHolder> {
    private ArrayList<AlertItem> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView mCardView;
        TextView mTextView;

        ViewHolder(CardView v) {
            super(v);
            mCardView = v;
            mTextView = (TextView) v.findViewById(R.id.card_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    AlertRecyclerAdapter(Context context, ArrayList<AlertItem> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlertRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Add item to data set
    public void addItem(AlertItem item) throws IOException {
        // Save to internal storage
        try {
            FileOutputStream fos = mContext.openFileOutput(mContext.getString(R.string.save_alerts_file), Context.MODE_APPEND);
            String writeString = item.getString() + System.getProperty("line.separator");
            fos.write(writeString.getBytes());
        } catch (IOException e) {
            Log.d("Drew", "addItem: broke");
        }
        mDataset.add(item);
        notifyItemInserted(mDataset.size()-1);
    }
    
    // Remove item from dataset
    public void removeItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }
}

