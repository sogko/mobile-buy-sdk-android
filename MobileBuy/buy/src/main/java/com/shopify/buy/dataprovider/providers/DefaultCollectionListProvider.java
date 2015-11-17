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

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.tasks.GetCollectionsTask;
import com.shopify.buy.model.Collection;
import com.shopify.buy.ui.collections.CollectionListProvider;

import java.util.List;

import retrofit.Callback;

public class DefaultCollectionListProvider extends DefaultBaseProvider implements CollectionListProvider {

    public DefaultCollectionListProvider(Context context) {
        super(context);
    }

    @Override
    public void getCollections(final BuyClient buyClient, final Callback<List<Collection>> callback) {
        GetCollectionsTask task = new GetCollectionsTask(buyDatabase, buyClient, callback, handler, executorService);
        executorService.execute(task);
    }

}
