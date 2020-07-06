package com.example.takephotoandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.takephotoandroid.R;
import com.example.takephotoandroid.entity.Item;

import java.util.List;

public class ItemAdapter extends Adapter {

    public ItemAdapter(Context context, List<Item> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        Item item = (Item) items.get(i);

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_grid, viewGroup, false);
            view.setTag(holder);

            holder.icon = (ImageView) view.findViewById(R.id.iv_icon);
            holder.label = (TextView) view.findViewById(R.id.tv_label);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.icon.setImageResource(item.getIconId());
        holder.label.setText(item.getLabel());

        return view;
    }

}
