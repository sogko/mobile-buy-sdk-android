package com.shopify.buy.dataprovider;

import android.content.Context;

import com.shopify.buy.dataprovider.tasks.SearchProductsTask;
import com.shopify.buy.model.Product;

import java.util.List;

import retrofit.Callback;

public class DefaultSearchProvider extends BaseProviderImpl implements SearchProvider {

    public DefaultSearchProvider(Context context) {
        super(context);
    }

    @Override
    public void searchProducts(String query, BuyClient buyClient, Callback<List<Product>> callback) {
        SearchProductsTask task = new SearchProductsTask(query, buyDatabase, buyClient, callback, handler, executorService);
        executorService.execute(task);
    }

}
