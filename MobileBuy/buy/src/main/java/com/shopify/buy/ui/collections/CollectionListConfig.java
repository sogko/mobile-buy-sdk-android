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

package com.shopify.buy.ui.collections;

import android.os.Bundle;

import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Collection;
import com.shopify.buy.ui.common.BaseConfig;

import java.util.ArrayList;
import java.util.List;

/***
 * Used to serialize data for the {@link CollectionListActivity}.
 */
class CollectionListConfig extends BaseConfig {

    public static final String EXTRA_SHOP_COLLECTIONS = "com.shopify.buy.ui.COLLECTIONS";

    private List<Collection> collections;

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public Bundle toBundle() {
        Bundle bundle = super.toBundle();

        if (collections != null) {
            String productsJson = BuyClientFactory.createDefaultGson().toJson(collections);
            bundle.putString(EXTRA_SHOP_COLLECTIONS, productsJson);
        }

        return bundle;
    }


}
