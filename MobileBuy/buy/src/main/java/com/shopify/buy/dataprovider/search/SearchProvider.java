package com.shopify.buy.dataprovider.search;

import com.shopify.buy.model.Product;

import java.util.List;

import retrofit.Callback;

/**
 * Created by davepelletier on 15-10-30.
 */
public interface SearchProvider {

    void searchProducts(String query, Callback<List<Product>> callback);

}
