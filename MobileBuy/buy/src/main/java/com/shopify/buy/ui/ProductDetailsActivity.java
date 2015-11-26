/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.buy.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.shopify.buy.model.Product;
import com.shopify.buy.ui.common.BaseActivity;
import com.shopify.buy.ui.common.BaseFragment;
import com.shopify.buy.ui.common.CheckoutListener;
import com.shopify.buy.utils.DeviceUtils;

/**
 * Activity that shows the details of a {@link Product}.
 */
public class ProductDetailsActivity extends BaseActivity implements CheckoutListener {

    @Override
    protected BaseFragment createFragment() {
        return new ProductDetailsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DeviceUtils.isTablet(getResources())) {
            makeActivityDialog();
        }
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

    // CheckoutListener Callbacks

    public void onSuccess(Bundle bundle) {
        setResult(Activity.RESULT_OK, bundle);
    }

    public void onFailure(Bundle bundle) {
        setResult(Activity.RESULT_CANCELED, bundle);
        finish();
    }

    public void onCancel(Bundle bundle) {
        setResult(Activity.RESULT_CANCELED, bundle);
        finish();
    }

}
