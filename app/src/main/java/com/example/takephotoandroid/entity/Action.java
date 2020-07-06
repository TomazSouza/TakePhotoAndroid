package com.example.takephotoandroid.entity;

import android.os.Parcel;

public class Action extends Item {

    public Action(int iconId, String label) {
        super(iconId, label);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Action(Parcel in) {
        super(in);
    }

    public static final Creator<Action> CREATOR = new Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel parcel) {
            return new Action(parcel);
        }

        @Override
        public Action[] newArray(int i) {
            return new Action[i];
        }
    };

}
