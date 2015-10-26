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
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.sqlite.BuyDatabase;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.utils.CollectionUtils;

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

        /*  TODO - once the collection to product mapping exists, we can check the local database first
            https://github.com/Shopify/shopify/issues/56585
        List<Product> products = buyDatabase.getProducts();
        if (!CollectionUtils.isEmpty(products)) {
            foundInDb.set(true);
            onSuccess(products, null);
        }
        */

        // need to fetch from the cloud
        Callback<List<Product>> callback = new Callback<List<Product>>() {
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

        // TODO need to go beyond just page 1
        if (!TextUtils.isEmpty(collectionId)) {
            buyClient.getProducts(1, collectionId, callback);
        } else if (!CollectionUtils.isEmpty(productIds)) {
            buyClient.getProducts(productIds, callback);
        } else {
            buyClient.getProductPage(1, callback);
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

}
