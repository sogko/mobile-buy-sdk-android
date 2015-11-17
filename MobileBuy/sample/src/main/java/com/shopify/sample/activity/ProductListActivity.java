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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Product;
import com.shopify.buy.ui.ProductDetailsBuilder;
import com.shopify.buy.ui.common.ShopifyTheme;
import com.shopify.buy.ui.products.ProductListFragment;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import com.shopify.sample.activity.base.SampleActivity;
import com.shopify.sample.dialog.HSVColorPickerDialog;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This activity allows the user to select a product to purchase from a list of all products in a collection.
 */
public class ProductListActivity extends SampleActivity implements ProductListFragment.OnProductListItemSelectedListener {

    private ShopifyTheme theme;
    private boolean useProductDetailsActivity;
    private View accentColorView;
    private View productViewOptionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.choose_product);
        setContentView(R.layout.product_list_activity);

        useProductDetailsActivity = false;
        theme = new ShopifyTheme(getResources());

        Intent intent = getIntent();

        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }

        ProductListFragment productListFragment = new ProductListFragment();
        if (bundle != null) {
            productListFragment.setArguments(bundle);
        }

        productListFragment.setListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, productListFragment)
                .commit();

        productViewOptionsContainer = findViewById(R.id.product_view_options_container);
        productViewOptionsContainer.setVisibility(View.GONE);

        ((ToggleButton) findViewById(R.id.product_details_activity_toggle)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useProductDetailsActivity = isChecked;
                productViewOptionsContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        ((ToggleButton) findViewById(R.id.theme_style_toggle)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme.setStyle(isChecked ? ShopifyTheme.Style.LIGHT : ShopifyTheme.Style.DARK);
            }
        });

        ((ToggleButton) findViewById(R.id.product_image_bg_toggle)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme.setShowProductImageBackground(isChecked);
            }
        });

        accentColorView = findViewById(R.id.accent_color_view);
        accentColorView.setBackgroundColor(theme.getAccentColor());
        accentColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAccentColor();
            }
        });
    }

    private void chooseAccentColor() {
        HSVColorPickerDialog cpd = new HSVColorPickerDialog(this, theme.getAccentColor(), new HSVColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void colorSelected(Integer color) {
                theme.setAccentColor(color);
                accentColorView.setBackgroundColor(color);
            }
        });
        cpd.setTitle(R.string.choose_accent_color);
        cpd.show();
    }

    /**
     * When the user selects a product, create a new checkout for that product.
     *
     * @param product The {@link Product} to create the checkout for
     */
    private void createCheckout(final Product product) {
        showLoadingDialog(R.string.syncing_data);

        getSampleApplication().createCheckout(product, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                dismissLoadingDialog();
                onCheckoutCreated(checkout);
            }

            @Override
            public void failure(RetrofitError error) {
                onError(error);
            }
        });
    }

    /**
     * If the selected product requires shipping, show the list of shipping rates so the user can pick one.
     * Otherwise, skip to the discounts activity (gift card codes and discount codes).
     *
     * @param checkout The current {@link Checkout}.
     */
    private void onCheckoutCreated(Checkout checkout) {
        if (checkout.isRequiresShipping()) {
            startActivity(new Intent(ProductListActivity.this, ShippingRateListActivity.class));
        } else {
            startActivity(new Intent(ProductListActivity.this, DiscountActivity.class));
        }

    }

    /***
     * Either opens a new {@link com.shopify.buy.ui.ProductDetailsActivity} with the specified parameters,
     * or starts the native checkout flow.
     *
     * @param product
     */
    @Override
    public void onProductListItemClick(Product product) {
        // Start the ProductDetailsActivity
        if (useProductDetailsActivity) {
            ProductDetailsBuilder builder = new ProductDetailsBuilder(this);
            Intent intent = builder.setShopDomain(BuildConfig.SHOP_DOMAIN)
                    .setApiKey(BuildConfig.API_KEY)
                    .setChannelid(BuildConfig.CHANNEL_ID)
                    .setApplicationName(BuildConfig.APPLICATION_ID)
                    .setProduct(product)
                    .setTheme(theme)
                    .setWebReturnToUrl(getString(R.string.web_return_to_url))
                    .setWebReturnToLabel(getString(R.string.web_return_to_label))
                    .setShop(getSampleApplication().getShop())
                    .build();
            startActivity(intent);
        } else {
            // Start the native checkout.
            createCheckout(product);
        }
    }

    @Override
    public void onProductListItemLongClick(Product product) {
        // Nothing to do
    }
}

