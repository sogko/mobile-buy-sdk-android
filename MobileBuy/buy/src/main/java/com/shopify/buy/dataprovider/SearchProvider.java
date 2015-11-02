package com.shopify.buy.dataprovider;

import com.shopify.buy.model.Product;

import java.util.List;

import retrofit.Callback;

public interface SearchProvider {

    void searchProducts(String query, BuyClient buyClient, Callback<List<Product>> callback);

}
