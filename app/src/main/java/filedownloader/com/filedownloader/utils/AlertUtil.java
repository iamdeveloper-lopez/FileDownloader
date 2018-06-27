package filedownloader.com.filedownloader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.lang.ref.WeakReference;

public class AlertUtil {

    private AlertDialog alert;

    private AlertUtil(Builder builder) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(builder.activity != null ? builder.activity.get() : builder.context.get());
        if (builder.title != null)
            alertBuilder.setTitle(builder.title);
        if (builder.message != null)
            alertBuilder.setMessage(builder.message);
        if (builder.customView != null)
            alertBuilder.setView(builder.customView);
        if (builder.layoutResId != Integer.MIN_VALUE)
            alertBuilder.setView(builder.layoutResId);
        alertBuilder.setCancelable(builder.cancellable);
        if (builder.positiveButton != null && builder.positiveOnClickListener != null)
            alertBuilder.setPositiveButton(builder.positiveButton, builder.positiveOnClickListener);
        if (builder.negativeButton != null && builder.negativeOnClickListener != null)
            alertBuilder.setNegativeButton(builder.negativeButton, builder.negativeOnClickListener);
        if (builder.neutralButton != null && builder.neutralOnClickListener != null)
            alertBuilder.setNeutralButton(builder.neutralButton, builder.neutralOnClickListener);
        alert = alertBuilder.create();

    }

    public View getCustomView(@IdRes int componentId) {
        return ((AlertDialog) alert).findViewById(componentId);
    }

    public static class Builder {
        private WeakReference<Activity> activity;
        private WeakReference<Context> context;
        private String title;
        private String message;
        private View customView;
        private boolean cancellable = true;
        @LayoutRes
        private int layoutResId;
        private CharSequence positiveButton;
        private DialogInterface.OnClickListener positiveOnClickListener;
        private CharSequence negativeButton;
        private DialogInterface.OnClickListener negativeOnClickListener;
        private CharSequence neutralButton;
        private DialogInterface.OnClickListener neutralOnClickListener;

        public Builder(Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        public Builder(Context context) {
            this.context = new WeakReference<>(context);
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setCustomView(View customView) {
            this.customView = customView;
            return this;
        }

        public Builder setCustomView(@LayoutRes int layoutResId) {
            this.layoutResId = layoutResId;
            return this;
        }

        public Builder setCancellable(boolean cancellable) {
            this.cancellable = cancellable;
            return this;
        }

        public Builder setPositiveButton(CharSequence positiveButton, DialogInterface.OnClickListener positiveOnClickListener) {
            this.positiveButton = positiveButton;
            this.positiveOnClickListener = positiveOnClickListener;
            return this;
        }

        public Builder setNegativeButton(CharSequence negativeButton, DialogInterface.OnClickListener negativeOnClickListener) {
            this.negativeButton = negativeButton;
            this.negativeOnClickListener = negativeOnClickListener;
            return this;
        }

        public Builder setNeutralButton(CharSequence neutralButton, DialogInterface.OnClickListener neutralOnClickListener) {
            this.neutralButton = neutralButton;
            this.neutralOnClickListener = neutralOnClickListener;
            return this;
        }

        public AlertUtil create() {
            return new AlertUtil(this);
        }

    }

    public AlertUtil show() {
        if (alert != null) alert.show();
        return this;
    }

    public void dismiss() {
        if (alert != null) alert.dismiss();
    }

    public void cancel() {
        if (alert != null) alert.cancel();
    }

}
