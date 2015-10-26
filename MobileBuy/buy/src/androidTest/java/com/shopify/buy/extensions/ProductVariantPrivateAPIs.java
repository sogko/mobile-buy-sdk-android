package com.shopify.buy.extensions;

import com.shopify.buy.model.ProductVariant;

/**
 * Wrapper exposing internal API for testing
 */
public class ProductVariantPrivateAPIs extends ProductVariant {

    public ProductVariantPrivateAPIs() {
        super(0, null, null, null, 0, null, null, false, false, 0, 0, null, null, null, false);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
