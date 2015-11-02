package com.shopify.buy.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.SearchProvider;
import com.shopify.buy.ui.common.BaseBuilder;
import com.shopify.buy.ui.common.BaseConfig;

public class SearchBuilder extends BaseBuilder<SearchBuilder> {

    /**
     * Create a default SearchBuilder.
     * If this constructor is used, {@link #setShopDomain(String)}, {@link #setApplicationName(String)}, {@link #setApiKey(String)}, {@link #setChannelid(String)}} must be called.
     *
     * @param context context to use for starting the {@code Activity}
     */
    public SearchBuilder(Context context) {
        super(context);
    }

    /**
     * Constructor that will use an existing {@link BuyClient} to configure the {@link SearchFragment}.
     *
     * @param context context to use for launching the {@code Activity}
     * @param client  the {@link BuyClient} to use to configure the SearchFragment
     */
    public SearchBuilder(Context context, BuyClient client) {
        super(context, client);
    }

    @Override
    protected BaseConfig getConfig() {
        if (config == null) {
            config = new SearchConfig();
        }
        return config;
    }

    public SearchBuilder setSearchQuery(String query) {
        ((SearchConfig) config).setSearchQuery(query);
        return this;
    }

    public Bundle buildBundle() {
        // TODO looks like config should be generic in base, lets refactor the config so we can move this function up into the base
        SearchConfig searchConfig = (SearchConfig) config;

        Bundle bundle = super.buildBundle();
        bundle.putAll(searchConfig.toBundle());
        return bundle;
    }

    /**
     * Returns a new {@link SearchFragment} based on the params that have already been passed to the builder.
     *
     * @param provider An optional implementation of {@link SearchProvider}. If you pass null, {@link com.shopify.buy.dataprovider.DefaultSearchProvider} will be used.
     * @param listener An implementation of {@link com.shopify.buy.ui.search.SearchFragment.Listener} which will be notified of user actions.
     * @return A new {@link SearchFragment}.
     */
    public SearchFragment buildFragment(@Nullable SearchProvider provider, SearchFragment.Listener listener) {
        SearchFragment fragment = new SearchFragment();
        fragment.setProvider(provider);
        fragment.setListener(listener);
        fragment.setArguments(buildBundle());
        return fragment;
    }

}
