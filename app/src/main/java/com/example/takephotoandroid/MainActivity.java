package com.example.takephotoandroid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.takephotoandroid.emulate.FakeCollection;
import com.example.takephotoandroid.entity.Item;
import com.example.takephotoandroid.exception.ActivityFragmentNullPointerException;
import com.example.takephotoandroid.exception.CallbackNullPointerException;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001;
    private static final int ACESSAR_MEDIA = 1002;
    private static final int ACESSAR_CAMERA = 1003;

    private String permissaoLerEscrever[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String permissaoCamera[] = {
            Manifest.permission.CAMERA
    };

    private String permissaoAcessarMedia[] = {
            Manifest.permission.ACCESS_MEDIA_LOCATION
    };

    private ImageView mImageView;

    private ImageCapture imageCapture;
    private static CustomBottomSheetDialogFragment mCustomBottomSheetDialogFragment;
    private Uri uri;
    private ImageCapture mImageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissaoLerEscrever();

        mImageView = findViewById(R.id.imageView);

        mImageCapture = new ImageCapture();

        Button mBtnTakePhoto = findViewById(R.id.btnTakePhotoId);

        mBtnTakePhoto.setOnClickListener(view -> {

//            try {
//                mImageCapture.startCamera(this, new ImageCapture.Callback() {
//                    @Override
//                    public void onPickImage(Uri imageUri) {
//                        Uri testWithDebug = imageUri;
//                    }
//
//                    @Override
//                    void imageUri(Uri imageUri) {
//                        Uri testWithDebug = imageUri;
//                        mImageView.setImageURI(imageUri);
//                    }
//
//                    @Override
//                    public void cropConfig(CropImage.ActivityBuilder builder) {
//                        builder.setMultiTouchEnabled(false)
//                                .setCropShape(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ?
//                                        CropImageView.CropShape.RECTANGLE : CropImageView.CropShape.OVAL)
//                                .setRequestedSize(640, 640)
//                                .setAspectRatio(5, 5);
//                    }
//                });
//            } catch (ActivityFragmentNullPointerException e) {
//                e.printStackTrace();
//            } catch (CallbackNullPointerException e) {
//                e.printStackTrace();
//            }

            Bundle b = new Bundle();
            b.putParcelableArrayList(Item.ITEMS_KEY, FakeCollection.getItems());

            mCustomBottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
            CustomBottomSheetDialogFragment.Callback callback = new CustomBottomSheetDialogFragment.Callback() {
                @Override
                void onCropImage(Uri imageUri) {
                    mImageView.setImageURI(imageUri);
                }

                @Override
                void cropConfig(CropImage.ActivityBuilder builder) {
                    builder.setMultiTouchEnabled(false)
                            .setCropShape(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ?
                                    CropImageView.CropShape.RECTANGLE : CropImageView.CropShape.OVAL)
                            .setRequestedSize(640, 640)
                            .setAspectRatio(5, 5);
                }
            };

            mCustomBottomSheetDialogFragment.registerCallback(callback);
            mCustomBottomSheetDialogFragment.setArguments(b);
            mCustomBottomSheetDialogFragment.show(getSupportFragmentManager(),
                    CustomBottomSheetDialogFragment.FRAGMENT_KEY);


        });

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;

        Cursor cursor = MainActivity.this
                .getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        try {
//            mImageCapture.onActivityResult(this, requestCode, resultCode, data);
//        } catch (ActivityFragmentNullPointerException e) {
//            e.printStackTrace();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void permissaoLerEscrever() {

        if (ActivityCompat.checkSelfPermission(this, permissaoLerEscrever[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissaoLerEscrever[0])) {
                showCustomDialog(permissaoLerEscrever, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, permissaoLerEscrever, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
         acessarCamera();
        }
    }

    private void acessarCamera() {
        if (ActivityCompat.checkSelfPermission(this, permissaoCamera[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissaoCamera[0])) {
                showCustomDialog(permissaoCamera, ACESSAR_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, permissaoCamera, ACESSAR_CAMERA);
            }
        } else {
            boolean checkVersion = Build.VERSION.SDK_INT >=  Build.VERSION_CODES.P;
            if (checkVersion) {
                accesarMedia();
            }
        }
    }

    private void accesarMedia() {
        if (ActivityCompat.checkSelfPermission(this, permissaoAcessarMedia[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissaoAcessarMedia[0])) {
                showCustomDialog(permissaoCamera, ACESSAR_MEDIA);
            } else {
                ActivityCompat.requestPermissions(this, permissaoAcessarMedia, ACESSAR_MEDIA);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        permissaoLerEscrever();
                        break;
                    }
                }
                break;
            case ACESSAR_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        acessarCamera();
                        break;
                    }
                }
                break;
            case ACESSAR_MEDIA:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // Sucesso
                        accesarMedia();
                        break;
                    }
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showCustomDialog(String[] permissaoLerEscrever, int readExternalStorageRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ler imagem da galeria?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this, permissaoLerEscrever, readExternalStorageRequestCode);
                dialog.dismiss();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}