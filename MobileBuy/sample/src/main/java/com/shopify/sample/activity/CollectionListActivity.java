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

package com.shopify.sample.activity;

import android.content.Intent;
import android.os.Bundle;

import com.shopify.buy.ui.collections.CollectionListBuilder;
import com.shopify.buy.ui.collections.CollectionListFragment;
import com.shopify.buy.ui.products.ProductListBuilder;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import com.shopify.buy.model.Collection;
import com.shopify.sample.activity.base.SampleActivity;

/**
 * The first activity in the app flow. Allows the user to browse the list of collections and drill down into a list of products.
 */
public class CollectionListActivity extends SampleActivity implements CollectionListFragment.OnCollectionListItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.choose_collection);
        setContentView(R.layout.collection_list_layout);

        CollectionListFragment collectionListFragment = new CollectionListBuilder(this)
                .setApiKey(BuildConfig.API_KEY)
                .setChannelid(BuildConfig.CHANNEL_ID)
                .setShopDomain(BuildConfig.SHOP_DOMAIN)
                .setApplicationName(getString(R.string.app_name))
                .buildFragment(null, this);

        collectionListFragment.setListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, collectionListFragment)
                .commit();
    }

    /**
     * When the user picks a collection, launch the product list activity to display the products in that collection.
     *
     * @param collection The currently selected {@link Collection}
     */
    @Override
    public void onCollectionListItemClick(Collection collection) {
        Bundle bundle = new ProductListBuilder(this)
                .setApiKey(BuildConfig.API_KEY)
                .setChannelid(BuildConfig.CHANNEL_ID)
                .setShopDomain(BuildConfig.SHOP_DOMAIN)
                .setApplicationName(getString(R.string.app_name))
                .setCollection(collection)
                .buildBundle();
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onCollectionListItemLongClick(Collection collection) {
        // Nothing to do
    }
}
