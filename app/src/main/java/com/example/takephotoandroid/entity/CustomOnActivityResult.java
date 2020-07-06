package com.example.takephotoandroid.entity;

import android.content.Context;
import android.content.Intent;

public class CustomOnActivityResult {

    public static Builder with(Context context) {
        return new Builder(context);
    }


    public static class Builder {

        private Context context;
        private OnActivityResultListener listener;
        private Intent intent;

        public Builder(Context context) {
            this.context = context;
        }

        public void startActivityForResult() {
            ProxyActivity.startActivityForResult(context, intent, listener);
        }

        public Builder setListener(OnActivityResultListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setIntent(Intent intent) {
            this.intent = intent;
            return this;
        }

    }

}
