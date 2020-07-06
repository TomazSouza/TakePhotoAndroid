package com.example.takephotoandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.takephotoandroid.adapter.ItemAdapter;
import com.example.takephotoandroid.entity.Item;
import com.example.takephotoandroid.exception.ActivityFragmentNullPointerException;
import com.example.takephotoandroid.exception.CallbackNullPointerException;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String FRAGMENT_KEY = "com.example.bottomsheet.CustomBottomSheetDialogFragment";

    private static Callback mCallback;

    private ImageCapture imageCapture;

    public CustomBottomSheetDialogFragment() {}

    ImageCapture.Callback imageCallback = new ImageCapture.Callback() {
        @Override
        public void imageUri(Uri imageUri) {
            mCallback.onCropImage(imageUri);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container);

        if (getArguments() == null) {
            return view;
        }

        imageCapture = new ImageCapture();

        ArrayList<Item> items = getArguments().getParcelableArrayList(Item.ITEMS_KEY);

        ItemAdapter adapter = new ItemAdapter(getActivity(), items);

        GridView gridView = view.findViewById(R.id.gv_items);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(2);

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position == 0) {
                try {
                    imageCapture.startCamera(CustomBottomSheetDialogFragment.this, imageCallback);
                } catch (ActivityFragmentNullPointerException e) {
                    e.printStackTrace();
                } catch (CallbackNullPointerException e) {
                    e.printStackTrace();
                }
            } else if (position == 1) {
                try {
                    imageCapture.startPickImage(CustomBottomSheetDialogFragment.this, imageCallback);
                } catch (ActivityFragmentNullPointerException e) {
                    e.printStackTrace();
                } catch (CallbackNullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        return (view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            imageCapture.onActivityResult(getActivity(), requestCode, resultCode, data);
        } catch (ActivityFragmentNullPointerException e) {
            e.printStackTrace();
        }
    }

    public void registerCallback(Callback callback) {
        if (mCallback == null) {
            mCallback = callback;
        }
    }

    public interface Callback {
        void onCropImage(Uri position);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
