package com.unlimited.penguins.battextr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Drew on 2/19/2017.
 */

class AlertRecyclerAdapter extends RecyclerView.Adapter<AlertRecyclerAdapter.ViewHolder> {
    private ArrayList<AlertItem> mDataset;

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        RelativeLayout mRelativeLayout;
        TextView mTextView;
        TextView mSubTextView;

        ViewHolder(RelativeLayout v) {
            super(v);
            mRelativeLayout = v;
            mTextView = (TextView) v.findViewById(R.id.list_item);
            mSubTextView = (TextView) v.findViewById(R.id.list_item_subtext);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    AlertRecyclerAdapter(ArrayList<AlertItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlertRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Set viewholder text
        holder.mTextView.setText(mDataset.get(position).getName());
        holder.mSubTextView.setText(mDataset.get(position).getDetail());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Add item to data set
    public void addItem(AlertItem item, AlertItemDataHelper dataHelper) {
        dataHelper.saveItem(item);
        mDataset.add(item);
        notifyItemInserted(mDataset.size()-1);
    }
    
    // Remove item from dataset
    public void removeItem(int position, AlertItemDataHelper dataHelper) {
        dataHelper.deleteItem(mDataset.get(position));
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
    }
}

