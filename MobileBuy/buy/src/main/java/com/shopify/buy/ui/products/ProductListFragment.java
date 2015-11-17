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
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.shopify.buy.R;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.dataprovider.providers.DefaultProductsProvider;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.common.BaseFragment;
import com.shopify.buy.ui.common.RecyclerViewHolder;
import com.shopify.buy.utils.CollectionUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductListFragment extends BaseFragment implements RecyclerViewHolder.ClickListener<Product> {

    ProductListFragmentView view;

    private List<Product> products;
    private List<String> productIds;
    private Collection collection;

    private static final String SAVED_LAYOUT_STATE = "saved_layout_state";
    private Parcelable layoutState;

    RecyclerView recyclerView;

    OnProductListItemSelectedListener listener;

    private ProductsProvider provider = null;

    public void setProvider(ProductsProvider provider) {
        this.provider = provider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (provider == null) {
            provider = new DefaultProductsProvider(safelyGetActivity());
        }

        Bundle bundle = getArguments();
        parseProducts(bundle);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            parseProducts(savedInstanceState);
            layoutState = savedInstanceState.getParcelable(SAVED_LAYOUT_STATE);
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (products != null) {
            outState.putString(ProductListConfig.EXTRA_SHOP_PRODUCTS, BuyClientFactory.createDefaultGson().toJson(products));
        }

        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            outState.putParcelable(SAVED_LAYOUT_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ProductListFragmentView) inflater.inflate(R.layout.fragment_product_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ProductListAdapter adapter = new ProductListAdapter(safelyGetActivity(), theme);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(safelyGetActivity(), 2));

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

        if (shop == null) {
            provider.getShop(buyClient, new Callback<Shop>() {
                @Override
                public void success(Shop shop, Response response) {
                    ProductListFragment.this.shop = shop;
                    showProductsIfReady();
                }

                @Override
                public void failure(RetrofitError error) {
                    // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/589
                }
            });
        }
    }

    private void parseProducts(Bundle bundle) {
        // Retrieve the list of products if they were provided
        if (bundle.containsKey(ProductListConfig.EXTRA_SHOP_PRODUCTS)) {
            String productsJson = bundle.getString(ProductListConfig.EXTRA_SHOP_PRODUCTS);

            if (!TextUtils.isEmpty(productsJson)) {
                products = BuyClientFactory.createDefaultGson().fromJson(productsJson, new TypeToken<List<Product>>() {}.getType());
            }

        } else if (bundle.containsKey(ProductListConfig.EXTRA_SHOP_PRODUCT_IDS)) {
            productIds = bundle.getStringArrayList(ProductListConfig.EXTRA_SHOP_PRODUCT_IDS);

        } else if (bundle.containsKey(ProductListConfig.EXTRA_SHOP_COLLECTION)) {
            collection = Collection.fromJson(bundle.getString(ProductListConfig.EXTRA_SHOP_COLLECTION));
        }
    }

    public void setListener(OnProductListItemSelectedListener listener) {
        this.listener = listener;
    }

    private void fetchProducts() {
        Callback callback = new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                ProductListFragment.this.products = products;
                showProductsIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/589
            }
        };

        if (!CollectionUtils.isEmpty(productIds)) {
            provider.getProducts(productIds, buyClient, callback);
        } else if (collection != null) {
            provider.getProducts(collection.getCollectionId(), buyClient, callback);
        } else {
            provider.getAllProducts(buyClient, callback);
        }
    }

    private void showProductsIfReady() {
        final AppCompatActivity activity = safelyGetActivity();

        if (activity == null) {
            return;
        }

        if (!viewCreated || products == null || shop == null) {
            if (!progressDialog.isShowing()) {
                showProgressDialog(getString(R.string.loading), getString(R.string.loading_collection_details), new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                });
            }
        } else {
            if (progressDialog.isShowing()) {
                dismissProgressDialog();
            }
            ProductListAdapter adapter = (ProductListAdapter) recyclerView.getAdapter();
            adapter.setShop(shop);
            adapter.setProducts(products);
            adapter.notifyDataSetChanged();

            // restore the layout state if there was any
            if (layoutState != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(layoutState);
            }
        }
    }

    @Override
    public void onItemClick(int position, View viewHolder, Product product) {
        if (listener != null) {
            listener.onProductListItemClick(product);
        }
    }

    @Override
    public void onItemLongClick(int position, View viewHolder, Product product) {
        if (listener != null) {
            listener.onProductListItemLongClick(product);
        }
    }

    public interface OnProductListItemSelectedListener {
        void onProductListItemClick(Product product);

        void onProductListItemLongClick(Product product);
    }
}
