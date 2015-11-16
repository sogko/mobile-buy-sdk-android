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

package com.shopify.buy.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.shopify.buy.R;
import com.shopify.buy.dataprovider.providers.DefaultSearchProvider;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.common.BaseFragment;
import com.shopify.buy.ui.common.RecyclerViewHolder;
import com.shopify.buy.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchFragment extends BaseFragment implements RecyclerViewHolder.ClickListener<Product>, SearchView.OnQueryTextListener {

    SearchFragmentView view;
    RecyclerView recyclerView;
    SearchView searchView;
    InputMethodManager inputMethodManager;

    OnSearchItemSelectedListener listener;

    private String query = null;
    private SearchProvider provider = null;

    private final List<Product> products = new ArrayList<>();

    public void setProvider(SearchProvider provider) {
        this.provider = provider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (provider == null) {
            provider = new DefaultSearchProvider(getActivity());
        }

        Bundle bundle = getArguments();

        // Retrieve the search query if it was provided
        if (bundle.containsKey(SearchConfig.EXTRA_SEARCH_QUERY)) {
            query = bundle.getString(SearchConfig.EXTRA_SEARCH_QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (SearchFragmentView) inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        SearchAdapter adapter = new SearchAdapter(getActivity(), theme);
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

        searchView = (SearchView) view.findViewById(R.id.search_view);

        if (!TextUtils.isEmpty(query)) {
            searchView.setQuery(query, true);
        }

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (inputMethodManager == null) return;

                if (hasFocus) {
                    inputMethodManager.showSoftInput(view.findFocus(), InputMethodManager.SHOW_IMPLICIT);
                } else {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Search the products if we already have a query
        fetchProducts();

        if (shop == null) {
            provider.getShop(buyClient, new Callback<Shop>() {
                @Override
                public void success(Shop shop, Response response) {
                    SearchFragment.this.shop = shop;
                    showProductsIfReady();
                }

                @Override
                public void failure(RetrofitError error) {
                    // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/589
                }
            });
        }
    }

    public void setListener(OnSearchItemSelectedListener listener) {
        this.listener = listener;
    }

    private void fetchProducts() {
        // We only search products if our query is at least 3 characters
        if (query == null || query.trim().length() < 3) {
            return;
        }

        Callback callback = new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                setProducts(products);
                showProductsIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/589
            }
        };

        provider.searchProducts(query, buyClient, callback);
    }

    private void showProductsIfReady() {
        if (!viewCreated || CollectionUtils.isEmpty(products) || shop == null) {
            return;
        } else {
            SearchAdapter adapter = (SearchAdapter) recyclerView.getAdapter();
            adapter.setShop(shop);
            adapter.setProducts(products);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, View viewHolder, Product product) {
        if (listener != null) {
            listener.onSearchItemClick(product);
        }
    }

    @Override
    public void onItemLongClick(int position, View viewHolder, Product product) {
        if (listener != null) {
            listener.onSearchItemLongClick(product);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.query = query;
        searchView.clearFocus();
        recyclerView.requestFocus();
        fetchProducts();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        boolean shouldFetch = query == null || (newText != null && newText.length() > query.length());
        query = newText;
        if (shouldFetch) {
            fetchProducts();
        }
        return true;
    }

    public interface OnSearchItemSelectedListener {
        void onSearchItemClick(Product product);

        void onSearchItemLongClick(Product product);
    }

    private synchronized void setProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
    }

}
