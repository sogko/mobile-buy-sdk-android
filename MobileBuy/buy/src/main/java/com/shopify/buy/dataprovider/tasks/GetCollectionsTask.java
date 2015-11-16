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
import com.shopify.buy.model.Collection;
import com.shopify.buy.utils.CollectionUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GetCollectionsTask extends BaseTask<List<Collection>> {

    private static final String LOG_TAG = GetCollectionsTask.class.getSimpleName();

    public GetCollectionsTask(BuyDatabase buyDatabase, BuyClient buyClient, Callback<List<Collection>> callback, Handler handler, ExecutorService executorService) {
        super(buyDatabase, buyClient, callback, handler, executorService);
    }

    @Override
    public void run() {
        final AtomicBoolean foundInDb = new AtomicBoolean(false);

        // check the local database first
        try {
            List<Collection> collections = buyDatabase.getCollections();
            if (!CollectionUtils.isEmpty(collections)) {
                foundInDb.set(true);
                onSuccess(collections, null);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not get Collections from database.", e);
        }

        // need to fetch from the cloud
        buyClient.getCollections(new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections, Response response) {
                saveCollections(collections);
                if (!foundInDb.get()) {
                    onSuccess(collections, response);
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

    private void saveCollections(final List<Collection> collections) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                buyDatabase.saveCollections(collections);
            }
        });
    }

}
