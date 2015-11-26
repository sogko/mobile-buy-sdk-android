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

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.ProductDetailsActivity;
import com.shopify.buy.ui.ProductDetailsBuilder;
import com.shopify.buy.ui.products.ProductListActivity;
import com.shopify.buy.ui.products.ProductListBuilder;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class BaseFragment extends Fragment {

    private final static String LOG_TAG = BaseFragment.class.getSimpleName();

    // The amount of time in milliseconds to delay between network calls when you are polling for Shipping Rates and Checkout Completion
    protected static final long POLL_DELAY = 1000;

    // If we are polling the status of an incomplete checkout, we need to lock the cart because we don't know whether to delete it or not yet
    // https://cloud.githubusercontent.com/assets/1058176/11193589/79267dfe-8c75-11e5-9dd1-c0301da5bb53.JPG
    protected static final AtomicBoolean isCartLocked = new AtomicBoolean(false);

    protected boolean viewCreated;
    protected BuyClient buyClient;
    protected ProgressDialog progressDialog;
    protected Shop shop;
    protected ShopifyTheme theme;
    protected String userId;
    protected BaseProvider provider;
    protected Handler pollingHandler;
    protected OnProviderFailedListener onProviderFailedListener;
    protected RoutingCoordinator routingCoordinator;

    public void setRoutingCoordinator(RoutingCoordinator routingCoordinator) {
        this.routingCoordinator = routingCoordinator;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyCustomFont(view);
        view.setBackgroundColor(theme.getBackgroundColor(getResources()));
        viewCreated = true;
    }

    private void applyCustomFont(final View view) {
        if (theme == null || view == null) {
            return;
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                applyCustomFont(child);
            }
        } else if (view instanceof TextView) {
            theme.applyCustomFont((TextView) view);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pollingHandler = new Handler();

        setHasOptionsMenu(true);

        parseArguments();

        initializeProgressDialog();
    }

    protected void parseArguments() {
        Bundle bundle = getArguments();

        // Retrieve all the items required to create a BuyClient
        String apiKey = bundle.getString(BaseConfig.EXTRA_SHOP_API_KEY);
        String shopDomain = bundle.getString(BaseConfig.EXTRA_SHOP_DOMAIN);
        String channelId = bundle.getString(BaseConfig.EXTRA_SHOP_CHANNEL_ID);
        String applicationName = bundle.getString(BaseConfig.EXTRA_SHOP_APPLICATION_NAME);

        // Retrieve the optional settings
        String webReturnToUrl = bundle.getString(BaseConfig.EXTRA_WEB_RETURN_TO_URL);
        String webReturnToLabel = bundle.getString(BaseConfig.EXTRA_WEB_RETURN_TO_LABEL);
        String shopJson = bundle.getString(BaseConfig.EXTRA_SHOP);

        // We require a user id for reading and writing carts to the DB
        userId = bundle.getString(BaseConfig.EXTRA_USER_ID);
        if (TextUtils.isEmpty(userId)) {
            userId = getString(R.string.default_user_id);
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

        if (!TextUtils.isEmpty(shopJson)) {
            shop = Shop.fromJson(shopJson);
        }

        // First try to get the ShopifyTheme and RoutingCoordinator from the bundle, then fallback to defaaults
        Bundle arguments = getArguments();
        if (arguments != null) {
            Parcelable bundleTheme = arguments.getParcelable(BaseConfig.EXTRA_THEME);
            if (bundleTheme != null && bundleTheme instanceof ShopifyTheme) {
                theme = (ShopifyTheme) bundleTheme;
            }

            Parcelable bundleRoutingCoordinator = arguments.getParcelable(BaseConfig.EXTRA_ROUTING_COORDINATOR);
            if (bundleRoutingCoordinator != null & bundleRoutingCoordinator instanceof RoutingCoordinator) {
                routingCoordinator = (RoutingCoordinator) bundleRoutingCoordinator;
            }
        }
        if (theme == null) {
            theme = new ShopifyTheme(getResources());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // fetch data if we need to
        fetchDataIfNecessary();

        // show the view now if we already have the data
        showViewIfReady();

        // If we launched from the return URL on the checkout completion page, we know that the checkout was successful so let's delete the cart and checkout
        Intent intent = getActivity().getIntent();
        Uri uri = intent.getData();
        String returnScheme = buyClient.getWebReturnToUrl();
        if (uri != null && !TextUtils.isEmpty(returnScheme) && (uri.getScheme().contains(returnScheme) || returnScheme.contains(uri.getScheme()))) {
            provider.deleteCheckout(buyClient, userId, true);
            onCartDeleted();
        }

        // If the current user ID is tied to a checkout token, lock the cart while we query the checkout status.
        provider.getCheckoutToken(buyClient, userId, new Callback<String>() {
            @Override
            public void success(String token, Response response) {
                if (!TextUtils.isEmpty(token)) {
                    isCartLocked.set(true);
                    processCheckoutToken(token);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // Empty
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

    protected AppCompatActivity safelyGetActivity() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        return activity;
    }

    /**
     * Fetches data required for the view to be loaded.  This is called on the main thread, so any long
     * running tasks should be executed asynchronously.
     */
    protected abstract void fetchDataIfNecessary();

    /**
     * Checks the preconditions for the view to be shown, and populates the views if the preconditions are met.
     */
    protected abstract void showViewIfReady();

    public void setOnProviderFailedListener(OnProviderFailedListener onProviderFailedListener) {
        this.onProviderFailedListener = onProviderFailedListener;
    }

    protected void onProviderError(RetrofitError error) {
        if (onProviderFailedListener != null) {
            onProviderFailedListener.onProviderFailed(error);
        }
    }

    protected void processCheckoutToken(final String checkoutToken) {
        buyClient.getCheckout(checkoutToken, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                if (checkout.hasOrderId()) {
                    onCheckoutComplete();
                } else {
                    pollCheckoutCompletionStatus(checkout);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // Checkout does not exist, delete the checkout token but keep the cart
                provider.deleteCheckout(buyClient, userId, false);
                isCartLocked.set(false);
            }
        });
    }

    protected void pollCheckoutCompletionStatus(final Checkout checkout) {
        buyClient.getCheckoutCompletionStatus(checkout, new Callback<Boolean>() {
            @Override
            public void success(Boolean complete, Response response) {
                if (complete) {
                    onCheckoutComplete();
                } else {
                    pollingHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pollCheckoutCompletionStatus(checkout);
                        }
                    }, POLL_DELAY);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // If the checkout status check returned an error, we need to know whether the checkout has expired.
                // We never delete the cart here because we want to allow the user to attempt the same checkout again.
                try {
                    // Only delete the checkout token if the checkout has expired
                    if (checkout.getReservationTimeLeft() <= 0) {
                        provider.deleteCheckout(buyClient, userId, false);
                    }
                } finally {
                    // No matter what, we need to unlock the cart for the user
                    isCartLocked.set(false);
                }
            }
        });
    }

    private void onCheckoutComplete() {
        // Delete the checkout token and the old cart, and unlock the cart for the user
        provider.deleteCheckout(buyClient, userId, true);
        onCartDeleted();
        isCartLocked.set(false);
    }

    /**
     * Override to be notified when the cart is deleted;
     */
    protected void onCartDeleted() {
        // Empty
    }

    protected void launchProductListActivity(Collection collection) {
        ProductListBuilder builder = new ProductListBuilder();
        populateBuilder(builder);
        builder.setCollection(collection);

        Intent intent = builder.buildIntent();
        intent.setClass(getActivity(), ProductListActivity.class);

        getActivity().startActivity(intent);
    }

    protected void launchProductDetailsActivity(Product product) {
        ProductDetailsBuilder builder = new ProductDetailsBuilder(getActivity());
        populateBuilder(builder);
        builder.setProduct(product);
        builder.setShowCartButton(true);

        Intent intent = builder.buildIntent();
        intent.setClass(getActivity(), ProductDetailsActivity.class);

        getActivity().startActivity(intent);
    }

    protected void populateBuilder(BaseBuilder builder) {
        builder.setShopDomain(buyClient.getShopDomain())
                .setApiKey(buyClient.getApiKey())
                .setChannelId(buyClient.getChannelId())
                .setApplicationName(buyClient.getApplicationName())
                .setShop(shop)
                .setTheme(theme)
                .setUserId(userId)
                .setWebReturnToLabel(buyClient.getWebReturnToLabel())
                .setWebReturnToUrl(buyClient.getWebReturnToUrl());
    }

}
