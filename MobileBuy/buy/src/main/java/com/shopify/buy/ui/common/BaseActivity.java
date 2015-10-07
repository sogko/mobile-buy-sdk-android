package com.shopify.buy.ui.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.shopify.buy.utils.DeviceUtils;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DeviceUtils.isTablet(getResources())) {
            makeActivityDialog();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null) {
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void makeActivityDialog() {
        // Turn on rotation for large devices
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        // Get the full screen size
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        WindowManager.LayoutParams params = getWindow().getAttributes();

        // Adjust the screen size to make it a dialog
        if (screenHeight > screenWidth) {// portrait mode
            params.width = Math.round(screenWidth * 0.80f);
            params.height = Math.min(Math.round(params.width * 1.25f), screenHeight);
        } else {
            // landscape
            params.height = Math.round(screenHeight * 0.85f);
            params.width = Math.min(Math.round(params.height * 0.80f), screenWidth);
        }

        // Update the window with our new settings
        getWindow().setAttributes(params);
    }

}
