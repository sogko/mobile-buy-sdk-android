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


import android.os.Bundle;
import android.text.TextUtils;

import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class BuyBuilderConfig {

    public static final String EXTRA_SHOP_DOMAIN = "com.shopify.buy.ui.SHOP_DOMAIN";
    public static final String EXTRA_SHOP_API_KEY = "com.shopify.buy.ui.API_KEY";
    public static final String EXTRA_SHOP_CHANNEL_ID = "com.shopify.buy.ui.CHANNEL_ID";
    public static final String EXTRA_SHOP_APPLICATION_NAME = "com.shopify.buy.ui.SHOP_APPLICATION_NAME";
    public static final String EXTRA_WEB_RETURN_TO_URL = "com.shopify.buy.ui.WEB_RETURN_TO_URL";
    public static final String EXTRA_WEB_RETURN_TO_LABEL = "com.shopify.buy.ui.WEB_RETURN_TO_LABEL";
    public static final String EXTRA_THEME = "com.shopify.buy.ui.THEME";
    public static final String EXTRA_USER_ID = "com.shopify.buy.ui.USER_ID";
    public static final String EXTRA_SHOP = "com.shopify.buy.ui.SHOP";
    public static final String EXTRA_CART = "com.shopify.buy.ui.CART";
    public static final String EXTRA_ROUTING_COORDINATOR = "com.shopify.buy.ui.ROUTING_COORDINATOR";
    public static final String EXTRA_SHOP_PRODUCT_ID = "com.shopify.buy.ui.PRODUCT_ID";
    public static final String EXTRA_SHOP_PRODUCT = "com.shopify.buy.ui.PRODUCT";
    public static final String EXTRA_SHOW_CART_BUTTON = "com.shopify.buy.ui.SHOW_CART_BUTTON";
    public static final String EXTRA_SHOP_COLLECTIONS = "com.shopify.buy.ui.COLLECTIONS";
    public static final String EXTRA_SHOP_PRODUCTS = "com.shopify.buy.ui.PRODUCTS";
    public static final String EXTRA_SHOP_PRODUCT_IDS = "com.shopify.buy.ui.PRODUCT_IDS";
    public static final String EXTRA_SHOP_COLLECTION = "com.shopify.buy.ui.COLLECTION";
    public static final String EXTRA_SEARCH_QUERY = "com.shopify.buy.ui.SEARCH_QUERY";
    public static final String EXTRA_SEARCH_RESULTS = "com.shopify.buy.ui.SEARCH_RESULTS";

    private String shopDomain;
    private String apiKey;
    private String channelId;
    private String applicationName;
    private ShopifyTheme theme;
    private String webReturnToUrl;
    private String webReturnToLabel;
    private String userId;
    private Shop shop;
    private Cart cart;
    private RoutingCoordinator routingCoordinator;
    private String productId;
    private Product product;
    private boolean showCartButton;
    private List<Collection> collections;
    private List<Product> products;
    private List<String> productIds;
    private Collection collection;
    private String query;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getShopShopDomain() {
        return shopDomain;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setTheme(ShopifyTheme theme) {
        this.theme = theme;
    }

    public void setWebReturnToUrl(String webReturnToUrl) {
        this.webReturnToUrl = webReturnToUrl;
    }

    public void setWebReturnToLabel(String webReturnToLabel) {
        this.webReturnToLabel = webReturnToLabel;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setRoutingCoordinator(RoutingCoordinator routingCoordinator) {
        this.routingCoordinator = routingCoordinator;
    }

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

    public void setShowCartButton(boolean showCartButton) {
        this.showCartButton = showCartButton;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public String getSearchQuery() {
        return query;
    }

    public void setSearchQuery(String query) {
        this.query = query;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        if (shopDomain != null) {
            bundle.putString(EXTRA_SHOP_DOMAIN, shopDomain);
        }

        if (apiKey != null) {
            bundle.putString(EXTRA_SHOP_API_KEY, apiKey);
        }

        if (channelId != null) {
            bundle.putString(EXTRA_SHOP_CHANNEL_ID, channelId);
        }

        if (applicationName != null) {
            bundle.putString(EXTRA_SHOP_APPLICATION_NAME, applicationName);
        }

        if (theme != null) {
            bundle.putParcelable(EXTRA_THEME, theme);
        }

        if (webReturnToUrl != null) {
            bundle.putString(EXTRA_WEB_RETURN_TO_URL, webReturnToUrl);
        }

        if (webReturnToLabel != null) {
            bundle.putString(EXTRA_WEB_RETURN_TO_LABEL, webReturnToLabel);
        }

        if (userId != null) {
            bundle.putString(EXTRA_USER_ID, userId);
        }

        if (shop != null) {
            bundle.putString(EXTRA_SHOP, shop.toJsonString());
        }

        if (cart != null) {
            bundle.putString(EXTRA_CART, cart.toJsonString());
        }

        if (routingCoordinator != null) {
            bundle.putParcelable(EXTRA_ROUTING_COORDINATOR, routingCoordinator);
        }
        if (productId != null) {
            bundle.putString(EXTRA_SHOP_PRODUCT_ID, productId);
        }

        if (product != null) {
            bundle.putString(EXTRA_SHOP_PRODUCT, product.toJsonString());
        }

        bundle.putBoolean(EXTRA_SHOW_CART_BUTTON, showCartButton);

        if (collections != null) {
            String productsJson = BuyClientFactory.createDefaultGson().toJson(collections);
            bundle.putString(EXTRA_SHOP_COLLECTIONS, productsJson);
        }

        if (products != null) {
            String productsJson = BuyClientFactory.createDefaultGson().toJson(products);
            bundle.putString(EXTRA_SHOP_PRODUCTS, productsJson);
        }

        if (productIds != null) {
            bundle.putStringArrayList(EXTRA_SHOP_PRODUCT_IDS, new ArrayList<>(productIds));
        }

        if (collection != null) {
            bundle.putString(EXTRA_SHOP_COLLECTION, collection.toJsonString());
        }

        if (!TextUtils.isEmpty(query)) {
            bundle.putString(EXTRA_SEARCH_QUERY, query);
        }

        return bundle;
    }

}
