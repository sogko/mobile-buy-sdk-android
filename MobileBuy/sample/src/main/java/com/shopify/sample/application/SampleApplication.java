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

package com.shopify.sample.application;

import android.app.Application;
import android.text.TextUtils;

import com.instabug.library.Instabug;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Application class that maintains instances of BuyClient and Checkout for the lifetime of the app.
 */
public class SampleApplication extends Application {

    private BuyClient buyClient;
    private Checkout checkout;

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO remove this for 1.3 release - https://github.com/Shopify/mobile-buy-sdk-android-private/issues/634
        Instabug.initialize(this, "29c4335f7aa30ca0c8be5c969d6fbf0f");

        initializeBuyClient();
    }

    private void initializeBuyClient() {
        String shopUrl = BuildConfig.SHOP_DOMAIN;
        if (TextUtils.isEmpty(shopUrl)) {
            throw new IllegalArgumentException("You must populate the 'shop_url' entry in strings.xml, in the form '<myshop>.myshopify.com'");
        }

        String shopifyApiKey = BuildConfig.API_KEY;
        if (TextUtils.isEmpty(shopifyApiKey)) {
            throw new IllegalArgumentException("You must populate the 'shopify_api_key' entry in strings.xml");
        }

        String channelId = BuildConfig.CHANNEL_ID;
        if (TextUtils.isEmpty(channelId)) {
            throw new IllegalArgumentException("You must populate the 'channel_id' entry in the strings.xml");
        }

        String applicationName = getPackageName();

        /**
         * Create the BuyClient
         */
        buyClient = BuyClientFactory.getBuyClient(shopUrl, shopifyApiKey, channelId, applicationName);
    }

    /**
     * Create a new checkout with the selected product. For convenience in the sample app we will hardcode the user's shipping address.
     * The shipping rates fetched in ShippingRateListActivity will be for this address.
     *
     * @param product  The {@link Product} to create the {@link Checkout} for.
     * @param callback The {@link Callback} that will be called when the Checkout creation completes.
     */
    public void createCheckout(final Product product, final Callback<Checkout> callback) {
        Cart cart = new Cart();
        cart.addVariant(product.getVariants().get(0));

        checkout = new Checkout(cart);

        Address address = new Address();
        address.setFirstName("Dinosaur");
        address.setLastName("Banana");
        address.setAddress1("421 8th Ave");
        address.setCity("New York");
        address.setProvince("NY");
        address.setZip("10001");
        address.setCountryCode("US");
        checkout.setShippingAddress(address);

        checkout.setEmail("something@somehost.com");

        checkout.setWebReturnToUrl(getString(R.string.web_return_to_url));
        checkout.setWebReturnToLabel(getString(R.string.web_return_to_label));

        buyClient.createCheckout(checkout, wrapCheckoutCallback(callback));
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public void getShippingRates(final Callback<List<ShippingRate>> callback) {
        buyClient.getShippingRates(checkout.getToken(), callback);
    }

    public void setShippingRate(ShippingRate shippingRate, final Callback<Checkout> callback) {
        checkout.setShippingRate(shippingRate);
        buyClient.updateCheckout(checkout, wrapCheckoutCallback(callback));
    }

    public void setDiscountCode(final String code, final Callback<Checkout> callback) {
        checkout.setDiscountCode(code);
        buyClient.updateCheckout(checkout, wrapCheckoutCallback(callback));
    }

    public void addGiftCard(final String code, final Callback<Checkout> callback) {
        buyClient.applyGiftCard(code, checkout, wrapCheckoutCallback(callback));
    }

    public void storeCreditCard(final CreditCard card, final Callback<Checkout> callback) {
        checkout.setBillingAddress(checkout.getShippingAddress());
        buyClient.storeCreditCard(card, checkout, wrapCheckoutCallback(callback));
    }

    public void completeCheckout(final Callback<Checkout> callback) {
        buyClient.completeCheckout(checkout, wrapCheckoutCallback(callback));
    }

    public void getCheckoutCompletionStatus(final Callback<Boolean> callback) {
        buyClient.getCheckoutCompletionStatus(checkout, callback);
    }

    /**
     * Wraps the callbacks that are provided by the activities so that the checkout ivar is always up to date.
     *
     * @param callback
     * @return
     */
    private Callback<Checkout> wrapCheckoutCallback(final Callback<Checkout> callback) {
        return new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                SampleApplication.this.checkout = checkout;
                callback.success(checkout, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        };
    }

}
