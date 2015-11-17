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
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopify.buy.R;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.common.CheckoutFragment;
import com.shopify.buy.ui.common.CheckoutListener;
import com.shopify.buy.ui.common.FabListener;
import com.shopify.buy.ui.common.ShareListener;
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

    private ShareListener shareListener;
    private FabListener fabListener;

    private Product product;
    private ProductVariant variant;
    private String productId;

    private boolean showCartButton;

    public void setShareListener(ShareListener shareListener) {
        this.shareListener = shareListener;
    }

    public void setFabListener(FabListener fabListener) {
        this.fabListener = fabListener;
    }

    void onSharePressed() {
        if (shareListener != null) {
            shareListener.onProductShared(product);
        }
    }

    void onFabPressed() {
        if (fabListener != null) {
            fabListener.onFabSelected();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ProductDetailsFragmentView) inflater.inflate(R.layout.fragment_product_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view.setTheme(theme);
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

        if (shop == null) {
            provider.getShop(buyClient, new Callback<Shop>() {
                @Override
                public void success(Shop shop, Response response) {
                    ProductDetailsFragment.this.shop = shop;
                    showProductIfReady();
                }

                @Override
                public void failure(RetrofitError error) {
                    checkoutListener.onFailure(createErrorBundle(ProductDetailsConstants.ERROR_GET_SHOP_FAILED, BuyClient.getErrorBody(error)));
                }
            });
        }

        if (cart == null) {
            provider.getCart(buyClient, userId, new Callback<Cart>() {
                        @Override
                        public void success(Cart cart, Response response) {
                            ProductDetailsFragment.this.cart = cart;
                            showProductIfReady();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/589
                        }
                    }
            );
        }
    }

    @Override
    protected void createWebCheckout() {
        cart.addVariant(variant);
        view.setVariant(variant);
        super.createWebCheckout();
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

    @Override
    protected void configureCheckoutButton() {
        super.configureCheckoutButton();

        // Override the button if we should show the cart instead of the checkout button
        if (showCartButton) {
            checkoutButton.setText(R.string.add_to_cart);
            checkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cart.addVariant(variant);
                    provider.saveCart(cart, null, buyClient, userId);

                    // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/594
                    Toast.makeText(getActivity(), getString(R.string.added_to_cart, variant.getProductTitle()), Toast.LENGTH_SHORT).show();
                }
            });
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
        final Activity activity = safelyGetActivity();

        if (activity == null) {
            return;
        }

        // Check for the prerequisites before populating the views
        if (!viewCreated || product == null || shop == null || cart == null) {
            // we're still loading, make sure we show the progress dialog
            if (!progressDialog.isShowing()) {
                showProgressDialog(getString(R.string.loading), getString(R.string.loading_product_details), new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
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
        // Only show the share button if the ShareListener has been set
        view.setCurrencyFormat(currencyFormat);
        view.onProductAvailable(ProductDetailsFragment.this, product, variant, shareListener != null, fabListener != null);

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
