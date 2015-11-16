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

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.sqlite.BuyDatabase;

import java.util.concurrent.ExecutorService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class BaseTask<T> implements Runnable {

    protected final BuyDatabase buyDatabase;
    protected final BuyClient buyClient;
    protected final Callback<T> callback;
    protected final Handler handler;
    protected final ExecutorService executorService;

    public BaseTask(BuyDatabase buyDatabase, BuyClient buyClient, Callback<T> callback, Handler handler, ExecutorService executorService) {
        this.buyDatabase = buyDatabase;
        this.buyClient = buyClient;
        this.callback = callback;
        this.handler = handler;
        this.executorService = executorService;

        // bump up the page size for fetches
        if (buyClient != null) {
            this.buyClient.setPageSize(50);
        }
    }

    protected void onSuccess(final T result, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.success(result, response);
                }
            }
        });
    }

    protected void onFailure(final RetrofitError error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.failure(error);
                }
            }
        });
    }

    protected void onFailure(final Exception exception) {
        onFailure(RetrofitError.unexpectedError(null, exception));
    }

}
