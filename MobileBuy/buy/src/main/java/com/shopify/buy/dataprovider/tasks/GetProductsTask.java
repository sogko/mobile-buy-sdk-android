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
import android.text.TextUtils;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.sqlite.BuyDatabase;
import com.shopify.buy.model.Product;
import com.shopify.buy.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GetProductsTask extends BaseTask<Product> {

    private String collectionId;
    private List<String> productIds;

    public GetProductsTask(BuyDatabase buyDatabase, BuyClient buyClient, Callback<List<Product>> callback, Handler handler, ExecutorService executorService) {
        super(buyDatabase, buyClient, callback, handler, executorService);
    }

    public GetProductsTask(String collectionId, BuyDatabase buyDatabase, BuyClient buyClient, Callback<List<Product>> callback, Handler handler, ExecutorService executorService) {
        super(buyDatabase, buyClient, callback, handler, executorService);
        this.collectionId = collectionId;
    }

    public GetProductsTask(List<String> productIds, BuyDatabase buyDatabase, BuyClient buyClient, Callback<List<Product>> callback, Handler handler, ExecutorService executorService) {
        super(buyDatabase, buyClient, callback, handler, executorService);
        this.productIds = productIds;
    }

    @Override
    public void run() {
        final AtomicBoolean foundInDb = new AtomicBoolean(false);

        // check the local database first
        List<Product> products = null;
        if (!TextUtils.isEmpty(collectionId)) {
            // TODO this case relies on: https://github.com/Shopify/shopify/issues/56585
        } else if (!CollectionUtils.isEmpty(productIds)) {
            products = buyDatabase.getProducts(productIds);
        } else {
            products = buyDatabase.getAllProducts();
        }

        if (!CollectionUtils.isEmpty(products)) {
            foundInDb.set(true);
            onSuccess(products, null);
        }

        // need to fetch from the cloud
        final Callback<List<Product>> callback = new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                saveProducts(products);
                if (!foundInDb.get()) {
                    onSuccess(products, response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!foundInDb.get()) {
                    onFailure(error);
                }
            }
        };

        if (!TextUtils.isEmpty(collectionId)) {
            fetchNextPage(0, callback, new ArrayList<Product>(), new PageFetcher() {
                @Override
                public void fetchPage(int page, Callback<List<Product>> callback) {
                    buyClient.getProducts(page, collectionId, callback);
                }
            });

        } else if (!CollectionUtils.isEmpty(productIds)) {
            buyClient.getProducts(productIds, callback);

        } else {
            fetchNextPage(0, callback, new ArrayList<Product>(), new PageFetcher() {
                @Override
                public void fetchPage(int page, Callback<List<Product>> callback) {
                    buyClient.getProductPage(page, callback);
                }
            });
        }
    }

    private void saveProducts(final List<Product> products) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                buyDatabase.saveProducts(products);
            }
        });
    }

    private void fetchNextPage(final int previousPage, final Callback<List<Product>> callback, final List<Product> results, final PageFetcher pageFetcher) {
        final int page = previousPage + 1;
        pageFetcher.fetchPage(page, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                if (!CollectionUtils.isEmpty(products)) {
                    // found more products on this page, add to the results list and try next page
                    results.addAll(products);
                    fetchNextPage(page, callback, results, pageFetcher);
                } else {
                    // empty page, process completed results
                    callback.success(results, response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    private interface PageFetcher {
        void fetchPage(final int page, final Callback<List<Product>> callback);
    }

}
