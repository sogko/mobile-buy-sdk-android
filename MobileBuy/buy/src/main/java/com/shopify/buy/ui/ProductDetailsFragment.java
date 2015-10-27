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

package com.shopify.buy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.shopify.buy.R;
import com.shopify.buy.customTabs.CustomTabActivityHelper;
import com.shopify.buy.customTabs.CustomTabsHelper;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.CartManager;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.common.CheckoutFragment;
import com.shopify.buy.ui.common.CheckoutListener;
import com.shopify.buy.utils.CurrencyFormatter;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * The fragment that controls the presentation of the {@link Product} details.
 */
public class ProductDetailsFragment extends CheckoutFragment {

    private ProductDetailsFragmentView view;

    private Product product;
    private ProductVariant variant;
    private String productId;

    private Button addToCartButton;
    private boolean showCartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ProductDetailsFragmentView) inflater.inflate(R.layout.fragment_product_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view.setTheme(theme);
        configureAddToCartButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        showCartButton = bundle.getBoolean(ProductDetailsConfig.EXTRA_SHOW_CART_BUTTON);

        // Retrieve the id of the Product we are going to display
        productId = bundle.getString(ProductDetailsConfig.EXTRA_SHOP_PRODUCT_ID);

        // If we have a full product object in the bundle, we don't need to fetch it
        if (bundle.containsKey(ProductDetailsConfig.EXTRA_SHOP_PRODUCT)) {
            product = Product.fromJson(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_PRODUCT));
            variant = product.getVariants().get(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (view.isImageAreaExpanded()) {
                view.setImageAreaSize(false);
            } else {
                checkoutListener.onCancel(null);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // fetch the Shop and Product data if we don't have them already
        if (product == null && !TextUtils.isEmpty(productId)) {
            fetchProduct(productId);
        }

        fetchShopIfNecessary(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                showProductIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                checkoutListener.onFailure(createErrorBundle(ProductDetailsConstants.ERROR_GET_SHOP_FAILED, BuyClient.getErrorBody(error)));
            }
        });

        showProductIfReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        addToCartButton.setEnabled(true);
    }

    @Override
    protected Cart getCartForCheckout() {
        Cart cart = new Cart();
        cart.addVariant(variant);
        return cart;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (checkoutListener != null) {
            return;
        }

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            checkoutListener = (CheckoutListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CheckoutListener");
        }
    }

    private void configureAddToCartButton() {
        if (showCartButton) {
            view.findViewById(R.id.cart_button_container).setVisibility(View.VISIBLE);

            addToCartButton = (Button) view.findViewById(R.id.cart_button);
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartManager.getInstance().getCart().addVariant(variant);

                    // TODO the toast is just temporary
                    Toast.makeText(getActivity(), getString(R.string.added_to_cart, variant.getProductTitle()), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            view.findViewById(R.id.cart_button_container).setVisibility(View.GONE);
        }
    }

    private void fetchProduct(final String productId) {
        buyClient.getProduct(productId, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                if (product != null) {
                    // Default to having the first variant selected in the UI
                    ProductVariant variant = product.getVariants().get(0);
                    ProductDetailsFragment.this.product = product;
                    ProductDetailsFragment.this.variant = variant;
                    showProductIfReady();
                } else {
                    checkoutListener.onFailure(createErrorBundle(ProductDetailsConstants.ERROR_GET_PRODUCT_FAILED, "Product id not found: " + productId));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                checkoutListener.onFailure(createErrorBundle(ProductDetailsConstants.ERROR_GET_PRODUCT_FAILED, BuyClient.getErrorBody(error)));
            }
        });
    }

    private void showProductIfReady() {
        // Check for the prerequisites before populating the views
        if (!viewCreated || product == null || shop == null) {
            // we're still loading, make sure we show the progress dialog
            if (!progressDialog.isShowing()) {
                showProgressDialog(getString(R.string.loading), getString(R.string.loading_product_details), new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                });
            }
            return;
        }

        NumberFormat currencyFormat = CurrencyFormatter.getFormatter(Locale.getDefault(), shop.getCurrency());

        // Create a VariantSelectionController which will manage the dialogs that allow the user to pick a product variant
        VariantSelectionController variantSelectionController = new VariantSelectionController(getActivity(), view, product, variant, theme, currencyFormat);
        variantSelectionController.setListener(onVariantSelectedListener);

        // Tell the view that it can populate the product details components now
        view.setCurrencyFormat(currencyFormat);
        view.onProductAvailable(ProductDetailsFragment.this, product, variant);

        // Disable the checkout button if the selected product variant is sold out
        checkoutButton.setEnabled(variant.isAvailable());
    }

    private final VariantSelectionController.OnVariantSelectedListener onVariantSelectedListener = new VariantSelectionController.OnVariantSelectedListener() {
        @Override
        public void onVariantSelected(ProductVariant variant) {
            ProductDetailsFragment.this.variant = variant;
            view.setVariant(variant);

            // Disable the checkout button if the selected product variant is sold out
            checkoutButton.setEnabled(variant.isAvailable());
        }
    };
    
}
