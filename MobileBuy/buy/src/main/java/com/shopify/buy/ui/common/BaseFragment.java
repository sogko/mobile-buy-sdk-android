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

package com.shopify.buy.ui.common;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.ShopifyTheme;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseFragment extends Fragment {

    protected boolean viewCreated;
    protected BuyClient buyClient;
    protected ProgressDialog progressDialog;
    protected Shop shop;
    protected ShopifyTheme theme;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initializeTheme();
        initializeBuyClient();
        initializeProgressDialog();
    }

    protected void initializeBuyClient() {
        Bundle bundle = getArguments();

        // Retrieve all the items required to create a BuyClient
        String apiKey = bundle.getString(BaseConfig.EXTRA_SHOP_API_KEY);
        String shopDomain = bundle.getString(BaseConfig.EXTRA_SHOP_DOMAIN);
        String channelId = bundle.getString(BaseConfig.EXTRA_SHOP_CHANNEL_ID);
        String applicationName = bundle.getString(BaseConfig.EXTRA_SHOP_APPLICATION_NAME);

        // Retrieve the optional settings
        String webReturnToUrl = bundle.getString(BaseConfig.EXTRA_WEB_RETURN_TO_URL);
        String webReturnToLabel = bundle.getString(BaseConfig.EXTRA_WEB_RETURN_TO_LABEL);

        // If we have a full shop object in the bundle, we won't need to fetch it
        if (bundle.containsKey(BaseConfig.EXTRA_SHOP_SHOP)) {
            shop = Shop.fromJson(bundle.getString(BaseConfig.EXTRA_SHOP_SHOP));
        }

        // Create the BuyClient
        buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, channelId, applicationName);

        // Set the optional web return to values
        if (!TextUtils.isEmpty(webReturnToUrl)) {
            buyClient.setWebReturnToUrl(webReturnToUrl);
        }

        if (!TextUtils.isEmpty(webReturnToLabel)) {
            buyClient.setWebReturnToLabel(webReturnToLabel);
        }
    }

    private void initializeTheme() {
        // First try to get the theme from the bundle, then fallback to a default theme
        Bundle arguments = getArguments();
        if (arguments != null) {
            Parcelable bundleTheme = arguments.getParcelable(BaseConfig.EXTRA_THEME);
            if (bundleTheme != null && bundleTheme instanceof ShopifyTheme) {
                theme = (ShopifyTheme) bundleTheme;
            }
        }
        if (theme == null) {
            theme = new ShopifyTheme(getResources());
        }
    }

    protected void fetchShopIfNecessary(final Callback<Shop> callback) {
        if (shop != null) {
            return;
        }

        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                BaseFragment.this.shop = shop;
                callback.success(shop, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    private void initializeProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
    }

    protected void showProgressDialog(final String title, final String message, final Runnable onCancel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle(title);
                progressDialog.setMessage(message);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        onCancel.run();
                    }
                });
                progressDialog.show();
            }
        });
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    protected Bundle createErrorBundle(int errorCode, String errorMessage) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseConstants.EXTRA_ERROR_CODE, errorCode);
        bundle.putString(BaseConstants.EXTRA_ERROR_MESSAGE, errorMessage);
        return bundle;
    }
}
