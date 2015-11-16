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

package com.shopify.buy.dataprovider.tasks;

import android.os.Handler;
import android.util.Log;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.sqlite.BuyDatabase;
import com.shopify.buy.model.Shop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GetShopTask extends BaseTask<Shop> {

    private static final String LOG_TAG = GetShopTask.class.getSimpleName();

    public GetShopTask(BuyDatabase buyDatabase, BuyClient buyClient, Callback<Shop> callback, Handler handler, ExecutorService executorService) {
        super(buyDatabase, buyClient, callback, handler, executorService);
    }

    @Override
    public void run() {
        final AtomicBoolean foundInDb = new AtomicBoolean(false);

        // check the local database first
        try {
            Shop shop = buyDatabase.getShop();
            if (shop != null) {
                foundInDb.set(true);
                onSuccess(shop, null);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not get Shop from database.", e);
        }

        // need to fetch from the cloud
        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                saveShop(shop);
                if (!foundInDb.get()) {
                    onSuccess(shop, response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!foundInDb.get()) {
                    onFailure(error);
                }
            }
        });
    }

    private void saveShop(final Shop shop) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                buyDatabase.saveShop(shop);
            }
        });
    }

}
