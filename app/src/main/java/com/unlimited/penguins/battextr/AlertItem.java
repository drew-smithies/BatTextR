package com.unlimited.penguins.battextr;

/**
 * Created by Drew on 2/22/2017.
 */

class AlertItem extends Object {
    Integer mID;
    String mObjectString;
    String mName;
    String mType;
    String mDetail;

    public String getString(){
        return mName + '&' + mType + '&' + mDetail;
    }

    public AlertItem(int id, String name, String type, String detail){
        mID = id;
        mName = name;
        mType = type;
        mDetail = detail;
    }

    public AlertItem(String concatString) {
        String[] attr = concatString.split("&");
        mName = attr[0];
        mType = attr[1];
        mDetail = attr[2];
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
