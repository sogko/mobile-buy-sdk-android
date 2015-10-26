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

package com.shopify.buy.dataprovider;

import android.content.Context;

import com.shopify.buy.dataprovider.tasks.GetCollectionsTask;
import com.shopify.buy.dataprovider.tasks.GetProductsTask;
import com.shopify.buy.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DefaultProductsProvider extends BaseProviderImpl implements ProductsProvider {

    public DefaultProductsProvider(Context context) {
        super(context);
    }

    @Override
    public void getAllProducts(BuyClient buyClient, Callback<List<Product>> callback) {
        GetProductsTask task = new GetProductsTask(buyDatabase, buyClient, callback, handler, executorService);
        executorService.execute(task);
    }

    @Override
    public void getProducts(String collectionId, BuyClient buyClient, Callback<List<Product>> callback) {
        GetProductsTask task = new GetProductsTask(collectionId, buyDatabase, buyClient, callback, handler, executorService);
        executorService.execute(task);
    }

    @Override
    public void getProducts(List<String> productIds, BuyClient buyClient, Callback<List<Product>> callback) {
        GetProductsTask task = new GetProductsTask(productIds, buyDatabase, buyClient, callback, handler, executorService);
        executorService.execute(task);
    }

    @Override
    public void getProduct(final Long productId, BuyClient buyClient, final Callback<Product> callback) {
        List<String> productIds = new ArrayList<>();
        productIds.add(productId.toString());

        Callback listCallback = new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                callback.success(products.get(0), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        };

        GetProductsTask task = new GetProductsTask(productIds, buyDatabase, buyClient, listCallback, handler, executorService);
        executorService.execute(task);
    }

}
