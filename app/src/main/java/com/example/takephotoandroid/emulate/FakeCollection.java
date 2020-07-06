package com.example.takephotoandroid.emulate;
import com.example.takephotoandroid.R;
import com.example.takephotoandroid.entity.Action;
import com.example.takephotoandroid.entity.Item;

import java.util.ArrayList;

public final class FakeCollection {

    static private ArrayList<Action> actions;
    static private ArrayList<Item> items;

    private FakeCollection() {
    }

    static public ArrayList<Action> getActions() {
        if (actions == null) {
            actions = new ArrayList<>();
            actions.add(new Action(R.drawable.ic_copy, "Copy"));
            actions.add(new Action(R.drawable.ic_share, "Share"));
            actions.add(new Action(R.drawable.ic_cut, "Cut"));
            actions.add(new Action(R.drawable.ic_remove, "Remove"));
        }
        return (actions);
    }

    static public ArrayList<Item> getItems() {
        if (items == null) {
            items = new ArrayList<>();
            items.add(new Item(R.drawable.ic_action_camera, "Câmera"));
            items.add(new Item(R.drawable.ic_action_library, "Galeria"));
        }
        return (items);
    }

}
