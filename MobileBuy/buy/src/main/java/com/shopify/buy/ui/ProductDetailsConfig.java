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

import android.os.Bundle;

import com.shopify.buy.model.Product;
import com.shopify.buy.ui.common.BaseConfig;

/***
 * Used to serialize data for the {@link ProductDetailsActivity}.
 */
class ProductDetailsConfig extends BaseConfig {

    public static final String EXTRA_SHOP_PRODUCT_ID = "com.shopify.buy.ui.PRODUCT_ID";
    public static final String EXTRA_SHOP_PRODUCT = "com.shopify.buy.ui.PRODUCT";
    public static final String EXTRA_SHOW_CART_BUTTON = "com.shopify.buy.ui.SHOW_CART_BUTTON";

    private String productId;
    private Product product;
    private boolean showCartButton;

    public String getProductId() {
        return productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean shouldShowCartButton() {
        return showCartButton;
    }

    public void showCartButton(boolean showCartButton) {
        this.showCartButton = showCartButton;
    }

    public Bundle toBundle() {
        Bundle bundle = super.toBundle();

        if (productId != null) {
            bundle.putString(EXTRA_SHOP_PRODUCT_ID, productId);
        }

        if (product != null) {
            bundle.putString(EXTRA_SHOP_PRODUCT, product.toJsonString());
        }

        bundle.putBoolean(EXTRA_SHOW_CART_BUTTON, showCartButton);

        return bundle;
    }


}
