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

package com.shopify.buy.ui.products;

import android.os.Bundle;

import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.ui.common.BaseConfig;

import java.util.ArrayList;
import java.util.List;

/***
 * Used to serialize data for the {@link ProductListFragment}.
 */
class ProductListConfig extends BaseConfig {

    public static final String EXTRA_SHOP_PRODUCTS = "com.shopify.buy.ui.PRODUCTS";
    public static final String EXTRA_SHOP_PRODUCT_IDS = "com.shopify.buy.ui.PRODUCT_IDS";
    public static final String EXTRA_SHOP_COLLECTION = "com.shopify.buy.ui.COLLECTION";

    private List<Product> products;
    private List<String> productIds;
    private Collection collection;

    public List<Product> getProducts() {
        return products;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setProductIds(List<String> productIds) { this.productIds = productIds; }

    public void setCollection(Collection collection) { this.collection = collection; }

    public Bundle toBundle() {
        Bundle bundle = super.toBundle();

        if (products != null) {
            String productsJson = BuyClientFactory.createDefaultGson().toJson(products);
            bundle.putString(EXTRA_SHOP_PRODUCTS, productsJson);
        }

        if (productIds != null) {
            bundle.putStringArrayList(EXTRA_SHOP_PRODUCT_IDS, new ArrayList<>(productIds));
        }

        if (collection != null) {
            bundle.putString(EXTRA_SHOP_COLLECTION, collection.toJsonString());
        }

        return bundle;
    }


}
