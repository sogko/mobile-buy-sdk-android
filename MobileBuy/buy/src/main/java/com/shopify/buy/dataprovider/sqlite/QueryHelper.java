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

package com.shopify.buy.dataprovider.sqlite;

import android.content.ContentValues;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.internal.CollectionImage;
import com.shopify.buy.utils.DateUtility;

public class QueryHelper implements DatabaseConstants {

    static String createCollectionsTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_COLLECTIONS)
                .append(" (")
                .append(CollectionsTable.COLLECTION_ID).append(" INTEGER PRIMARY KEY, ")
                .append(CollectionsTable.TITLE).append(" TEXT, ")
                .append(CollectionsTable.BODY_HTML).append(" TEXT, ")
                .append(CollectionsTable.HANDLE).append(" TEXT, ")
                .append(CollectionsTable.PUBLISHED).append(" INTEGER, ")
                .append(CollectionsTable.PUBLISHED_AT).append(" TEXT, ")
                .append(CollectionsTable.CREATED_AT).append(" TEXT, ")
                .append(CollectionsTable.UPDATED_AT).append(" TEXT, ")
                .append(CollectionsTable.IMAGE_CREATED_AT).append(" TEXT, ")
                .append(CollectionsTable.IMAGE_SRC).append(" TEXT")
                .append(")");
        return sql.toString();
    }

    static ContentValues contentValues(Collection collection) {
        ContentValues values = new ContentValues();

        values.put(CollectionsTable.COLLECTION_ID, Long.parseLong(collection.getCollectionId()));
        values.put(CollectionsTable.TITLE, collection.getTitle());
        values.put(CollectionsTable.BODY_HTML, collection.getHtmlDescription());
        values.put(CollectionsTable.HANDLE, collection.getHandle());
        values.put(CollectionsTable.PUBLISHED, collection.isPublished() ? 1 : 0);
        values.put(CollectionsTable.PUBLISHED_AT, DateUtility.toString(collection.getPublishedAtDate()));
        values.put(CollectionsTable.CREATED_AT, DateUtility.toString(collection.getCreatedAtDate()));
        values.put(CollectionsTable.UPDATED_AT, DateUtility.toString(collection.getUpdatedAtDate()));

        CollectionImage image = collection.getImage();
        if (image != null) {
            values.put(CollectionsTable.IMAGE_CREATED_AT, image.getCreatedAt());
            values.put(CollectionsTable.IMAGE_SRC, image.getSrc());
        }

        return values;
    }

    static String createProductsTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_PRODUCTS)
                .append(" (")
                .append(ProductsTable.PRODUCT_ID).append(" INTEGER PRIMARY KEY, ")
                .append(ProductsTable.CHANNEL_ID).append(" TEXT, ")
                .append(ProductsTable.TITLE).append(" TEXT, ")
                .append(ProductsTable.HANDLE).append(" TEXT, ")
                .append(ProductsTable.BODY_HTML).append(" TEXT, ")
                .append(ProductsTable.PUBLISHED_AT).append(" TEXT, ")
                .append(ProductsTable.CREATED_AT).append(" TEXT, ")
                .append(ProductsTable.UPDATED_AT).append(" TEXT, ")
                .append(ProductsTable.VENDOR).append(" TEXT, ")
                .append(ProductsTable.PRODUCT_TYPE).append(" TEXT, ")
                .append(ProductsTable.TAGS).append(" TEXT, ")
                .append(ProductsTable.AVAILABLE).append(" INTEGER, ")
                .append(ProductsTable.PUBLISHED).append(" INTEGER")
                .append(")");
        return sql.toString();
    }

    static String createImagesTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_IMAGES)
                .append(" (")
                .append(ImagesTable.PRODUCT_ID).append(" INTEGER, ")
                .append(ImagesTable.POSITION).append(" INTEGER, ")
                .append(ImagesTable.CREATED_AT).append(" TEXT, ")
                .append(ImagesTable.UPDATED_AT).append(" TEXT, ")
                .append(ImagesTable.VARIANT_IDS).append(" TEXT, ")
                .append(ImagesTable.SRC).append(" TEXT, ")
                .append("PRIMARY KEY (").append(ImagesTable.PRODUCT_ID).append(", ").append(ImagesTable.POSITION).append(")")
                .append(")");
        return sql.toString();
    }

    static String createOptionsTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_OPTIONS)
                .append(" (")
                .append(OptionsTable.PRODUCT_ID).append(" INTEGER, ")
                .append(OptionsTable.POSITION).append(" INTEGER, ")
                .append(OptionsTable.NAME).append(" TEXT, ")
                .append("PRIMARY KEY (").append(OptionsTable.PRODUCT_ID).append(", ").append(OptionsTable.POSITION).append(")")
                .append(")");
        return sql.toString();
    }

    static String createOptionValuesTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_OPTION_VALUES)
                .append(" (")
                .append(OptionValuesTable.OPTION_ID).append(" INTEGER PRIMARY KEY, ")
                .append(OptionValuesTable.NAME).append(" TEXT, ")
                .append(OptionValuesTable.VALUE).append(" TEXT")
                .append(")");
        return sql.toString();
    }

    static String createProductVariantsTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(TABLE_PRODUCT_VARIANTS)
                .append(" (")
                .append(ProductVariantsTable.ID).append(" INTEGER PRIMARY KEY, ")
                .append(ProductVariantsTable.TITLE).append(" TEXT, ")
                .append(ProductVariantsTable.PRICE).append(" TEXT, ")
                .append(ProductVariantsTable.GRAMS).append(" INTEGER, ")
                .append(ProductVariantsTable.COMPARE_AT_PRICE).append(" TEXT, ")
                .append(ProductVariantsTable.SKU).append(" TEXT, ")
                .append(ProductVariantsTable.REQUIRES_SHIPPING).append(" INTEGER, ")
                .append(ProductVariantsTable.TAXABLE).append(" INTEGER, ")
                .append(ProductVariantsTable.POSITION).append(" INTEGER, ")
                .append(ProductVariantsTable.PRODUCT_ID).append(" INTEGER, ")
                .append(ProductVariantsTable.PRODUCT_TITLE).append(" TEXT, ")
                .append(ProductVariantsTable.CREATED_AT).append(" TEXT, ")
                .append(ProductVariantsTable.UPDATED_AT).append(" TEXT, ")
                .append(ProductVariantsTable.AVAILABLE).append(" INTEGER")
                .append(")");
        return sql.toString();
    }


}
