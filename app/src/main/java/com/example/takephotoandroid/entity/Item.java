package com.example.takephotoandroid.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    public static final String ITEMS_KEY = "com.example.bottomsheet.domain";

    private int iconId;
    private String label;

    public Item(int iconId, String label) {
        this.iconId = iconId;
        this.label = label;
    }

    public int getIconId() {
        return iconId;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.iconId);
        dest.writeString(this.label);
    }

    protected Item(Parcel in) {
        this.iconId = in.readInt();
        this.label = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
