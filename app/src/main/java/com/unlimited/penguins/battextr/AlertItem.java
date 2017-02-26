package com.unlimited.penguins.battextr;

import android.content.Context;

/**
 * Created by Drew on 2/22/2017.
 */

class AlertItem {
    Integer mID;
    String mName;
    String mType;
    String mDetail;

    public AlertItem(Context context) {
        mID = 0;
        mName = context.getString(R.string.card_default_text);
        mType = "text";
        mDetail = "";
    }

    public AlertItem(int id, String name, String type, String detail){
        mID = id;
        mName = name;
        mType = type;
        mDetail = detail;
    }

    public int getID(){ return mID; }

    public void setID(int id) { mID = id; }

    public void setName(String name) { mName = name; }

    public String getName() {
        return mName;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType(){
        return mType;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public String getDetail() {
        return mDetail;
    }
}
