package com.example.takephotoandroid;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.takephotoandroid.exception.ActivityFragmentNullPointerException;
import com.example.takephotoandroid.exception.CallbackNullPointerException;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ImageCapture {

    public static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    private static final int PICK_IMAGE_REQUEST_CODE = 1000;
    private static final String PHOTOS = "photos";
    public static File output = null;
    private static Callback mCallback;
    private static Uri outputUri;

    /**
     * content://media/external/images/media/74275 的真实路径 file:///storage/sdcard0/Pictures/X.jpg
     * <p>
     * http://stackoverflow.com/questions/20028319/how-to-convert-content-media-external-images-media-y-to-file-storage-sdc
     *
     * @param context
     * @param contentUri
     * @return
     */
    private static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void startCamera(@NonNull Activity activity, @NonNull Callback callback)
            throws ActivityFragmentNullPointerException, CallbackNullPointerException {

        if (activity == null)
            throw new ActivityFragmentNullPointerException("Fragmento e activity não pode ser nullo");

        if (callback == null)
            throw new CallbackNullPointerException(new StringBuilder()
                    .append("Callback ")
                    .append(ImageCapture.class.getSimpleName())
                    .append(" não pode ser nulo").toString());

        mCallback = callback;
        Intent intent = getIntent(activity);

        activity.startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void startCamera(@NonNull Fragment fragment, @NonNull Callback callback)
            throws ActivityFragmentNullPointerException, CallbackNullPointerException {

        if (fragment == null || fragment.getActivity() == null)
            throw new ActivityFragmentNullPointerException("Fragmento e activity não pode ser nullo");

        if (callback == null)
            throw new CallbackNullPointerException(new StringBuilder()
                    .append("Callback ")
                    .append(ImageCapture.class.getSimpleName())
                    .append(" não poder ser nulo").toString());

        mCallback = callback;
        Intent intent = getIntent(fragment.getActivity());

        fragment.startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void startPickImage(@NonNull Activity activity, @NonNull Callback callback)
            throws ActivityFragmentNullPointerException, CallbackNullPointerException {

        if (activity == null)
            throw new ActivityFragmentNullPointerException("Fragmento e activity não pode ser nulo");

        if (callback == null)
            throw new CallbackNullPointerException(new StringBuilder().
                    append("Callback ")
                    .append(ImageCapture.class.getSimpleName())
                    .append(" não poder ser nulo")
                    .toString());

        mCallback = callback;

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    public void startPickImage(@NonNull Fragment fragment, @NonNull Callback callback)
            throws ActivityFragmentNullPointerException, CallbackNullPointerException {

        if (fragment == null || fragment.getActivity() == null)
            throw new ActivityFragmentNullPointerException("Fragmento e activity não pode ser nulo");

        if (callback == null)
            throw new CallbackNullPointerException(new StringBuilder()
                    .append("Callback ")
                    .append(ImageCapture.class.getSimpleName())
                    .append(" não poder ser nulo")
                    .toString());

        mCallback = callback;

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    public Intent getIntent(Activity activity) {
        outputUri = getOutputMediaFile(activity);
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageCaptureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip = ClipData.newUri(activity.getContentResolver(), "A photo", outputUri);

            imageCaptureIntent.setClipData(clip);
            imageCaptureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    activity.getPackageManager()
                            .queryIntentActivities(imageCaptureIntent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        return imageCaptureIntent;
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) throws ActivityFragmentNullPointerException {
        onActivityResultInner(activity, null, requestCode, resultCode, data);
    }

    public void onActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data) throws ActivityFragmentNullPointerException {
        onActivityResultInner(null, fragment, requestCode, resultCode, data);
    }

    private void onActivityResultInner(@Nullable Activity activity, @Nullable Fragment fragment,
                                       int requestCode, int resultCode, Intent data) throws ActivityFragmentNullPointerException {
        if (resultCode == Activity.RESULT_OK) {
            Context context;

            if (activity == null && fragment == null)
                throw new ActivityFragmentNullPointerException("Fragmento e activity não pode ser nulo");

            if (activity != null) {
                context = activity;
            } else {
                context = fragment.getActivity();
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result != null) {
                    Uri resultUri = result.getUri();
                    mCallback.imageUri(resultUri);
                }
            } else if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {

                if (activity != null)
                    handlePickImage(activity, outputUri);
                else
                    handlePickImage(fragment, outputUri);

            } else if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Intent i = CropImage.activity(data.getData())
                            .getIntent(context);

                    if (fragment != null) {
                        fragment.startActivityForResult(i, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                    } else {
                        activity.startActivityForResult(i, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                }
            }



        }
    }

    private void handlePickImage(@NonNull Activity activity, Uri imageUri) {

        if (activity == null)
            return;

        handlePickImageInner(activity, null, imageUri);
    }

    private void handlePickImage(@NonNull Fragment fragment, Uri imageUri) {
        if (fragment == null)
            return;

        handlePickImageInner(null, fragment, imageUri);
    }

    private void handlePickImageInner(Activity activity, Fragment fragment, Uri imageUri) {
        if (mCallback != null) {
            Context context;

            if (activity == null && fragment == null)
                return;

            if (activity != null) {
                context = activity;
            } else {
                context = fragment.getContext();
            }
            //mCallback.onPickImage(handleUri(context, imageUri));
        }
//        if(!isCropImage){
//            return;
//        }

        CropImage.ActivityBuilder builder = CropImage.activity(imageUri);
        mCallback.cropConfig(builder);

        if (activity != null) {
            builder.start(activity);
        } else {
            builder.start(fragment.getActivity(), fragment);
        }
    }

    private Uri handleUri(Context context, Uri imageUri) {
        if ("content".equals(imageUri.getScheme())) {
            String realPathFromUri = getRealPathFromUri(context, imageUri);
            if (!TextUtils.isEmpty(realPathFromUri)) {
                return Uri.fromFile(new File(realPathFromUri));
            }
        }
        return imageUri;
    }

    private Uri getOutputMediaFile(Activity activity) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        String fileName = new StringBuilder()
                .append(imageFileName)
                .append(".jpg")
                .toString();

        File filesDir = new File(activity.getFilesDir(), PHOTOS);
        output = new File(filesDir, fileName);

        if (output.exists()) {
            output.delete();
        } else {
            if (output == null || output.getParentFile() == null)
                return null;

            output.getParentFile().mkdirs();
        }

        return FileProvider.getUriForFile(activity, AUTHORITY, output);
    }

    public static abstract class Callback {

        public abstract void onPickImage(Uri imageUri);

        abstract void imageUri(Uri imageUri);

        public void cropConfig(CropImage.ActivityBuilder builder) {
//            builder.setMultiTouchEnabled(false)
//                    .setCropShape(CropImageView.CropShape.OVAL)
//                    .setRequestedSize(640, 640)
//                    .setAspectRatio(5, 5);
        }
    }

}
