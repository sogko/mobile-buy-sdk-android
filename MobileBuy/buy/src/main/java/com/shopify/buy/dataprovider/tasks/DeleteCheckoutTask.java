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
import com.shopify.buy.model.Cart;

import java.util.concurrent.ExecutorService;

public class DeleteCheckoutTask extends BaseTask<Cart> {

    private static final String LOG_TAG = DeleteCheckoutTask.class.getSimpleName();

    private final String userId;
    private final boolean alsoDeleteCart;

    public DeleteCheckoutTask(String userId, boolean alsoDeleteCart, BuyDatabase buyDatabase, BuyClient buyClient, Handler handler, ExecutorService executorService) {
        super(buyDatabase, buyClient, null, handler, executorService);
        this.userId = userId;
        this.alsoDeleteCart = alsoDeleteCart;
    }

    @Override
    public void run() {
        try {
            buyDatabase.deleteCheckout(userId);

            if (alsoDeleteCart) {
                buyDatabase.deleteCart(userId);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not delete Checkout or Cart from database.", e);
            onFailure(e);
        }
    }
}
