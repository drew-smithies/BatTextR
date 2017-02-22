package com.unlimited.penguins.battextr;

/**
 * Created by Drew on 2/22/2017.
 */

class AlertItem {
    String mName;
    String mType;
    String mDetail;

    public AlertItem(String name, String type, String detail){
        mName = name;
        mType = type;
        mDetail = detail;
    }

    public void setName(String name) {
        mName = name;
    }

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
