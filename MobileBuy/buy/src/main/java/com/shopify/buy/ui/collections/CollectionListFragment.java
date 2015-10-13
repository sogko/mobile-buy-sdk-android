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

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopify.buy.R;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.ui.cart.CartBuilder;
import com.shopify.buy.ui.cart.CartFragment;
import com.shopify.buy.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CollectionListFragment extends BaseFragment implements CollectionListAdapter.ClickListener {
    private static final String TAG = CollectionListFragment.class.getSimpleName();

    CollectionListFragmentView view;

    private List<Collection> collections;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        // Retrieve the list of collections if they were provided
        ArrayList<String> collectionsArrayList = bundle.getStringArrayList(CollectionListConfig.EXTRA_SHOP_COLLECTION);
        if (collectionsArrayList != null && collectionsArrayList.size() > 0) {
            for (String collectionString : collectionsArrayList) {
                collections.add(Collection.fromJson(collectionString));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (CollectionListFragmentView) inflater.inflate(R.layout.fragment_collection_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        CollectionListAdapter adapter = new CollectionListAdapter();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Fetch the Collections if we don't have them
        if (collections == null) {
            fetchCollections();
        }
        showCollectionsIfReady();
    }

    private void fetchCollections() {
        buyClient.getCollections(new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections, Response response) {
                CollectionListFragment.this.collections = collections;
                showCollectionsIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO add error case listeners
            }
        });
    }

    private void showCollectionsIfReady() {
        if (!viewCreated || collections == null) {
            if (!progressDialog.isShowing()) {
                showProgressDialog(getString(R.string.loading), getString(R.string.loading_product_details), new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                });
            }
            return;
        } else {
            // TODO this is temporary.  The view should pull down the progressview when it has populated its subviews
            if (progressDialog.isShowing()) {
                dismissProgressDialog();}
            CollectionListAdapter adapter = (CollectionListAdapter) recyclerView.getAdapter();
            adapter.setCollections(collections);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, View viewHolder, Collection collection) {
        Log.i(TAG, "Collection Item clicked");

        // TODO temporary code for testing cart fragment
        buyClient.getProducts(1, collection.getCollectionId(), new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                Cart cart = new Cart();
                for (Product product : products) {
                    cart.addVariant(product.getVariants().get(0));
                }
                Intent intent = new CartBuilder(getActivity(), buyClient).setCart(cart).buildIntent();
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onItemLongClick(int position, View viewHolder, Collection collection) {
        Log.i(TAG, "Collection Item long clicked");
    }
}
