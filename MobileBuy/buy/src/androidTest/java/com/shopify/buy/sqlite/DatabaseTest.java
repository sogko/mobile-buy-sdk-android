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

package com.shopify.buy.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.shopify.buy.dataprovider.sqlite.BuyDatabase;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Image;
import com.shopify.buy.model.Option;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.utils.CollectionUtils;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DatabaseTest extends ShopifyAndroidTestCase {

    class WipeableDatabase extends BuyDatabase {

        public WipeableDatabase(Context context) {
            super(context);
        }

        public void wipe() {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_COLLECTIONS, null, null);
            db.delete(TABLE_PRODUCTS, null, null);
            db.delete(TABLE_IMAGES, null, null);
            db.delete(TABLE_OPTION_VALUES, null, null);
            db.delete(TABLE_OPTIONS, null, null);
            db.delete(TABLE_PRODUCT_VARIANTS, null, null);
        }

    }

    public void testCollectionsTable() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final WipeableDatabase db = new WipeableDatabase(getContext());
        db.wipe();
        buyClient.getCollections(new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> apiCollections, Response response) {
                db.saveCollections(apiCollections);
                List<Collection> dbCollections = db.getCollections();
                for (int i = 0; i < apiCollections.size(); i++) {
                    assertExactMatch(apiCollections.get(i), dbCollections.get(i));
                }
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
        db.close();
    }

    public void testProductsTable() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final WipeableDatabase db = new WipeableDatabase(getContext());
        db.wipe();
        buyClient.getProductPage(1, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> apiProducts, Response response) {
                db.saveProducts(apiProducts);
                List<Product> dbProducts = db.getAllProducts();
                for (int i = 0; i < apiProducts.size(); i++) {
                    assertExactMatch(apiProducts.get(i), dbProducts.get(i));
                }
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
        db.close();
    }

    private void assertExactMatch(Collection c1, Collection c2) {
        assertEquals(c1.getTitle(), c2.getTitle());
        assertEquals(c1.getHandle(), c2.getHandle());
        assertEquals(c1.getCollectionId(), c2.getCollectionId());
        assertEquals(c1.getHtmlDescription(), c2.getHtmlDescription());
        assertEquals(c1.getPublishedAtDate(), c2.getPublishedAtDate());
        assertEquals(c1.getCreatedAtDate(), c2.getCreatedAtDate());
        assertEquals(c1.getUpdatedAtDate(), c2.getUpdatedAtDate());
        assertEquals(c1.isPublished(), c2.isPublished());

        if (c1.getImage() != null || c2.getImage() != null) {
            assertEquals(c1.getImage().getSrc(), c2.getImage().getSrc());
            assertEquals(c1.getImage().getCreatedAt(), c2.getImage().getCreatedAt());
        }
    }

    private void assertExactMatch(Product p1, Product p2) {
        assertEquals(p1.getProductId(), p2.getProductId());
        assertEquals(p1.getChannelId(), p2.getChannelId());
        assertEquals(p1.getTitle(), p2.getTitle());
        assertEquals(p1.getHandle(), p2.getHandle());
        assertEquals(p1.getBodyHtml(), p2.getBodyHtml());
        assertEquals(p1.getPublishedAtDate(), p2.getPublishedAtDate());
        assertEquals(p1.getCreatedAtDate(), p2.getCreatedAtDate());
        assertEquals(p1.getUpdatedAtDate(), p2.getUpdatedAtDate());
        assertEquals(p1.getVendor(), p2.getVendor());
        assertEquals(p1.getProductType(), p2.getProductType());
        assertEquals(p1.getTags(), p2.getTags());
        assertEquals(p1.isAvailable(), p2.isAvailable());
        assertEquals(p1.isPublished(), p2.isPublished());

        List<Image> images1 = p1.getImages();
        List<Image> images2 = p2.getImages();
        if (!CollectionUtils.isEmpty(images1) || !CollectionUtils.isEmpty(images2)) {
            for (int i = 0; i < images1.size() || i < images2.size(); i++) {
                assertExactMatch(images1.get(i), images2.get(i));
            }
        }

        List<ProductVariant> variants1 = p1.getVariants();
        List<ProductVariant> variants2 = p2.getVariants();
        if (!CollectionUtils.isEmpty(variants1) || !CollectionUtils.isEmpty(variants2)) {
            for (int i = 0; i < variants1.size() || i < variants2.size(); i++) {
                assertExactMatch(variants1.get(i), variants2.get(i));
            }
        }

        List<Option> options1 = p1.getOptions();
        List<Option> options2 = p2.getOptions();
        if (!CollectionUtils.isEmpty(options1) || !CollectionUtils.isEmpty(options2)) {
            for (int i = 0; i < options1.size() || i < options2.size(); i++) {
                assertExactMatch(options1.get(i), options2.get(i));
            }
        }
    }

    private void assertExactMatch(Image i1, Image i2) {
        assertEquals(i1.getPosition(), i2.getPosition());
        assertEquals(i1.getProductId(), i2.getProductId());
        assertEquals(i1.getCreatedAt(), i2.getCreatedAt());
        assertEquals(i1.getUpdatedAt(), i2.getUpdatedAt());

        List<Long> ids1 = i1.getVariantIds();
        List<Long> ids2 = i2.getVariantIds();
        if (!CollectionUtils.isEmpty(ids1) || !CollectionUtils.isEmpty(ids2)) {
            for (int i = 0; i < ids1.size() || i < ids2.size(); i++) {
                assertEquals(ids1.get(i), ids2.get(i));
            }
        }
    }

    private void assertExactMatch(ProductVariant v1, ProductVariant v2) {
        assertEquals(v1.getId(), v2.getId());
        assertEquals(v1.getTitle(), v2.getTitle());
        assertEquals(v1.getPrice(), v2.getPrice());
        assertEquals(v1.getGrams(), v2.getGrams());
        assertEquals(v1.getCompareAtPrice(), v2.getCompareAtPrice());
        assertEquals(v1.getSku(), v2.getSku());
        assertEquals(v1.isRequiresShipping(), v2.isRequiresShipping());
        assertEquals(v1.isTaxable(), v2.isTaxable());
        assertEquals(v1.getPosition(), v2.getPosition());
        assertEquals(v1.getProductId(), v2.getProductId());
        assertEquals(v1.getProductTitle(), v2.getProductTitle());
        assertEquals(v1.getCreatedAtDate(), v2.getCreatedAtDate());
        assertEquals(v1.getUpdatedAtDate(), v2.getUpdatedAtDate());
        assertEquals(v1.isAvailable(), v2.isAvailable());

        List<OptionValue> ov1 = v1.getOptionValues();
        List<OptionValue> ov2 = v2.getOptionValues();
        if (!CollectionUtils.isEmpty(ov1) || !CollectionUtils.isEmpty(ov2)) {
            for (int i = 0; i < ov1.size() || i < ov2.size(); i++) {
                assertExactMatch(ov1.get(i), ov2.get(i));
            }
        }
    }

    private void assertExactMatch(OptionValue ov1, OptionValue ov2) {
        assertEquals(ov1.getName(), ov2.getName());
        assertEquals(ov1.getOptionId(), ov2.getOptionId());
        assertEquals(ov1.getValue(), ov2.getValue());

    }

    private void assertExactMatch(Option o1, Option o2) {
        assertEquals(o1.getId(), o2.getId());
        assertEquals(o1.getName(), o2.getName());
        assertEquals(o1.getPosition(), o2.getPosition());
        assertEquals(o1.getProductId(), o2.getProductId());
    }

}
