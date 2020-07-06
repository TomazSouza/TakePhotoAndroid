package com.example.takephotoandroid.entity;

import android.content.Intent;

public class ActivityRequest {

    private Intent intent;
    private OnActivityResultListener listener;

    public ActivityRequest(Intent intent,
                           OnActivityResultListener listener) {
        this.intent = intent;
        this.listener = listener;
    }

    public Intent getIntent() {
        return intent;
    }

    public OnActivityResultListener getListener() {
        return listener;
    }
}
