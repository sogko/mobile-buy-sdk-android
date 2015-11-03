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
import android.content.Intent;
import android.os.Bundle;

import com.shopify.buy.R;
import com.shopify.buy.model.Product;
import com.shopify.buy.ui.common.BaseActivity;
import com.shopify.buy.ui.common.CheckoutListener;

/**
 * Activity that shows the details of a {@link Product}.
 */
public class ProductDetailsActivity extends BaseActivity implements CheckoutListener {

    protected ProductDetailsFragment productDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            productDetailsFragment = getFragment();

            Intent intent = getIntent();
            if (intent != null) {
                productDetailsFragment.setArguments(intent.getExtras());
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_details_activity, productDetailsFragment)
                    .commit();
        } else {
            productDetailsFragment = (ProductDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.product_details_activity);
        }

        setContentView(R.layout.activity_product_details);
    }

    private ProductDetailsFragment getFragment() {
        if (productDetailsFragment == null) {
            productDetailsFragment = new ProductDetailsFragment();
        }
        return productDetailsFragment;
    }

    // ProductDetailsListener Callbacks

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

    private void setResult(int resultCode, Bundle bundle) {
        Intent intent = getIntent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        setResult(resultCode, intent);
    }

}
