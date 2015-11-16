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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.shopify.buy.R;
import com.shopify.buy.customTabs.CustomTabActivityHelper;
import com.shopify.buy.dataprovider.providers.DefaultCartProvider;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;

import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class CheckoutFragment extends BaseFragment {

    protected CheckoutListener checkoutListener;
    protected Button checkoutButton;
    protected Cart cart;

    protected CartProvider provider = null;

    private final AtomicBoolean cancelledCheckout = new AtomicBoolean(false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (provider == null) {
            provider = new DefaultCartProvider(getActivity());
        }
    }

    @Override
    protected void processArguments() {
        super.processArguments();

        Bundle bundle = getArguments();

        String cartJson = bundle.getString(BaseConfig.EXTRA_CART);
        if (!TextUtils.isEmpty(cartJson)) {
            cart = Cart.fromJson(cartJson);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureCheckoutButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkoutButton.setEnabled(true);
        cancelledCheckout.set(false);
    }

    public void setProvider(CartProvider provider) {
        this.provider = provider;
    }

    public void setCheckoutListener(CheckoutListener checkoutListener) {
        this.checkoutListener = checkoutListener;
    }

    protected void configureCheckoutButton() {
        checkoutButton = (Button) getView().findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutButton.setEnabled(false);
                cancelledCheckout.set(false);
                createWebCheckout();

                showProgressDialog(getString(R.string.loading), getString(R.string.loading_checkout_page), new Runnable() {
                    @Override
                    public void run() {
                        checkoutButton.setEnabled(true);
                        cancelledCheckout.set(true);
                    }
                });
            }
        });

    }

    /**
     * Creates a checkout for use with the web checkout flow
     */
    protected void createWebCheckout() {
        // Create the checkout
        buyClient.createCheckout(new Checkout(cart), new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                if (response.getStatus() == HttpURLConnection.HTTP_CREATED) {
                    // Start the web checkout
                    launchWebCheckout(checkout);
                } else {
                    onCheckoutFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                onCheckoutFailure();
            }
        });

    }

    /**
     * Show the error message in a {@link Snackbar}
     */
    private void onCheckoutFailure() {
        dismissProgressDialog();

        CoordinatorLayout snackbarLayout = (CoordinatorLayout) getView().findViewById(R.id.snackbar_location);

        Snackbar snackbar = Snackbar.make(snackbarLayout, R.string.default_checkout_error, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.error_background));

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
                checkoutButton.setEnabled(false);
            }

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                checkoutButton.setEnabled(true);
            }
        });

        snackbar.show();
    }

    /**
     * Launch Chrome, and open the correct url for our {@code Checkout}
     *
     * @param checkout
     */
    private void launchWebCheckout(Checkout checkout) {
        // if the user dismissed the progress dialog before we got here, abort
        if (cancelledCheckout.getAndSet(false)) {
            return;
        }

        dismissProgressDialog();

        String uri = checkout.getWebUrl();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(theme.getAppBarBackgroundColor(getResources()))
                .setShowTitle(true)
                .build();
        CustomTabActivityHelper.openCustomTab(
                getActivity(), customTabsIntent, Uri.parse(uri), new BrowserFallback());


        // The checkout was successfully started, let the listener know.
        if (checkoutListener != null) {
            Bundle bundle = new Bundle();
            bundle.putString(BaseConstants.EXTRA_CHECKOUT, checkout.toJsonString());
            checkoutListener.onSuccess(bundle);
        }
    }


    /**
     * A Fallback that opens any available Browser when Custom Tabs is not available
     */
    private class BrowserFallback implements CustomTabActivityHelper.CustomTabFallback {

        @Override
        public void openUri(Activity activity, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setData(uri);

            try {
                intent.setPackage("com.android.chrome");
                startActivity(intent);

            } catch (Exception launchChromeException) {
                try {
                    // Chrome could not be opened, attempt to us other launcher
                    intent.setPackage(null);
                    startActivity(intent);

                } catch (Exception launchOtherException) {
                    onCheckoutFailure();
                    return;
                }
            }

        }
    }
}
