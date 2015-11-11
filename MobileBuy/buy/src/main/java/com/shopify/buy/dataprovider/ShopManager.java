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

package com.shopify.buy.dataprovider;

import android.content.Context;
import android.text.TextUtils;

import com.shopify.buy.dataprovider.sqlite.BuyDatabase;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Shop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShopManager {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static ShopManager instance = new ShopManager();

    public static ShopManager getInstance() {
        return instance;
    }

    private BuyDatabase database;
    private Shop shop;
    private String userId;
    private Cart cart;

    private ShopManager() {
    }

    public void loadShopAndCart(final String userId, final Context context) {
        this.userId = userId;

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (database == null) {
                    database = new BuyDatabase(context);
                }
                shop = database.getShop();
                cart = database.getCart(userId);
            }
        });
    }

    public Shop getShop() {
        return shop;
    }

    public Cart getCart() {
        return cart;
    }

    public void saveShop(final Shop shop, final Context context) {
        if (hasChanged(shop)) {
            this.shop = shop;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (database == null) {
                        database = new BuyDatabase(context);
                    }
                    database.saveShop(shop);
                }
            });
        }
    }

    public void saveCart(final Context context) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (database == null) {
                    database = new BuyDatabase(context);
                }
                database.saveCart(cart, userId);
            }
        });
    }

    private boolean hasChanged(Shop newShop) {
        if (newShop == null) {
            return false;
        }
        if (shop == null) {
            return true;
        }
        return !TextUtils.equals(shop.getName(), newShop.getName()) ||
                !TextUtils.equals(shop.getCity(), newShop.getCity()) ||
                !TextUtils.equals(shop.getProvince(), newShop.getProvince()) ||
                !TextUtils.equals(shop.getCountry(), newShop.getCountry()) ||
                !TextUtils.equals(shop.getContactEmail(), newShop.getContactEmail()) ||
                !TextUtils.equals(shop.getCurrency(), newShop.getCurrency()) ||
                !TextUtils.equals(shop.getDescription(), newShop.getDescription()) ||
                !TextUtils.equals(shop.getDomain(), newShop.getDomain()) ||
                !TextUtils.equals(shop.getMyshopifyDomain(), newShop.getMyshopifyDomain()) ||
                !TextUtils.equals(shop.getMoneyFormat(), newShop.getMoneyFormat()) ||
                !TextUtils.equals(shop.getUrl(), newShop.getUrl()) ||
                !shop.getShipsToCountries().equals(newShop.getShipsToCountries()) ||
                !(shop.getPublishedProductsCount() == newShop.getPublishedProductsCount());
    }

}
