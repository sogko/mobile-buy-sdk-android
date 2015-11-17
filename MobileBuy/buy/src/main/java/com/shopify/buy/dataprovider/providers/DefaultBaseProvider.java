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

package com.shopify.buy.dataprovider.providers;

import android.content.Context;
import android.os.Handler;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.sqlite.BuyDatabase;
import com.shopify.buy.dataprovider.tasks.DeleteCheckoutTask;
import com.shopify.buy.dataprovider.tasks.GetCartTask;
import com.shopify.buy.dataprovider.tasks.GetShopTask;
import com.shopify.buy.dataprovider.tasks.SaveCartTask;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.common.BaseProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.Callback;

public class DefaultBaseProvider implements BaseProvider {

    protected static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    protected static BuyDatabase buyDatabase;

    protected final Handler handler;

    public DefaultBaseProvider(Context context) {
        if (buyDatabase == null) {
            buyDatabase = new BuyDatabase(context);
        }
        this.handler = new Handler(context.getMainLooper());
    }

    @Override
    public void getShop(BuyClient buyClient, Callback<Shop> callback) {
        GetShopTask task = new GetShopTask(buyDatabase, buyClient, callback, handler, executorService);
        executorService.execute(task);
    }

    @Override
    public void getCart(final BuyClient buyClient, final String userId, final Callback<Cart> callback) {
        GetCartTask task = new GetCartTask(userId, buyDatabase, buyClient, callback, handler, executorService);
        executorService.execute(task);
    }

    @Override
    public void saveCart(Cart cart, BuyClient buyClient, String userId) {
        SaveCartTask task = new SaveCartTask(cart, userId, buyDatabase, buyClient, handler, executorService);
        executorService.execute(task);
    }

    @Override
    public void deleteCheckout(BuyClient buyClient, String userId, boolean alsoDeleteCart) {
        DeleteCheckoutTask task = new DeleteCheckoutTask(userId, alsoDeleteCart, buyDatabase, buyClient, handler, executorService);
        executorService.execute(task);
    }

}
