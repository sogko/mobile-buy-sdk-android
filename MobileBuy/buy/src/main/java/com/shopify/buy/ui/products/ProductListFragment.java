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
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopify.buy.R;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductListFragment extends BaseFragment implements ProductListAdapter.ClickListener {
    private static final String TAG = ProductListFragment.class.getSimpleName();

    ProductListFragmentView view;

    private List<Product> products;
    private List<String> productIds;
    private Collection collection;

    RecyclerView recyclerView;

    Listener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        // Retrieve the list of products if they were provided
        if (bundle.containsKey(ProductListConfig.EXTRA_SHOP_PRODUCTS)) {
            ArrayList<String> productsArrayList = bundle.getStringArrayList(ProductListConfig.EXTRA_SHOP_PRODUCTS);
            if (productsArrayList != null && productsArrayList.size() > 0) {
                for (String productString : productsArrayList) {
                    products.add(Product.fromJson(productString));
                }
            }
        } else if (bundle.containsKey(ProductListConfig.EXTRA_SHOP_PRODUCT_IDS)) {
            productIds = bundle.getStringArrayList(ProductListConfig.EXTRA_SHOP_PRODUCT_IDS);

        } else if (bundle.containsKey(ProductListConfig.EXTRA_SHOP_CHANNEL_ID)) {
            collection = Collection.fromJson(bundle.getString(ProductListConfig.EXTRA_SHOP_COLLECTION));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ProductListFragmentView) inflater.inflate(R.layout.fragment_product_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ProductListAdapter adapter = new ProductListAdapter();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
        if (products == null) {
            fetchProducts();
        }
        showProductsIfReady();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void fetchProducts() {
        Callback callback  = new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                ProductListFragment.this.products = products;
                showProductsIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO add error case listeners
            }
        };

        if (productIds != null && productIds.size() > 0) {
            buyClient.getProducts(productIds, callback);

        } else if (collection != null) {
            // TODO we will need to implement paging. For now fetch the first page.
            buyClient.getProducts(1, collection.getCollectionId(), callback);

        } else {
            //TODO - get all the products or throw error?
        }
    }

    private void showProductsIfReady() {
        if (!viewCreated || products == null) {
            if (!progressDialog.isShowing()) {
                // TODO proper message
                showProgressDialog(getString(R.string.loading), getString(R.string.loading_collection_details), new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                });
            }
            return;
        } else {
            if (progressDialog.isShowing()) {
                dismissProgressDialog();
            }
            ProductListAdapter adapter = (ProductListAdapter) recyclerView.getAdapter();
            adapter.setProducts(products);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, View viewHolder, Product product) {
        Log.i(TAG, "ProductList Item clicked");
        if (listener != null) {
            listener.onItemClick(product);
        }
    }

    @Override
    public void onItemLongClick(int position, View viewHolder, Product product) {
        Log.i(TAG, "ProductList Item long clicked");
        if (listener != null) {
            listener.onItemLongClick(product);
        }
    }

    public interface Listener {
        void onItemClick(Product product);
        void onItemLongClick(Product product);
    }
}
