package com.example.takephotoandroid.adapter;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.takephotoandroid.entity.Item;

import java.util.List;

public abstract class Adapter extends BaseAdapter {

    protected List<?> items;
    protected LayoutInflater inflater;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        Item item = (Item) items.get(i);
        return item.getIconId();
    }

    static class ViewHolder {
        ImageView icon;
        TextView label;
    }
    
}
