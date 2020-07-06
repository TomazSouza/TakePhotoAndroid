package com.example.takephotoandroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.takephotoandroid.adapter.ItemAdapter;
import com.example.takephotoandroid.entity.Item;
import com.example.takephotoandroid.exception.ActivityFragmentNullPointerException;
import com.example.takephotoandroid.exception.CallbackNullPointerException;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String FRAGMENT_KEY = "com.example.bottomsheet.CustomBottomSheetDialogFragment";

    private static Callback mCallback;

    private static final int PICK_IMAGE_REQUEST_CODE = 1000;
    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private Uri file;
    private Uri cameraImageUri;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private String currentPhotoPath;
    private ImageCapture imageCapture;

    public CustomBottomSheetDialogFragment() {}

//    public CustomBottomSheetDialogFragment(Observer observer) {
//        this.mObserver = observer;
//    }

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

        GridView gv = (GridView) view.findViewById(R.id.gv_items);
        gv.setAdapter(adapter);
        gv.setNumColumns(3);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (position == 0) {
//                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
//                    contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
//                    cameraImageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//
//                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
//                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//                    }
                    try {
                        imageCapture.startCamera(CustomBottomSheetDialogFragment.this, imageCallback);
                    } catch (ActivityFragmentNullPointerException e) {
                        e.printStackTrace();
                    } catch (CallbackNullPointerException e) {
                        e.printStackTrace();
                    }
                } else if (position == 1) {
//                    intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
                    try {
                        imageCapture.startPickImage(CustomBottomSheetDialogFragment.this, imageCallback);
                    } catch (ActivityFragmentNullPointerException e) {
                        e.printStackTrace();
                    } catch (CallbackNullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return (view);
    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent();
//    }
//
//    private File getImageFile() {
//        // Create an image file name
//        File imageFile = null;
//        try {
//            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
//            String imageFileName = "JPEG_" + timeStamp + "_";
//            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//
//            if (!storageDir.exists())
//                storageDir.mkdirs();
//
//            imageFile = File.createTempFile(
//                    imageFileName,  /* prefix */
//                    ".jpg",         /* suffix */
//                    storageDir      /* directory */
//            );
//
//            currentPhotoPath = imageFile.getAbsolutePath();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return imageFile;
//    }

    private File getExternalFilesDir(String directoryPictures) {
        return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            imageCapture.onActivityResult(getActivity(), requestCode, resultCode, data);
        } catch (ActivityFragmentNullPointerException e) {
            e.printStackTrace();
        }
//        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Intent i = CropImage.activity(data.getData())
//                        .getIntent(getActivity());
//                startActivityForResult(i, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
//            }
//        }
    }

    public void registerCallback(Callback callback) {
        if (mCallback == null) {
            mCallback = callback;
        }
    }

    public interface Callback {
        void onCropImage(Uri position);
    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    /** Create a file Uri for saving an image or video */
//    private static Uri getOutputMediaFileUri(int type){
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    /** Create a File for saving an image or video */
//    private static File getOutputMediaFile(int type){
//        // To be safe, you should check that the SDCard is mounted
//        // using Environment.getExternalStorageState() before doing this.
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
//        // This location works best if you want the created images to be shared
//        // between applications and persist after your app has been uninstalled.
//
//        // Create the storage directory if it does not exist
//        if (! mediaStorageDir.exists()){
//            if (! mediaStorageDir.mkdirs()){
//                Log.d("MyCameraApp", "failed to create directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE){
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "IMG_"+ timeStamp + ".jpg");
//        } else if(type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "VID_"+ timeStamp + ".mp4");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }
}
