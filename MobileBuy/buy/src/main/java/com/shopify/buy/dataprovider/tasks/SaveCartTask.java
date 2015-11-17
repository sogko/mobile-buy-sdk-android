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
import android.util.Log;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.sqlite.BuyDatabase;
import com.shopify.buy.model.Cart;

import java.util.concurrent.ExecutorService;

public class SaveCartTask extends BaseTask<Cart> {

    private static final String LOG_TAG = GetShopTask.class.getSimpleName();

    private final Cart cart;
    private final String checkoutToken;
    private final String userId;

    public SaveCartTask(Cart cart, @Nullable String checkoutToken, String userId, BuyDatabase buyDatabase, BuyClient buyClient, Handler handler, ExecutorService executorService) {
        super(buyDatabase, buyClient, null, handler, executorService);
        this.cart = cart;
        this.checkoutToken = checkoutToken;
        this.userId = userId;
    }

    @Override
    public void run() {
        // Carts are only stored locally, not in the cloud
        try {
            buyDatabase.saveCart(cart, userId);

            if (!TextUtils.isEmpty(checkoutToken)) {
                buyDatabase.saveCheckoutToken(checkoutToken, userId);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not save Cart to database.", e);
        }
    }

}
