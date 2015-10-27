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

package com.shopify.buy.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.ProductDetailsTheme;

@SuppressWarnings("unchecked")
public abstract class BaseBuilder<T extends BaseBuilder>{

    protected final Context context;

    protected BaseConfig config;

    /**
     * Create a default BaseBuilder.
     *
     * @param context context to use for starting the {@code Activity}
     */
    public BaseBuilder(Context context) {
       this(context, null);
    }

    public BaseBuilder(Context context, BuyClient client) {
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

    public Bundle buildBundle() {

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

        return config.toBundle();
    }

    public Intent buildIntent() {
        Intent intent = new Intent();
        intent.putExtras(buildBundle());

        return intent;
    }

}
