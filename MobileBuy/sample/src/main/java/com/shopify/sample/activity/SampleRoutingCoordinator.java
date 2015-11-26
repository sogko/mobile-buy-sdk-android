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

package com.shopify.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShopifyObject;
import com.shopify.buy.ui.ProductDetailsActivity;
import com.shopify.buy.ui.ProductDetailsBuilder;
import com.shopify.buy.ui.common.RoutingCoordinator;
import com.shopify.buy.ui.products.ProductListBuilder;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;

public class SampleRoutingCoordinator implements RoutingCoordinator {

    public SampleRoutingCoordinator() {
    }

    @Override
    public void displayContent(ShopifyObject content, Context context) {
        if (content == null || context == null) {
            throw new IllegalArgumentException("content or context is NULL");
        }

        if (content instanceof Product) {
            displayProduct((Product) content, context);
        } else if (content instanceof Collection) {
            displayCollection((Collection) content, context);
        } else {
            throw new IllegalArgumentException("content must be either a Collection or a Product");
        }
    }

    protected void displayProduct(Product product, Context context) {
        Bundle bundle = new ProductDetailsBuilder(context)
                .setApiKey(BuildConfig.API_KEY)
                .setChannelId(BuildConfig.CHANNEL_ID)
                .setShopDomain(BuildConfig.SHOP_DOMAIN)
                .setApplicationName(context.getString(R.string.app_name))
                .setProduct(product)
                .setShowCartButton(false)
                .buildBundle();
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    protected void displayCollection(Collection collection, Context context) {
        Bundle bundle = new ProductListBuilder()
                .setApiKey(BuildConfig.API_KEY)
                .setChannelId(BuildConfig.CHANNEL_ID)
                .setShopDomain(BuildConfig.SHOP_DOMAIN)
                .setApplicationName(context.getString(R.string.app_name))
                .setCollection(collection)
                .buildBundle();
        Intent intent = new Intent(context, ProductListActivity.class);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // Nothing to write
    }

    public static final Parcelable.Creator<SampleRoutingCoordinator> CREATOR
            = new Parcelable.Creator<SampleRoutingCoordinator>() {
        public SampleRoutingCoordinator createFromParcel(Parcel in) {
            return new SampleRoutingCoordinator(in);
        }

        public SampleRoutingCoordinator[] newArray(int size) {
            return new SampleRoutingCoordinator[size];
        }
    };

    private SampleRoutingCoordinator(Parcel in) {
        // Nothing to read
    }

}
