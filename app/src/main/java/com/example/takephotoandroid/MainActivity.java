package com.example.takephotoandroid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import com.example.takephotoandroid.exception.ActivityFragmentNullPointerException;
import com.example.takephotoandroid.exception.CallbackNullPointerException;

public class MainActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001;
    private static final int ACESSAR_MEDIA = 1002;
    private static final int ACESSAR_CAMERA = 1003;

    private String permissaoLerEscrever[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String permissaoCamera[] = {Manifest.permission.CAMERA};
    private String permissaoAcessarMedia[] = {Manifest.permission.ACCESS_MEDIA_LOCATION};

    private AppCompatImageView imageView;

    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissaoLerEscrever();

        imageView = findViewById(R.id.imageView);

        imageCapture = new ImageCapture();

        Button mBtnTakePhoto = findViewById(R.id.btnTakePhotoId);

        mBtnTakePhoto.setOnClickListener(view -> {
            try {
                imageCapture.startCamera(this,  imageUri -> imageView.setImageURI(imageUri));
            } catch (ActivityFragmentNullPointerException e) {
                e.printStackTrace();
            } catch (CallbackNullPointerException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            imageCapture.onActivityResult(this, requestCode, resultCode, data);
        } catch (ActivityFragmentNullPointerException e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void permissaoLerEscrever() {

        if (ActivityCompat.checkSelfPermission(this, permissaoLerEscrever[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissaoLerEscrever[0])) {
                showDialog();
            } else {
                ActivityCompat.requestPermissions(this, permissaoLerEscrever, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
            accesarMedia();
        }
    }

    private void acessarCamera() {
        if (ActivityCompat.checkSelfPermission(this, permissaoCamera[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissaoCamera[0])) {
                showDialog();
            } else {
                ActivityCompat.requestPermissions(this, permissaoCamera, ACESSAR_CAMERA);
            }
        } else {
            permissaoLerEscrever();
        }
    }

    private void accesarMedia() {
        if (ActivityCompat.checkSelfPermission(this, permissaoAcessarMedia[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissaoAcessarMedia[0])) {
                showDialog();
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ler imagem da galeria?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ActivityCompat.requestPermissions(MainActivity.this, mPermissions, READ_EXTERNAL_STORAGE_REQUEST_CODE);
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