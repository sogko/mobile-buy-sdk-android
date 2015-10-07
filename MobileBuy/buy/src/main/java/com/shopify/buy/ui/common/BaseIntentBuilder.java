package com.shopify.buy.ui.common;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.ProductDetailsTheme;

@SuppressWarnings("unchecked")
public abstract class BaseIntentBuilder <T extends BaseIntentBuilder>{

    protected final Context context;

    protected BaseConfig config;

    /**
     * Create a default BaseIntentBuilder.
     *
     * @param context context to use for starting the {@code Activity}
     */
    public BaseIntentBuilder(Context context) {
       this(context, null);
    }

    public BaseIntentBuilder(Context context, BuyClient client) {
        this.context = context;

        config = getConfig();

        if (client != null) {
            config.setShopDomain(client.getShopDomain());
            config.setApiKey(client.getApiKey());
            config.setApplicationName(client.getApplicationName());
            config.setChannelId(client.getChannelId());
            config.setWebReturnToUrl(client.getWebReturnToUrl());
            config.setWebReturnToLabel(client.getWebReturnToLabel());
        }
    }

    protected abstract BaseConfig getConfig();

    public T setShopDomain(String shopDomain) {
        config.setShopDomain(shopDomain);
        return (T) this;
    }

    public T setApiKey(String apiKey) {
        config.setApiKey(apiKey);
        return (T) this;
    }

    public T setChannelid(String channelId) {
        config.setChannelId(channelId);
        return (T) this;
    }

    public T setApplicationName(String applicationName) {
        config.setApplicationName(applicationName);
        return (T) this;
    }

    public T setWebReturnToUrl(String webReturnToUrl) {
        config.setWebReturnToUrl(webReturnToUrl);
        return (T) this;
    }

    public T setWebReturnToLabel(String webReturnToLabel) {
        config.setWebReturnToLabel(webReturnToLabel);
        return (T) this;
    }

    public T setShop(Shop shop) {
        config.setShop(shop);
        return (T) this;
    }

    public T setTheme(ProductDetailsTheme theme) {
        config.setTheme(theme);
        return (T) this;
    }

    public Intent build() {

        if (TextUtils.isEmpty(config.getShopShopDomain())) {
            throw new IllegalArgumentException("shopDomain must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(config.getApiKey())) {
            throw new IllegalArgumentException("apiKey must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(config.getApplicationName())) {
            throw new IllegalArgumentException("applicationName must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(config.getChannelId())) {
            throw new IllegalArgumentException("channelId must be provided, and cannot be empty");
        }

        return new Intent();
    }
}
